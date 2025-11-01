package io.fluidsonic.pdf

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*


internal abstract class AbstractPdfGeneratorService<Instance> : PdfGeneratorService {

	protected abstract suspend fun generateWithInstance(instance: Instance, input: PdfGenerationInput): PdfGenerationOutput
	protected abstract suspend fun startInstance(): Instance
	protected abstract suspend fun stopInstance(instance: Instance)


	private var state: State<Instance> = State.Stopped
	private val stateMutex = Mutex()


	final override suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput =
		stateMutex.withLock {
			when (val state = state) {
				is State.Started ->
					state.scope.async(start = CoroutineStart.LAZY) {
						generateWithInstance(state.instance, input)
					}

				State.Stopped -> error("generate() called while service is stopped. Call startIn() first.")
				State.Starting -> error("generate() not allowed while service is starting.")
				State.Stopping -> error("generate() not allowed while service is stopping.")
			}
		}.await()


	final override suspend fun startIn(scope: CoroutineScope) {
		stateMutex.withLock {
			when (state) {
				is State.Started -> error("startIn() called while already started.")
				State.Starting -> error("startIn() already in progress.")
				State.Stopped -> state = State.Starting
				State.Stopping -> error("startIn() not allowed while stopping; wait for stop() to finish.")
			}
		}

		val instance = try {
			startInstance()
		}
		catch (failure: Throwable) {
			stateMutex.withLock {
				check(state == State.Starting)
				state = State.Stopped
			}

			throw failure
		}

		stateMutex.withLock {
			check(state == State.Starting)

			val job = scope.coroutineContext.job
			val sessionJob = SupervisorJob(job)
			val sessionScope = CoroutineScope(
				scope.coroutineContext
					+ sessionJob
					+ CoroutineName("SessionPdfGeneratorService")
			)

			state = State.Started(instance, sessionScope)

			sessionJob.invokeOnCompletion {
				if (stateMutex.tryLock()) {
					try {
						if (state is State.Started)
							state = State.Stopped
					}
					finally {
						stateMutex.unlock()
					}
				}
			}
		}
	}


	final override suspend fun stop() {
		val (instance, scope) = stateMutex.withLock {
			when (val state = state) {
				is State.Started -> {
					this.state = State.Stopping
					state
				}

				State.Starting -> error("stop() not allowed while starting; try again after startIn() completes.")
				State.Stopped -> error("stop() called while already stopped.")
				State.Stopping -> error("stop() already in progress.")
			}
		}

		val job = scope.coroutineContext.job as CompletableJob
		job.complete()

		val joinFailure = try {
			job.join()
			null
		}
		catch (failure: Throwable) {
			failure
		}

		try {
			stopInstance(instance)
		}
		catch (failure: Throwable) {
			if (joinFailure != null) failure.addSuppressed(joinFailure)
			throw failure
		}
		finally {
			stateMutex.withLock {
				check(state == State.Stopping)
				state = State.Stopped
			}
		}

		joinFailure?.let { throw it }
	}


	private sealed interface State<out Session> {

		data class Started<out Session>(
			val instance: Session,
			val scope: CoroutineScope,
		) : State<Session>

		data object Starting : State<Nothing>
		data object Stopped : State<Nothing>
		data object Stopping : State<Nothing>
	}
}

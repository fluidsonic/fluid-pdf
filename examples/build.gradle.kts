import io.fluidsonic.gradle.*

fluidLibraryModule(description = "examples") {
	withoutPublishing()

	language {
		withoutExplicitApi()
	}

	targets {
		jvm {
			dependencies {
				implementation(rootProject)
				implementation(kotlinx("coroutines-core", "1.7.1"))
				implementation("org.slf4j:slf4j-simple:2.0.7")
			}
		}
	}
}

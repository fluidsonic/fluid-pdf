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
				implementation(kotlinx("coroutines-core", "1.4.2"))
				implementation("org.slf4j:slf4j-simple:1.7.30")
			}
		}
	}
}

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
				implementation(kotlinx("coroutines-core", "1.3.8-1.4.0-rc"))
				implementation("org.slf4j:slf4j-simple:1.7.30")
			}
		}
	}
}

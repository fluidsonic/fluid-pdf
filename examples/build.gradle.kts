import io.fluidsonic.gradle.*

fluidLibraryModule(description = "examples") {
	noDokka()
	withoutPublishing()

	language {
		withoutExplicitApi()
	}

	targets {
		jvm {
			dependencies {
				implementation(rootProject)
				implementation(kotlinx("coroutines-core", "1.10.2"))
				implementation("org.slf4j:slf4j-simple:2.0.17")
			}
		}
	}
}

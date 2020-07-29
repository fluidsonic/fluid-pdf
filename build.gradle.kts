import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.0"
}

fluidLibrary(name = "pdf", version = "0.9.1")

fluidLibraryModule(description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome") {
	publishSingleTargetAsModule()

	targets {
		jvm {
			dependencies {
				implementation(kotlinx("coroutines-core", "1.3.8-1.4.0-rc"))
				implementation("com.github.kklisura.cdt:cdt-java-client:2.1.0")
				implementation("org.apache.pdfbox:pdfbox:2.0.20")
			}
		}
	}
}

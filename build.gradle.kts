import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.3.2"
}

fluidLibrary(name = "pdf", version = "0.19.0")

fluidLibraryModule(description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("coroutines-core", "1.7.1"))
				implementation("io.fluidsonic.mirror:cdt-java-client:4.0.0-fluidsonic-1")
				implementation("org.apache.pdfbox:pdfbox:3.0.2")
			}
		}
	}
}

import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.2.1"
}

fluidLibrary(name = "pdf", version = "0.16.0")

fluidLibraryModule(description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("coroutines-core", "1.6.0"))
				implementation("com.github.kklisura.cdt:cdt-java-client:4.0.0")
				implementation("org.apache.pdfbox:pdfbox:2.0.26")
			}
		}
	}
}

import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.22"
}

fluidLibrary(name = "pdf", version = "0.14.5")

fluidLibraryModule(description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("coroutines-core", "1.4.3"))
				implementation("com.github.kklisura.cdt:cdt-java-client:3.0.0")
				implementation("org.apache.pdfbox:pdfbox:2.0.22")
			}
		}
	}
}

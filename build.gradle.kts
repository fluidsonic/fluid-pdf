import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.2.2"
}

fluidLibrary(name = "pdf", version = "0.16.0")

fluidLibraryModule(description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("coroutines-core", "1.6.4"))
				implementation("io.fluidsonic.mirror:cdt-java-client:4.0.0-fluidsonic-1")
				implementation("org.apache.pdfbox:pdfbox:2.0.27")
			}
		}
	}
}

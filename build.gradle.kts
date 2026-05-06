import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "3.0.0"
	kotlin("plugin.serialization") version "2.3.20"
}

fluidLibrary(name = "pdf", version = "0.32.1")

fluidLibraryModule(description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("coroutines-core", "1.10.2"))
				implementation("io.fluidsonic.mirror:cdt-java-client:4.0.0-fluidsonic-1")
				implementation("io.ktor:ktor-client-cio:3.4.1")
				implementation("io.ktor:ktor-client-content-negotiation:3.4.1")
				implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.1")
				implementation("org.apache.pdfbox:pdfbox:3.0.7")
				implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
				implementation("org.slf4j:slf4j-api:2.0.17")
			}
			testDependencies {
				implementation(kotlinx("coroutines-test", "1.10.2"))
			}
		}
	}
}

import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.0.12"
}

fluidJvmLibrary(name = "pdf", version = "0.9.1")

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	description = "Easy PDF generation with HTML & CSS using Chromium or Google Chrome"
}

dependencies {
	implementation(kotlinx("coroutines-core", "1.3.3"))
	implementation("com.github.kklisura.cdt:cdt-java-client:2.1.0")
	implementation("org.apache.pdfbox:pdfbox:2.0.20")
}

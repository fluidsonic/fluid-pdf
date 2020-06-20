import io.fluidsonic.gradle.*

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	publishing = false
}

dependencies {
	implementation(rootProject)
	implementation("org.slf4j:slf4j-simple:1.7.30")
}

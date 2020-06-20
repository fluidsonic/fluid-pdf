package io.fluidsonic.pdf


interface LazyChromiumPdfGenerator : ChromiumPdfGenerator {

	suspend fun start()
}

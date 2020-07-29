package io.fluidsonic.pdf


public interface LazyChromiumPdfGenerator : ChromiumPdfGenerator {

	public suspend fun start()
}

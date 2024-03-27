package com.github.shunsukesudo.minecraft2fa.tests.shared.util

import com.github.shunsukesudo.minecraft2fa.shared.util.QRCodeUtil
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class QRCodeUtilTest {

    @Test
    fun `Test QR code generation and check value inside of QR code is valid`() {
        println("=========== Test QR code generation and check value inside of QR code is valid")
        val expectedText = "TEST"

        println("Creating Image")
        val image = QRCodeUtil.generateQRCodeFromString(expectedText)

        println("Creating binary bitmap")
        val binaryBitmap = BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(image)))

        println("Decoding")
        val result = MultiFormatReader().decode(binaryBitmap)

        println("Expected: $expectedText | Actual: ${result.text}")
        Assertions.assertEquals(expectedText, result.text)
    }
}
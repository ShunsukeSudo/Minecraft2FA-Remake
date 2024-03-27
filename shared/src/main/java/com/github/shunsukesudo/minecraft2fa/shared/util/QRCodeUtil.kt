package com.github.shunsukesudo.minecraft2fa.shared.util

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import java.awt.image.BufferedImage

object QRCodeUtil {

    @JvmStatic
    fun generateQRCodeFromString(string: String): BufferedImage {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(string, BarcodeFormat.QR_CODE, 256, 256)
        return MatrixToImageWriter.toBufferedImage(bitMatrix)
    }
}
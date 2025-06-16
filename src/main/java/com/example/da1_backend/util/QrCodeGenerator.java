package com.example.da1_backend.util;

import io.nayuki.qrcodegen.QrCode;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QrCodeGenerator {

    /** Genera un PNG con el QR que codifica el texto dado. */
    public static byte[] generatePng(String text) throws IOException {
        QrCode qr = QrCode.encodeText(text, QrCode.Ecc.MEDIUM);
        BufferedImage img = toImage(qr, 8, 4);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "PNG", baos);
            return baos.toByteArray();
        }
    }

    /** Convierte el QrCode en una imagen blanco/negro. */
    private static BufferedImage toImage(QrCode qr, int scale, int border) {
        int size = qr.size;                       // usa el campo público `size`
        int imgSize = (size + border * 2) * scale;
        BufferedImage img = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imgSize; y++) {
            for (int x = 0; x < imgSize; x++) {
                // getModule devuelve true si el módulo está “on”
                boolean dark = qr.getModule(x / scale - border, y / scale - border);
                img.setRGB(x, y, dark ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return img;
    }
}

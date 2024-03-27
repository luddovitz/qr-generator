package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class QRGenerator {

    private int height;
    private int width;
    private ErrorCorrectionLevel errorCorrectionLevel;
    private final String imageFormat;
    private final BufferedImage bufferedImage;
    QRCodeWriter writer = new QRCodeWriter();

    public QRGenerator(String content,
                       int size,
                       ImageFormat format,
                       ErrorCorrectionLevel errorCorrectionLevel) {
        this.imageFormat = format.getFormatName();

        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE,
                    size,
                    size,
                    Map.of(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel));
            this.bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    byte[] getQRCodeInBytes() {
        try (var baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, imageFormat, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

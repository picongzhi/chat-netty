package com.pcz.chat.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author picongzhi
 */
public class QRCodeUtil {
    private static final String format = "png";

    /**
     * 创建二维码
     *
     * @param filePath 二维码临时存放路径
     * @param content  内容
     */
    public static void createQRCode(String filePath, String content) {
        int width = 300;
        int height = 300;

        Map hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            Path path = new File(filePath).toPath();

            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getContentFromQRCode(String filePath) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        File file = new File(filePath);
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(
                    new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

            Map hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            Result result = multiFormatReader.decode(binaryBitmap, hints);

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

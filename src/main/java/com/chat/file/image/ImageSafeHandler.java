package com.chat.file.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class ImageSafeHandler {
    public static boolean isImage(InputStream inputStream){
        Image img = null;
        try {
            img = ImageIO.read(inputStream);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }
}

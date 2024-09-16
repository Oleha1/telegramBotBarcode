package ru.oleha.utils;

import ru.oleha.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {
    private static int preFirstPrise = 0;
    private static int preLastPrise = 0;
    private static final Color black = new Color(0x000000);
    private static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isSameScreen() throws Exception {
        BufferedImage image = robot.createScreenCapture(new Rectangle(889,405,494,370));
        BufferedImage first = image.getSubimage(359,0,135,37);
        BufferedImage last = image.getSubimage(359,333,135,37);
        int firstPrice = getPrice(main.tesseractFirstItemPrice.doOCR(first));
        int lastPrice = getPrice(main.tesseractLastItemPrice.doOCR(last));
        if (preFirstPrise == firstPrice && preLastPrise == lastPrice) {
            return true;
        }
        preFirstPrise = firstPrice;
        preLastPrise = lastPrice;
        return false;
    }
    public static int getPrice(String string) {
        String result = string.replaceAll("[^0-9]", "");
        try {
            if (!string.isEmpty()) {
                int pars = Integer.parseInt(result);
                if (pars == 0) {
                    return -1;
                }
                return Integer.parseInt(result);
            }
        } catch (NumberFormatException ignore) {
            return -1;
        }
        return -1;
    }
    public static int getStack(String string) {
        String result = string.replaceAll("[^0-9]", "");
        if (result.isEmpty()) {
            return 1;
        } else {
            return Integer.parseInt(result);
        }
    }
    public static boolean isWhite(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return red > 195 && green > 195 && blue > 195;
    }
    public static BufferedImage white(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < pixels.length; i++) {
            Color color = new Color(pixels[i], true);
            if (isWhite(color)) {
                pixels[i] = color.getRGB();
            } else {
                pixels[i] = black.getRGB();
            }
        }

        result.setRGB(0, 0, width, height, pixels, 0, width);
        return result;
    }
}

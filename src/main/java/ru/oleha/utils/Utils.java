package ru.oleha.utils;

import ru.oleha.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Utils {
    private static int preFirstPrise = 0;
    private static int preLastPrise = 0;
    private static final Color black = new Color(0x000000);
    public static final Robot robot;

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
    public static boolean isGuiOpenAuction() {
        BufferedImage image = Utils.robot.createScreenCapture(new Rectangle(566,267,1,1));
        return image.getRGB(0, 0) == -14396823;
    }
    public static boolean isGuiMainMenu() {
        BufferedImage image = Utils.robot.createScreenCapture(new Rectangle(240,46,1,1));
        return image.getRGB(0,0) == -131587;
    }
    public static boolean isInGame() {
        BufferedImage image = Utils.robot.createScreenCapture(new Rectangle(1646,957,1,1));
        return image.getRGB(0,0) != -3328461;
    }
    public static boolean isSocketException() throws Exception {
        BufferedImage image = Utils.white(Utils.robot.createScreenCapture(new Rectangle(680,975,575,30)));
        String string = main.tesseractConnection.doOCR(image).replace("\n","");
        return string.equals("Соединение потеряно: Socket exception");
    }
    public static Rarity calcAvgNameColor(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        ArrayList<Color> colors = new ArrayList<>();
        for (int pixel : pixels) {
            Color color = new Color(pixel, true);
            if (color.getRed() == 18 && color.getGreen() == 20 && color.getBlue() == 21) {
                continue;
            }
            colors.add(color);
        }
        int r = 0;
        int g = 0;
        int b = 0;
        for (Color color : colors) {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }
        r /= colors.size();
        g /= colors.size();
        b /= colors.size();
        if (r > 80 && g > 80 && b > 80) {
            return Rarity.COMMON;
        } else if (r < 80 && g > 100 && b < 80) {
            return Rarity.UNCOMMON;
        } else if (r < 80 && g < 80 && b > 100) {
            return Rarity.SPECIAL;
        } else if (r > 50 && g < 40 && b > 50) {
            return Rarity.RARE;
        } else if (r > 100 && g < 80 && b < 80) {
            return Rarity.EXCEPTIONAL;
        } else if (r > 100 && g > 50 && b < 40) {
            return Rarity.LEGENDARY;
        } else {
            throw new IllegalStateException("unknown color " + String.format("r=%d,g=%d,b=%d",r,g,b));
        }
        //обычный 127 128 128 / 124 125 125 / 125 126 127
        //необычный 47 124 49 / 47 123 49 / 48 126 49
        //особый 48 49 127 / 48 49 125 / 47 48 122
        //редкий 85 11 87 / 84 11 86 / 86 10 88
        //искл 123 49 49 / 126 49 50 / 122 48 49
        //лег 124 87 11 / 125 87 11 / 127 89 11
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
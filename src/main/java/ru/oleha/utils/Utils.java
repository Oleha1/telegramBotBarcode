package ru.oleha.utils;

import ru.oleha.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {
    private static int preFirstPrise = 0;
    private static int preLastPrise = 0;
    private static final Color black = new Color(0x000000);

//    public static void createImgItemStack() throws Exception {
//        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//        BufferedImage subImage = image.getSubimage((image.getWidth() / 2) - 70,(image.getHeight() / 2) - 120,34,360);
//        int y = 0;
//        for (int i = 0; i < 10; i++) {
//            BufferedImage imageSubimage = Utils.white(subImage.getSubimage(0,y,34,18));
////            ImageIO.write(imageSubimage,"png",new File("./xyi"+i+".png"));
//            main.imgItemStack.add(imageSubimage);
//            y+=37;
//        }
//    }

//    public static void createImgItemName() throws Exception {
//        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//        BufferedImage subImage = image.getSubimage((image.getWidth() / 2) - 35,(image.getHeight() / 2) - 135,210,360);
//        int y = 0;
//        for (int i = 0; i < 10; i++) {
//            BufferedImage imageSubimage = subImage.getSubimage(0,y,210,18);
////            ImageIO.write(imageSubimage,"png",new File("./xyi"+i+".png"));
//            main.imgItemName.add(imageSubimage);
//            y += 37;
//        }
//    }
    public static BufferedImage preprocessImage(BufferedImage img) {
        BufferedImage processedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D graphics = processedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return processedImage;
    }
    public static boolean isSameScreen() throws Exception {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        BufferedImage first = image.getSubimage((image.getWidth() / 2) + 288,(image.getHeight() / 2) - 135,135,37);
        BufferedImage last = image.getSubimage((image.getWidth() / 2) + 288,(image.getHeight() / 2) + 198,135,37);
        int firstPrice = Utils.getPrice(main.tesseractFirstItemPrice.doOCR(first));
        int lastPrice = Utils.getPrice(main.tesseractLastItemPrice.doOCR(last));
        if (preFirstPrise == firstPrice && preLastPrise == lastPrice) {
            return true;
        }
        preFirstPrise = firstPrice;
        preLastPrise = lastPrice;
        return false;
    }
//    public static void createImgPrice() throws Exception {
//        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//        BufferedImage subImage = image.getSubimage((image.getWidth() / 2) + 288,(image.getHeight() / 2) - 135,135,360);
//        int y = 0;
//        for (int i = 0; i < 10; i++) {
//            BufferedImage imageSubimage = subImage.getSubimage(0,y,135,27);
////            ImageIO.write(imageSubimage,"png",new File("./xyi"+i+".png"));
//            main.imgItemPrice.add(imageSubimage);
//            y += 37;
//        }
//    }
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
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x,y);
                Color color = new Color(rgb,true);
                if (isWhite(color)) {
                    result.setRGB(x,y,rgb);
                } else {
                    result.setRGB(x,y, black.getRGB());
                }
            }
        }
        return result;
    }
}

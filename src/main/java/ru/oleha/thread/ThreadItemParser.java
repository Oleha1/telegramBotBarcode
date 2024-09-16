package ru.oleha.thread;

import net.sourceforge.tess4j.Tesseract;
import ru.oleha.main;
import ru.oleha.utils.ItemInfo;
import ru.oleha.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

public class ThreadItemParser extends Thread {
    private final CountDownLatch countDownLatch;
    private final BufferedImage image;

    public ThreadItemParser(CountDownLatch countDownLatch,BufferedImage image) {
        this.countDownLatch = countDownLatch;
        this.image = image;
    }

    @Override
    public void run() {
        try {
            Tesseract tesseractItemStack = new Tesseract();
            Tesseract tesseractItemName = new Tesseract();
            Tesseract tesseractItemPrice = new Tesseract();
            tesseractItemStack.setDatapath("./tessdata");
            tesseractItemStack.setLanguage("rusb");
            tesseractItemName.setDatapath("./tessdata");
            tesseractItemName.setLanguage("rusf");
            tesseractItemPrice.setDatapath("./tessdata");
            tesseractItemPrice.setLanguage("rusf");
            BufferedImage imgItemStack = Utils.white(image.getSubimage(0,18,36,18));
            BufferedImage imgItemName = image.getSubimage(38,0,206,18);
            BufferedImage imgItemPrice = image.getSubimage(360,5,134,24);
            int itemStack = Utils.getStack(tesseractItemStack.doOCR(imgItemStack));
            String itemName = tesseractItemName.doOCR(imgItemName);
            int itemPrice = Utils.getPrice(tesseractItemPrice.doOCR(imgItemPrice));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int)(screen.getWidth() / 2) - imgItemPrice.getTileGridXOffset();
            int y = (int)(screen.getHeight() / 2) - imgItemPrice.getTileGridYOffset();
            main.itemInfos.add(new ItemInfo(itemName,itemPrice,itemStack,x, y - 115,image));
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        } finally {
            countDownLatch.countDown();
        }
    }
}

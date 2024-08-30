package ru.oleha.thread;

import ru.oleha.main;
import ru.oleha.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadCreateImg extends Thread {
    int x;
    int y;
    private File file = new File("./imgs");

    public ThreadCreateImg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        try {
            if (!file.exists()) {
                file.mkdir();
            }
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            BufferedImage subImage = image.getSubimage(x - 435,y - 5,510,75);
            ImageIO.write(subImage,"png",new File(file.toPath() +"/" + file.listFiles().length + ".png"));
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }
}

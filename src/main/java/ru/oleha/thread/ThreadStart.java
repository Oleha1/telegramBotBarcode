package ru.oleha.thread;

import ru.oleha.main;
public class ThreadStart extends Thread {
    @Override
    public void run() {
        try {
            do {
                main.start();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            } while (main.loop.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRunning() {
        this.interrupt();
    }
}

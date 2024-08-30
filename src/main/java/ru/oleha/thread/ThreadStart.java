package ru.oleha.thread;

import ru.oleha.main;
public class ThreadStart extends Thread {
    private volatile boolean running = false;
    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                main.start();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            running = false;
        }
    }

    public void stopRunning() {
        running = false;
        this.interrupt();
    }
}

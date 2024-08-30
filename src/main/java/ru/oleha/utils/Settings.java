package ru.oleha.utils;

public class Settings {
    private int keyIDStart;
    private int keyIDExit;
    private boolean loop;
    private boolean logs;
    public Settings() {

    }

    public Settings(int keyIDStart,int keyIDExit, boolean loop, boolean logs) {
        this.keyIDStart = keyIDStart;
        this.keyIDExit = keyIDExit;
        this.loop = loop;
        this.logs = logs;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isLogs() {
        return logs;
    }

    public void setLogs(boolean logs) {
        this.logs = logs;
    }

    public int getKeyIDStart() {
        return keyIDStart;
    }

    public void setKeyIDStart(int keyIDStart) {
        this.keyIDStart = keyIDStart;
    }

    public int getKeyIDExit() {
        return keyIDExit;
    }

    public void setKeyIDExit(int keyIDExit) {
        this.keyIDExit = keyIDExit;
    }
}

package com.mikeldpl.uifxconsole;

import com.mikeldpl.uifxconsole.exceptions.UiFxConsoleException;
import javafx.application.Application;

import java.util.Objects;

public class UiFxConsole {

    private static final UiFxConsole instance = new UiFxConsole();
    private static final Configurer configurer = new Configurer(instance);
    final Object lock = new Object();
    volatile String title = "Console";
    private volatile Runnable onClose = () -> {};
    private volatile boolean daemon = true;
    private volatile boolean waitForWindowStartup = true;
    private volatile boolean isStarted = false;

    private UiFxConsole() {
    }

    public static UiFxConsole getInstance() {
        return instance;
    }

    public static Configurer configure() {
        return configurer;
    }

    public void start(String... args) {
        synchronized (lock) {
            checkIsNotStarted();
            createThread(args).start();
            markAsStarted();
            Runtime.getRuntime().addShutdownHook(new Thread(onClose));
            if (waitForWindowStartup) {
                try {
                    lock.wait(1500);
                } catch (InterruptedException e) {
                    throw new UiFxConsoleException(e);
                }
            }
        }
    }

    private void markAsStarted() {
        isStarted = true;
    }

    private Thread createThread(String[] args) {
        Thread thread = new Thread(() -> Application.launch(ConsoleWindow.class, args));
        thread.setName(title + " - UiFxConsole Thread");
        thread.setDaemon(daemon);
        return thread;
    }

    public boolean isWaitForWindowStartup() {
        return waitForWindowStartup;
    }

    public void setWaitForWindowStartup(boolean waitForWindowStartup) {
        checkIsNotStarted();
        this.waitForWindowStartup = waitForWindowStartup;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        checkIsNotStarted();
        this.daemon = daemon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Objects.requireNonNull(title);
        checkIsNotStarted();
        this.title = title;
    }

    public Runnable getOnClose() {
        return onClose;
    }

    public void setOnClose(Runnable onClose) {
        Objects.requireNonNull(onClose);
        this.onClose = onClose;
    }

    private void checkIsNotStarted() {
        if (isStarted) {
            throw new UiFxConsoleException("UiFxConsole has been already started. Or it was started with exception.");
        }
    }


    public static class Configurer {

        private final UiFxConsole console;

        private Configurer(UiFxConsole inst) {
            this.console = inst;
        }

        public Configurer onClose(Runnable onClose) {
            console.setOnClose(onClose);
            return this;
        }

        public Configurer title(String title) {
            console.setTitle(title);
            return this;
        }

        public Configurer demon(boolean demon) {
            console.setDaemon(demon);
            return this;
        }

        public Configurer waitForWindowStartup(boolean waitForWindowStartup) {
            console.setWaitForWindowStartup(waitForWindowStartup);
            return this;
        }

        public void start(String... args) {
            console.start(args);
        }
    }
}

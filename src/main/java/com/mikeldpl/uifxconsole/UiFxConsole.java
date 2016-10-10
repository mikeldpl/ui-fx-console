package com.mikeldpl.uifxconsole;

import com.mikeldpl.uifxconsole.exceptions.UiFxConsoleException;
import javafx.application.Application;

import java.util.Objects;

public class UiFxConsole {

    static final Object LOCK = new Object();
    private static final Configurer CONFIGURER = new Configurer();
    static volatile String title = "Console";
    private static volatile Runnable onClose = () -> {
    };
    private static volatile boolean daemon = true;
    private static volatile boolean waitForWindowStartup = true;
    private static volatile boolean isStarted = false;

    private UiFxConsole() {
    }

    public static void start(String... args) {
        synchronized (LOCK) {
            checkIsNotStarted();
            createThread(args).start();
            markAsStarted();
            Runtime.getRuntime().addShutdownHook(new Thread(UiFxConsole.onClose));
            if (waitForWindowStartup) {
                try {
                    LOCK.wait(1500);
                } catch (InterruptedException e) {
                    throw new UiFxConsoleException(e);
                }
            }
        }
    }

    private static void markAsStarted() {
        isStarted = true;
    }

    private static Thread createThread(String[] args) {
        Thread thread = new Thread(() -> Application.launch(ConsoleWindow.class, args));
        thread.setName(title + " - UiFxConsole Thread");
        thread.setDaemon(daemon);
        return thread;
    }

    public static boolean isWaitForWindowStartup() {
        return waitForWindowStartup;
    }

    public static void setWaitForWindowStartup(boolean waitForWindowStartup) {
        checkIsNotStarted();
        UiFxConsole.waitForWindowStartup = waitForWindowStartup;
    }

    public static boolean isDaemon() {
        return daemon;
    }

    public static void setDaemon(boolean daemon) {
        checkIsNotStarted();
        UiFxConsole.daemon = daemon;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        Objects.requireNonNull(title);
        checkIsNotStarted();
        UiFxConsole.title = title;
    }

    public static Runnable getOnClose() {
        return onClose;
    }

    public static void setOnClose(Runnable onClose) {
        Objects.requireNonNull(onClose);
        UiFxConsole.onClose = onClose;
    }

    public static Configurer configure() {
        return CONFIGURER;
    }

    private static void checkIsNotStarted() {
        if (isStarted) {
            throw new UiFxConsoleException("UiFxConsole has been already started. Or it was started with exception.");
        }
    }


    public static class Configurer {

        private Configurer() {
        }

        public Configurer onClose(Runnable onClose) {
            setOnClose(onClose);
            return this;
        }

        public Configurer title(String title) {
            setTitle(title);
            return this;
        }

        public Configurer demon(boolean demon) {
            setDaemon(demon);
            return this;
        }

        public Configurer waitForWindowStartup(boolean waitForWindowStartup) {
            setWaitForWindowStartup(waitForWindowStartup);
            return this;
        }

        public void start(String... args) {
            UiFxConsole.start(args);
        }
    }
}

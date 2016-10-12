package com.github.mikeldpl.uifxconsole;

import com.github.mikeldpl.uifxconsole.exceptions.UiFxConsoleException;
import javafx.application.Application;

import java.util.Objects;

/**
 * The {@code UiFxConsole} class represents start point of UI Console.
 * {@code UiFxConsole} is singleton.
 */
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

    /**
     * This method returns an instance of {@code UiFxConsole}.
     *
     * @return Instance of class {@code UiFxConsole}
     */
    public static UiFxConsole getInstance() {
        return instance;
    }

    /**
     * This method returns an instance of {@code UiFxConsole.Configurer}.
     * This object is some kind of builder.
     * <blockquote><pre>
     *     UiFxConsole console = UiFxConsole.getInstance();
     *     console.setDemon(false);
     *     console.start();
     * </pre></blockquote>
     * is the same as
     * <blockquote><pre>
     *     UiFxConsole.configure().demon(false).start();
     * </pre></blockquote>
     *
     * @return Instance of class {@code UiFxConsole.Configurer}
     */
    public static Configurer configure() {
        return configurer;
    }

    /**
     * Loads a window of UI Console
     *
     * @param args Console line arguments
     * @throws UiFxConsoleException <ul><li>If console window was already started</li>
     *                              <li>If current thread was interrupted</li></ul>
     */
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

    /**
     * If {@code true} thread that call {@code starts()}, will wait for load of the window.
     *
     * @return Whether thread will be wait for windows appearance
     */
    public boolean isWaitForWindowStartup() {
        return waitForWindowStartup;
    }

    /**
     * Sets whether thread will be wait for windows appearance.
     *
     * @throws UiFxConsoleException If console window was already started
     */
    public void setWaitForWindowStartup(boolean waitForWindowStartup) {
        checkIsNotStarted();
        this.waitForWindowStartup = waitForWindowStartup;
    }

    /**
     * If {@code true}, then process will be finished, when all thread be finished
     *
     * @return Whether UI thread will be demon
     */
    public boolean isDaemon() {
        return daemon;
    }

    /**
     * Sets whether UI thread will be demon.
     *
     * @throws UiFxConsoleException If console window was already started
     */
    public void setDaemon(boolean daemon) {
        checkIsNotStarted();
        this.daemon = daemon;
    }

    /**
     * Title of console window.
     *
     * @return Window's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets window's title.
     *
     * @throws UiFxConsoleException If console window was already started
     */
    public void setTitle(String title) {
        Objects.requireNonNull(title);
        checkIsNotStarted();
        this.title = title;
    }

    /**
     * Callback on should down of JVM.
     */
    public Runnable getOnClose() {
        return onClose;
    }

    /**
     * Sets callback on should down of JVM.
     * Could be called after start of console window.
     */
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

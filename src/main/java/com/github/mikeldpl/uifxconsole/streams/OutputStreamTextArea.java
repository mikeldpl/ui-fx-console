package com.github.mikeldpl.uifxconsole.streams;

import javafx.application.Platform;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * {@code OutputStreamTextArea} is {@code OutputStream} that can write
 * to {@link javafx.scene.control.TextArea}
 */
public class OutputStreamTextArea extends OutputStream {

    private TextArea textArea;
    private volatile boolean notFollow;

    public OutputStreamTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        appendText(String.valueOf((char) b));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        appendText(new String(b, off, len));
    }

    private synchronized void appendText(String text) {
        Platform.runLater(() -> {
            if (notFollow) {
                IndexRange selection = textArea.getSelection();
                textArea.appendText(text);
                textArea.selectRange(selection.getStart(), selection.getEnd());
            } else {
                textArea.appendText(text);
            }
        });
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    public synchronized void setFollow(boolean follow) {
        this.notFollow = !follow;
    }
}
package com.mikeldpl.uifxconsole.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class DuplicateOutputStream extends OutputStream {

    private OutputStream[] outputStreams;

    public DuplicateOutputStream(OutputStream... outputStreams) {
        this.outputStreams = Arrays.copyOf(outputStreams, outputStreams.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(b, off, len);
        }
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(b);
        }
    }
}

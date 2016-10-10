package com.mikeldpl.uifxconsole.exceptions;

public class UiFxConsoleException extends RuntimeException {
    public UiFxConsoleException() {
    }

    public UiFxConsoleException(String message) {
        super(message);
    }

    public UiFxConsoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public UiFxConsoleException(Throwable cause) {
        super(cause);
    }
}

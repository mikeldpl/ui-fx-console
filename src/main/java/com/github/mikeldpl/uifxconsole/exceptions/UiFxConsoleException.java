package com.github.mikeldpl.uifxconsole.exceptions;


/**
 * {@code UiFxConsoleException} - own exception of com.mikeldpl.uifxconsole.exceptions package.
 */
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

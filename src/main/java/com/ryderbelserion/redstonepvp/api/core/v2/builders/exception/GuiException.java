package com.ryderbelserion.redstonepvp.api.core.v2.builders.exception;

/**
 * @author Matt
 */
public final class GuiException extends RuntimeException {

    public GuiException(String message) {
        super(message);
    }

    public GuiException(String message, Exception cause) {
        super(message, cause);
    }
}
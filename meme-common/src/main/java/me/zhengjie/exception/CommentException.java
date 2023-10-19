//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.zhengjie.exception;

public class CommentException extends RuntimeException {
    public CommentException() {
    }

    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Object... obj) {
        super(String.format(message, obj));
    }

    public CommentException(Throwable cause, String message, Object... obj) {
        super(String.format(message, obj), cause);
    }

    public CommentException(Throwable cause) {
        super(cause);
    }

    public CommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

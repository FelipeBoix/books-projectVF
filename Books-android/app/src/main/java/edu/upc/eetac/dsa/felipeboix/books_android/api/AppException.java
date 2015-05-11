package edu.upc.eetac.dsa.felipeboix.books_android.api;

/**
 * Created by Felipe on 09/05/2015.
 */
public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(String detailMessage) {
        super(detailMessage);
    }
}
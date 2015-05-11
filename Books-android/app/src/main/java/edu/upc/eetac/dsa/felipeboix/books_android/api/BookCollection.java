package edu.upc.eetac.dsa.felipeboix.books_android.api;

/**
 * Created by Felipe on 09/05/2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookCollection {
    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    private List<Book> books;
    private long newestTimestamp;
    private long oldestTimestamp;
    private Map<String, Link> links = new HashMap<String, Link>();

    public BookCollection() {
        super();
        books = new ArrayList<Book>();
    }
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> stings) {
        this.books = books;
    }

    public void addSting(Book book) {
        books.add(book);
    }

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public Map<String, Link> getLinks() {
        return links;
    }
}
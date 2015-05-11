package edu.upc.eetac.dsa.felipeboix.books_android.api;

/**
 * Created by Felipe on 09/05/2015.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class BookAPI {
    private final static String TAG = BookAPI.class.getName();
    private static BookAPI instance = null;
    private URL url;

    private BookRootAPI rootAPI = null;

    private BookAPI(Context context) throws IOException, AppException {
        super();

        AssetManager assetManager = context.getAssets();
        Properties config = new Properties();
        config.load(assetManager.open("config.properties"));
        String urlHome = config.getProperty("books.home");
        url = new URL(urlHome);

        Log.d("LINKS", url.toString());
        getRootAPI();
    }

    public final static BookAPI getInstance(Context context) throws AppException {
        if (instance == null)
            try {
                instance = new BookAPI(context);
            } catch (IOException e) {
                throw new AppException(
                        "Can't load configuration file");
            }
        return instance;
    }

    private void getRootAPI() throws AppException {
        Log.d(TAG, "getRootAPI()");
        rootAPI = new BookRootAPI();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Book API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, rootAPI.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Beeter API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Beeter Root API");
        }

    }

    public BookCollection getBooks() throws AppException {
        Log.d(TAG, "getBook()");
        BookCollection books = new BookCollection();
        System.out.println("DENTRO...");
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("books").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Book API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, books.getLinks());

            books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
            books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
            JSONArray jsonBooks = jsonObject.getJSONArray("books");
            for (int i = 0; i < jsonBooks.length(); i++) {
                Book book = new Book();
                JSONObject jsonBook = jsonBooks.getJSONObject(i);

                book.setId(jsonBook.getInt("id"));
                System.out.println("ID"+book.getId());
                book.setFecha_edicion(jsonBook.getLong("fecha_edicion"));
                book.setFecha_impresion(jsonBook.getLong("fecha_impresion"));
                book.setTitulo(jsonBook.getString("titulo"));
                book.setLengua(jsonBook.getString("lengua"));
                book.setEdicion(jsonBook.getString("edicion"));
                book.setEditorial(jsonBook.getString("editorial"));
                jsonLinks = jsonBook.getJSONArray("links");
                parseLinks(jsonLinks, book.getLinks());
                books.getBooks().add(book);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Beeter API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Beeter Root API");
        }

        return books;
    }

    private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
            throws AppException, JSONException {
        for (int i = 0; i < jsonLinks.length(); i++) {
            Link link = null;
            try {
                link = SimpleLinkHeaderParser
                        .parseLink(jsonLinks.getString(i));
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
            String rel = link.getParameters().get("rel");
            String rels[] = rel.split("\\s");
            for (String s : rels)
                map.put(s, link);
        }
    }
    private Map<String, Book> stingsCache = new HashMap<String, Book>();

    public Book getBooks(String urlSting) throws AppException {
        Book book = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlSting);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            book = stingsCache.get(urlSting);
            String eTag = (book == null) ? null : book.getETag();
            if (eTag != null)
                urlConnection.setRequestProperty("If-None-Match", eTag);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.d(TAG, "CACHE");
                return stingsCache.get(urlSting);
            }
            Log.d(TAG, "NOT IN CACHE");
            book = new Book();
            eTag = urlConnection.getHeaderField("ETag");
            book.setETag(eTag);
            stingsCache.put(urlSting, book);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonBook = new JSONObject(sb.toString());
            book.setId(jsonBook.getInt("id"));
            System.out.println("ID" + book.getId());
            book.setFecha_edicion(jsonBook.getLong("fecha_edicion"));
            book.setFecha_impresion(jsonBook.getLong("fecha_impresion"));
            book.setTitulo(jsonBook.getString("titulo"));
            book.setLengua(jsonBook.getString("lengua"));
            book.setEdicion(jsonBook.getString("edicion"));
            book.setEditorial(jsonBook.getString("editorial"));
            JSONArray jsonLinks = jsonBook.getJSONArray("links");
            parseLinks(jsonLinks, book.getLinks());

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Bad sting url");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception when getting the sting");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception parsing response");
        }

        return book;
    }
}
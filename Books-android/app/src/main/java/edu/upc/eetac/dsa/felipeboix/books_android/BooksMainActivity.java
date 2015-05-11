package edu.upc.eetac.dsa.felipeboix.books_android;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.felipeboix.books_android.api.AppException;
import edu.upc.eetac.dsa.felipeboix.books_android.api.Book;
import edu.upc.eetac.dsa.felipeboix.books_android.api.BookAPI;
import edu.upc.eetac.dsa.felipeboix.books_android.api.BookCollection;

public class BooksMainActivity extends ListActivity {
    private class FetchStingsTask extends
            AsyncTask<Void, Void, BookCollection> {
        private ProgressDialog pd;

        @Override
        protected BookCollection doInBackground(Void... params) {
            BookCollection books = null;
            try {
                books = BookAPI.getInstance(BooksMainActivity.this)
                        .getBooks();
            } catch (AppException e) {
                e.printStackTrace();
            }
            return books;
        }

        @Override
       /* protected void onPostExecute(BookCollection result) {
            ArrayList<Book> stings = new ArrayList<Book>(result.getBooks());
            for (Book s : stings) {
                Log.d(TAG, s.getId() + "-" + s.getTitulo());
            }
            if (pd != null) {
                pd.dismiss();
            }
        }*/

        protected void onPostExecute(BookCollection result) {
            addBooks(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BooksMainActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }
    private final static String TAG = BooksMainActivity.class.toString();

    private static final String[] items = { "lorem", "ipsum", "dolor", "sit",
            "amet", "consectetuer", "adipiscing", "elit", "morbi", "vel",
            "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam", "vel",
            "erat", "placerat", "ante", "porttitor", "sodales", "pellentesque",
            "augue", "purus" };

  //  private ArrayAdapter<String> adapter;
    private ArrayList<Book> booksList;
    private BookAdapter adapter;
    @Override
  /*  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_main);
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alicia", "alicia"
                        .toCharArray());
            }
        });
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
        (new FetchStingsTask()).execute();
    }*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_main);

        booksList = new ArrayList<Book>();
        adapter = new BookAdapter(this, booksList);
        setListAdapter(adapter);

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alicia", "alicia"
                        .toCharArray());
            }
        });
        (new FetchStingsTask()).execute();
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Book book= booksList.get(position);
        Log.d(TAG, book.getLinks().get("self").getTarget());

        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("url", book.getLinks().get("self").getTarget());
        startActivity(intent);
    }
    private void addBooks(BookCollection books){
        booksList.addAll(books.getBooks());
        adapter.notifyDataSetChanged();
    }
}
package edu.upc.eetac.dsa.felipeboix.books_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.felipeboix.books_android.api.AppException;
import edu.upc.eetac.dsa.felipeboix.books_android.api.Book;
import edu.upc.eetac.dsa.felipeboix.books_android.api.BookAPI;

/**
 * Created by Felipe on 09/05/2015.
 */
public class BookDetailActivity extends Activity{
    private final static String TAG = BookDetailActivity.class.getName();
    private class FetchStingTask extends AsyncTask<String, Void, Book> {
        private ProgressDialog pd;

        @Override
        protected Book doInBackground(String... params) {
            Book book = null;
            try {
                book = BookAPI.getInstance(BookDetailActivity.this)
                        .getBooks(params[0]);
            } catch (AppException e) {
                Log.d(TAG, e.getMessage(), e);
            }
            return book;
        }



        @Override
        protected void onPostExecute(Book result) {
            loadBook(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BookDetailActivity.this);
            pd.setTitle("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);
        String urlSting = (String) getIntent().getExtras().get("url");
        (new FetchStingTask()).execute(urlSting);
    }
    private void loadBook(Book book) {
        TextView tvDetailEditorial = (TextView) findViewById(R.id.tvDetailEditorial);
        TextView tvDetailLengua = (TextView) findViewById(R.id.tvDetailLengua);
        TextView tvDetailTitulo = (TextView) findViewById(R.id.tvDetailTitulo);
       // TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);

        tvDetailEditorial.setText(book.getEditorial());
        tvDetailLengua.setText(book.getLengua());
        tvDetailTitulo.setText(book.getTitulo());
       /* tvDetailDate.setText(SimpleDateFormat.getInstance().format(
                book.getLastModified()));*/
    }




}

package edu.upc.eetac.dsa.felipeboix.books_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.felipeboix.books_android.api.Book;


/**
 * Created by Felipe on 27/04/2015.
 */
public class BookAdapter extends BaseAdapter{
    private ArrayList<Book> data;
    LayoutInflater inflater;
    public BookAdapter(Context context, ArrayList<Book> data) {

        super();
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Book) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_book, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitulo = (TextView) convertView
                    .findViewById(R.id.tvTitulo);
            viewHolder.tvEditorial = (TextView) convertView
                    .findViewById(R.id.tvEditorial);
            viewHolder.tvidlibro = (TextView) convertView
                    .findViewById(R.id.tvidLibro);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String titulo = data.get(position).getTitulo();
        String editorial = data.get(position).getEditorial();
        int idlibro = data.get(position).getId();
        viewHolder.tvTitulo.setText(titulo);
        viewHolder.tvEditorial.setText(editorial);
        String libro= Integer.toString(idlibro);
        viewHolder.tvidlibro.setText(libro);
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitulo;
        TextView tvEditorial;
        TextView tvidlibro;
    }

}

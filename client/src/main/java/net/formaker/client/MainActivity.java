package net.formaker.client;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import net.formaker.client.DataBase.CRUD.dbForm;
import net.formaker.client.DataBase.DBHelper;


public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    ListView lvForms;
    dbForm table_form;
    SimpleCursorAdapter scAdapter;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table_form = new dbForm(this);
        table_form.open();

        // формируем столбцы сопоставления
        String[] from = new String[]{DBHelper.FORM_NAME, DBHelper.FORM_TITLE};
        int[] to = new int[]{R.id.tvFormName, R.id.tvFormTitle};

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
        scAdapter = new MySimpleCursorAdapter(this, R.layout.listform_item, null, from, to, 0);
        lvForms = (ListView) findViewById(R.id.lvForms);
        lvForms.setAdapter(scAdapter);
        registerForContextMenu(lvForms);
    }

    public void onResume() {
        super.onResume();
        // Restart loader so that it refreshes displayed items according to database
        refreshUI();
    }

    public void refreshUI() {
        getSupportLoaderManager().restartLoader(0x01, null, this);
    }

    // обработка нажатия кнопки
    public void onButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    protected void onDestroy() {
        super.onDestroy();
        table_form.close();
        // закрываем подключение при выходе
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, table_form);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }

    static class MyCursorLoader extends CursorLoader {

        dbForm table_form;

        public MyCursorLoader(Context context, dbForm table_form) {
            super(context);
            this.table_form = table_form;
        }

        @Override
        public Cursor loadInBackground() {
            return table_form.getAllForms();
        }

    }

    public class MySimpleCursorAdapter extends SimpleCursorAdapter {

        // Context context;
        //  Cursor cursor;

        public MySimpleCursorAdapter(Context contxt, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(contxt, layout, c, from, to, flags);
            //context = contxt;
            //cursor = c;
        }

        public View newView(Context _context, Cursor _cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.listform_item, parent, false);
        }

        @Override
        public void bindView(View view, Context Context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.FORM_NAME));
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.FORM_TITLE));
            TextView formname = (TextView) view.findViewById(R.id.tvFormName);
            formname.setText(name);
            TextView formtitle = (TextView) view.findViewById(R.id.tvFormTitle);
            formtitle.setText(title);
            ImageButton ibtnDelete = (ImageButton) view.findViewById(R.id.ibtnDelete);
            ibtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view != null) {
                        Object obj = view.getTag();
                        //dbForm form = new dbForm(context);
                        //form.open();
                        String st = obj.toString();
                        table_form.deleteForm(Long.valueOf(st));
                        refreshUI();
                    }
                }
            });
            Object obj = cursor.getString(cursor.getColumnIndex(DBHelper.FORM_ID));
            ibtnDelete.setTag(obj);


        }
    }

}
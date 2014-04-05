package net.formaker.client.DataBase.CRUD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.formaker.client.DataBase.DBHelper;


/**
 * Created by NikOS on 01.02.14.
 */
public class dbForm {

    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public dbForm(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // получить все данные из таблицы TABLE_FORM
    public Cursor getAllForms() {

        return db.query(DBHelper.TABLE_FORM, null, null, null, null, null, null);
    }

    public void deleteForm(long id) {
        db.delete(DBHelper.TABLE_FORM, DBHelper.FORM_ID + " = " + id, null);

    }

}

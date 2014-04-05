package net.formaker.client.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NikOS on 28.01.14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "formaker.db";
    public static final int DB_VERSION = 19;

    // определяем таблицу форм
    public static final String TABLE_FORM = "Form";
    public static final String FORM_ID = "_id";
    public static final String FORM_NAME = "name";
    public static final String FORM_TITLE = "title";
    private static final String CREATE_TABLE_FORM =
            "CREATE TABLE IF NOT EXISTS " + TABLE_FORM + "(" +
                    FORM_ID + " integer primary key, " +
                    FORM_NAME + " text not null, " +
                    FORM_TITLE + " text not null " +
                    ");";

    // определяем таблицу
    public static final String TABLE_QUESTION = "Question";
    public static final String QUESTION_ID = "_id";
    public static final String QUESTION_FORM_ID = "form_id";
    public static final String QUESTION_QUESTION = "question";
    public static final String QUESTION_TYPE = "type";
    public static final String QUESTION_REQUIRED = "required";
    private static final String CREATE_TABLE_QUESTION =
            "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "(" +
                    QUESTION_ID + " integer primary key," +
                    QUESTION_FORM_ID + " integer, " +
                    QUESTION_QUESTION + " text not null, " +
                    QUESTION_TYPE + " integer, " +
                    QUESTION_REQUIRED + " integer, " +
                    " FOREIGN KEY (" + QUESTION_FORM_ID + ") REFERENCES " + TABLE_FORM + "(" + FORM_ID + ") ON DELETE CASCADE" +
                    ");";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FORM);
        ContentValues cv = new ContentValues();
        for (int i = 1; i < 12; i++) {
            cv.put(FORM_ID, i);
            cv.put(FORM_NAME, "test form" + i);
            cv.put(FORM_TITLE, "some title" + i);
            db.insert(TABLE_FORM, null, cv);
        }
        db.execSQL(CREATE_TABLE_QUESTION);
        /*for (int i = 1; i < 15; i++) {
            cv.put(QUESTION_ID, i);
            cv.put(QUESTION_FORM_ID, i);
            cv.put(QUESTION_QUESTION, "test question in "+i);
            cv.put(QUESTION_TYPE, 0);
            cv.put(QUESTION_REQUIRED, 1);
            db.insert(TABLE_QUESTION,null,cv);
        }*/
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        onCreate(db);


    }
}

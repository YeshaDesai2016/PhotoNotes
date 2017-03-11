package edu.csulb.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by pinks on 3/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DataPics";
    private static final String TABLE_NAME = "Pictures";


    private static final String Col1 = "ID";
    private static final String Col2 = "Image";
    private static final String Col3 = "Caption";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Col1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + Col2 + " TEXT,"
                + Col3 + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public boolean AddData(Getter Dataset) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        ContentValues values = new ContentValues();
        values.put(Col1, Dataset.id);
        values.put(Col3, Dataset.caption);
        values.put(Col2, Dataset.path);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        if (result == -1)
            return false;
        else
            return true;

    }

    public ArrayList<Getter> Getcaption() {
        ArrayList<Getter> getters = new ArrayList<Getter>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " where 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Getter get = new Getter();
                get.id = cursor.getString(0);
                get.caption = cursor.getString(2);
                get.path = cursor.getString(1);
                getters.add(get);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return getters;
    }

    public Getter GetData(String id) {
        Getter get = new Getter();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + TABLE_NAME + " where `" + Col1 + "` = '" + id + "'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                get.id = cursor.getString(0);
                get.caption = cursor.getString(2);
                get.path = cursor.getString(1);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return get;
    }
}
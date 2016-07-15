package cn.crepusculo.subway_ticket_android.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "search_history.db";
    private static Integer version = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(40))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

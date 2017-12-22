package me.tbis.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zhongze Tang on 2017/9/26.
 *
 * a class extends SQLiteOpenHelper to create the default table in the database.
 *
 *
 */

class DBOpenHelper extends SQLiteOpenHelper  {

    DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "task.db", null, 1);
        //context, name of db file, cursor factory, version of database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table taskInfo (id integer primary key autoincrement, title text, description text) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

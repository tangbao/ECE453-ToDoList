package me.tbis.todolist;

import java.util.ArrayList;
import java.util.List;

import me.tbis.todolist.DBOpenHelper;
import me.tbis.todolist.TaskInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zhongze Tang on 2017/9/26.
 *
 * Add and Delete items of Tasks
 *
 */


class TaskMethods {
    private DBOpenHelper helper;

    TaskMethods(Context context) {
        helper = new DBOpenHelper(context,"task.db", null, 1);
    }

    //add a task
    long add(String title,String description){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        long result = db.insert("taskInfo", null, values);
        db.close();
        return result;
    }

    //delete a task
    boolean delete(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("delete from taskInfo where id=?", new Object[]{id});
        int result = db.delete("taskInfo", "id=?", new String[]{id + ""});
        db.close();
        if(result > 0){
            return true;
        }
        else {
            return false;
        }
    }

    //find all tasks
    List<TaskInfo> findAll(){
        List<TaskInfo> taskInfos = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("taskInfo", new String[]{"id","title","description"},
                null, null, null, null, null);
        //traversal the cursor, save all the tasks into taskInfos
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            TaskInfo taskInfo = new TaskInfo(id, title, description);
            taskInfos.add(taskInfo);
        }
        cursor.close();
        db.close();
        return taskInfos;
    }

}

package me.tbis.todolist;

/**
 * Created by Zhongze Tang on 2017/9/26.
 *
 * the class have to be serializable in order to be written to a text file
 *
 */

class TaskInfo implements java.io.Serializable{
    private int id;
    private String title;
    private String description;


    TaskInfo(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "id: " + id + "\r\ntitle: " + title + "\r\ndescription: " +  description +"\r\n";
    }

    int getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

}

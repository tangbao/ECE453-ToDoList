package me.tbis.todolist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import me.tbis.todolist.TaskMethods;
import me.tbis.todolist.TaskInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private ListView listView;
    private Button button;
    private EditText editDescription, editTitle;
    private TaskMethods taskMethods;
    private List<TaskInfo> taskInfos;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        editTitle =(EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);

        taskMethods = new TaskMethods(this);
        taskInfos = taskMethods.findAll(); //taskInfos stores all the tasks.

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                new  AlertDialog.Builder(MainActivity.this)
                    .setTitle("Alert")
                    .setMessage("Are you sure to delete it?" )
                    .setPositiveButton("yes" , new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteTask(position);
                            writeFile(taskInfos);
                            }
                    })
                    .setNegativeButton("no" , null)
                    .show();
                return true;
            }
        });

        //add a task and save to a txt file
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String newTitle = editTitle.getText().toString();
            String newDescription = editDescription.getText().toString();

            //if title or description is empty, ask the user to input it. and the empty one will get focus.
            if (newTitle.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter the title", Toast.LENGTH_LONG).show();
                setFocus(editTitle);
            } else if (newDescription.isEmpty()){
                Toast.makeText(MainActivity.this, "Please enter the description", Toast.LENGTH_LONG).show();
                setFocus(editDescription);
            } else { //title and description have s
                int id = (int)taskMethods.add(newTitle, newDescription); //add into database, and get the id
                taskInfos.add(new TaskInfo(id, newTitle, newDescription)); //add into adapter
                adapter.notifyDataSetChanged(); //update adapter view
                editTitle.setText("");
                editDescription.setText("");
                setFocus(editTitle);
                Toast.makeText(MainActivity.this, "Add successfully.", Toast.LENGTH_LONG).show();
                writeFile(taskInfos);
            }
            }
        });
    }

    //the function to set a editText to get focus
    private void setFocus(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();
    }

    //the function to delete task from database and the list
    private void deleteTask(int position){
        TaskInfo taskInfo = taskInfos.get(position); //get current taskinfo
        int taskId = taskInfo.getId(); //get id
        taskMethods.delete(taskId);  //delete it from database
        taskInfos.remove(position);  //delete it from list
        adapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Delete successfully", Toast.LENGTH_LONG).show();
    }

    //write file to /Android/data/me.tbis.todolist/files/todolist.txt of external storage
    private void writeFile(List<TaskInfo> taskInfos){
        try {
            //new a file with path and name
            File file = new File(getExternalFilesDir(null) , "todolist.txt");

            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            for (int i=0;i<taskInfos.size();i++){
                //write the taskInfo to the file separately
                bufferWriter.write(taskInfos.get(i).toString());
            }
            bufferWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //rewrite BaseAdapter
    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return taskInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //convert R.layout.to_do_lists into a view object
            View view = View.inflate(MainActivity.this, R.layout.to_do_lists, null);

            TextView title= (TextView)view.findViewById(R.id.title);
            TextView description = (TextView)view.findViewById(R.id.description);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            //get a taskInfo object from the position
            TaskInfo taskInfo = taskInfos.get(position);
            //get the title and description
            title.setText(taskInfo.getTitle());
            description.setText(taskInfo.getDescription());
            //bind the position to the checkbox, and set the listener
            checkBox.setTag(position);
            checkBox.setOnCheckedChangeListener(checkListener);
            return view;
        }
    }

    //listen the onCheckedChange event and delete corresponding task
    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            int position = (int) buttonView.getTag();
            deleteTask(position);
            writeFile(taskInfos);
        }
    };



}
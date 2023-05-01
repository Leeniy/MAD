package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private ListView toDoListView;
    private List<String> listData = new ArrayList<>(Arrays.asList("Rita", "Ron", "George"));
    private ArrayAdapter<String> toDoListViewAdapter;

    private FloatingActionButton fab;

    private ActivityResultLauncher<Intent> DetailviewLauncherForEdit = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result){
                    if (result.getResultCode() == DetailviewActivity.ITEM_EDITED){
                        String item = result.getData().getStringExtra("item");
                        onToDoItemEdited(item);
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED){
                        showMessage("edited cancelled!");
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> detailviewLauncherForCreate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result){
                    if (result.getResultCode() == DetailviewActivity.ITEM_CREATED){
                        String item = result.getData().getStringExtra("item");
                        onNewToDoItemCreated(item);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overview);
        this.toDoListView = findViewById(R.id.toDoListView);
        this.fab = findViewById(R.id.fab);

        this.fab.setOnClickListener(view -> {
            onCreateNewToDo();
        });

//        Arrays.asList("Rita", "Ron", "George")
//                .forEach(listitem-> {
//                   TextView itemView = (TextView) getLayoutInflater().inflate(R.layout.activity_overview_listitem, null);
//                   itemView.setText(listitem);
//                   itemView.setOnClickListener(view -> {
//                       onListItemSelected(String.valueOf(((TextView)view).getText()));
//                   });
//                   this.toDoListView.addView(itemView);
//                });

        // initialise the List
        this.toDoListViewAdapter = new ArrayAdapter<>(this, R.layout.activity_overview_listitem, listData);
        this.toDoListView.setAdapter(this.toDoListViewAdapter);
        this.toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionOfSelectedElement, long l) {
                String selectedElement = toDoListViewAdapter.getItem(positionOfSelectedElement);
                onListItemSelected(selectedElement);
            }
        });
    }

    public void onListItemSelected(String listitem){
//        DetailviewActivity detailviewActivity = new DetailviewActivity();
//        detailviewActivity.onCreate(null);
        Intent callDetailviewIntent = new Intent(this, DetailviewActivity.class);
        callDetailviewIntent.putExtra("item", listitem);
//        startActivity(callDetailviewIntent);
        DetailviewLauncherForEdit.launch(callDetailviewIntent);
    }

    public void onCreateNewToDo(){
        detailviewLauncherForCreate.launch(new Intent(this, DetailviewActivity.class));
    }

    public void onNewToDoItemCreated(String item){
        //showMessage("created: " + item);
        this.toDoListViewAdapter.add(item);
    }

    public void onToDoItemEdited(String item){
        showMessage("edited: " + item);
    }

    public void showMessage(String msg){
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }
}

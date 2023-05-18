package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.SimpleToDoCRUPOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private ListView toDoListView;
    private List<ToDoItem> listData = new ArrayList<>();
    private ArrayAdapter<ToDoItem> toDoListViewAdapter;

    private FloatingActionButton fab;

    private IToDoItemCRUDOperations crudOperations;

    private ProgressBar progressBar;

    private ActivityResultLauncher<Intent> DetailviewLauncherForEdit = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == DetailviewActivity.ITEM_EDITED) {
                        ToDoItem item = (ToDoItem) result.getData().getSerializableExtra(DetailviewActivity.ARG_ITEM);
                        onEditedToDoItemReceived(item);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        showMessage("edited cancelled!");
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> detailviewLauncherForCreate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == DetailviewActivity.ITEM_CREATED) {
                        ToDoItem item = (ToDoItem) result.getData().getSerializableExtra(DetailviewActivity.ARG_ITEM);
                        onNewToDoItemReceived(item);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        this.crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();
        //this.crudOperations = new SimpleToDoCRUPOperationsImpl();

        this.toDoListView = findViewById(R.id.toDoListView);
        this.fab = findViewById(R.id.fab);

        this.progressBar = findViewById(R.id.progressBar);

        this.fab.setOnClickListener(view -> {
            onCreateNewToDo();
        });

        // prepare the List view
        this.toDoListViewAdapter = new ArrayAdapter<>(this, R.layout.activity_overview_listitem, listData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewGroup itemView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_overview_listitem, null);
                TextView itemNameView = itemView.findViewById(R.id.todoName);
                ToDoItem item = getItem(position);
                itemNameView.setText(item.getName());
                return itemView;
            }
        };
        this.toDoListView.setAdapter(this.toDoListViewAdapter);
        this.toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionOfSelectedElement, long l) {
                ToDoItem selectedElement = toDoListViewAdapter.getItem(positionOfSelectedElement);
                onListItemSelected(selectedElement);
            }
        });

        // initialise the list
        //1. prepare the view for the data access that will take place
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            //2. run the data access on a separate thread
            List<ToDoItem> items = crudOperations.readAllToDoItems();
            Log.i(OverviewActivity.class.getSimpleName(), "got items: " + items);
            //3. get back to the ui thread in order to update the ui
            runOnUiThread(() -> {
                toDoListViewAdapter.addAll(items);
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    public void onListItemSelected(ToDoItem listitem) {
//        DetailviewActivity detailviewActivity = new DetailviewActivity();
//        detailviewActivity.onCreate(null);
        Intent callDetailviewIntent = new Intent(this, DetailviewActivity.class);
        callDetailviewIntent.putExtra(DetailviewActivity.ARG_ITEM, listitem);
//        startActivity(callDetailviewIntent);
        DetailviewLauncherForEdit.launch(callDetailviewIntent);
    }

    public void onCreateNewToDo() {
        detailviewLauncherForCreate.launch(new Intent(this, DetailviewActivity.class));
    }

    public void onNewToDoItemReceived(ToDoItem item) {
        // 1. run the crup operation on a seperate thread
        new Thread(() -> {
            ToDoItem createditem = this.crudOperations.createToDoItem(item);
            // 2. update the view
            this.runOnUiThread(() -> {
                this.toDoListViewAdapter.add(createditem);
            });
        }).start();
    }

    public void onEditedToDoItemReceived(ToDoItem item) {
        new Thread(() -> {
            this.crudOperations.updateToDoItem(item);
            runOnUiThread(() -> {
                Log.i(OverviewActivity.class.getSimpleName(), "item id: " + item.getId() + "," + item.getName());
                int posOfToDoItemInList = toDoListViewAdapter.getPosition(item);
                ToDoItem itemInList = toDoListViewAdapter.getItem(posOfToDoItemInList);
                itemInList.setName(item.getName());
                itemInList.setDescription(item.getDescription());
                itemInList.setChecked(item.isChecked());
                toDoListViewAdapter.notifyDataSetChanged();
            });
        }).start();

    }

    public void showMessage(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }
}

package org.dieschnittstelle.mobile.android.skeleton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityOverviewListitemBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.OverviewViewModelImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private ListView toDoListView;
    private List<ToDoItem> listData = new ArrayList<>();
    private ArrayAdapter<ToDoItem> toDoListViewAdapter;

    OverviewViewModelImpl overviewViewModel;

    private FloatingActionButton fab;

    private IToDoItemCRUDOperations crudOperations;

    private ProgressBar progressBar;

    private MADAsyncOperationRunner operationRunner;

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

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (((ToDoItemApplication) getApplication()).isOffLineMode()){
            startActivity(new Intent(this, DetailviewActivity.class));
        }

        setContentView(R.layout.activity_overview);

        this.crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();

        this.toDoListView = findViewById(R.id.toDoListView);
        this.fab = findViewById(R.id.fab);

        this.progressBar = findViewById(R.id.progressBar);

        this.operationRunner = new MADAsyncOperationRunner(this, this.progressBar);

        this.fab.setOnClickListener(view -> {
            onCreateNewToDo();
        });

        // prepare the List view
        this.toDoListViewAdapter = new ArrayAdapter<>(this, R.layout.activity_overview_listitem, listData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View existingListToDoItemView, @NonNull ViewGroup parent) {
                Log.i(OverviewActivity.class.getSimpleName(), "getView() has been called for position " + position + ", convertView is null");
                ActivityOverviewListitemBinding itemBinding = null;
                if (existingListToDoItemView != null){
                    Log.i(OverviewActivity.class.getSimpleName(), "existingListToDoItemView is not null. Read out binding via getTag().");
                    itemBinding = (ActivityOverviewListitemBinding) existingListToDoItemView.getTag();
                }
                else {
                    Log.i(OverviewActivity.class.getSimpleName(), "existingListToDoItemView is null. Create new binding object and set it as tag.");
                    itemBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_overview_listitem, null, false);
                    itemBinding.getRoot().setTag(itemBinding);
                }
                ToDoItem item = super.getItem(position);
                itemBinding.setItem(item);
                itemBinding.setController(OverviewActivity.this);
                itemBinding.setItem(item);
                return itemBinding.getRoot();
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

        this.overviewViewModel = new ViewModelProvider(this).get(OverviewViewModelImpl.class);

        if (overviewViewModel.getToDoItem() == null){
            operationRunner.run(
                    // Supplier (= the operation)
                    () -> crudOperations.readAllToDoItems(),
                    // Consumer (= the reaction to the operation result)
                    toDoItems -> {
                        toDoListViewAdapter.addAll(toDoItems);
                        overviewViewModel.setItems(toDoItems);
                        sortToDoItems();
                    });
        }
        else {
            toDoListViewAdapter.addAll(overviewViewModel.getToDoItem());
        }
    }

    public void onListItemSelected(ToDoItem listitem) {
        Intent callDetailviewIntent = new Intent(this, DetailviewActivity.class);
        callDetailviewIntent.putExtra(DetailviewActivity.ARG_ITEM, listitem);
        DetailviewLauncherForEdit.launch(callDetailviewIntent);
    }

    public void onCreateNewToDo() {
        detailviewLauncherForCreate.launch(new Intent(this, DetailviewActivity.class));
        sortToDoItems();
    }

    public void onNewToDoItemReceived(ToDoItem item) {
        operationRunner.run(
                () -> this.crudOperations.createToDoItem(item),
                createdToDoItem -> {
                    this.toDoListViewAdapter.add(createdToDoItem);
                    sortToDoItems();
                }
        );
    }

    public void onEditedToDoItemReceived(ToDoItem item) {
        this.operationRunner.run(
                () -> this.crudOperations.updateToDoItem(item),
                updated -> {
                    Log.i(OverviewActivity.class.getSimpleName(), "item id: " + item.getId() + "," + item.getName());
                    int posOfToDoItemInList = toDoListViewAdapter.getPosition(item);
                    ToDoItem itemInList = toDoListViewAdapter.getItem(posOfToDoItemInList);
                    itemInList.setName(item.getName());
                    itemInList.setDescription(item.getDescription());
                    itemInList.setChecked(item.isChecked());
                    itemInList.setFavourite(item.isFavourite());
                    itemInList.setExpiry(item.getExpiry());
                    sortToDoItems();
                }
        );
    }

    public void showMessage(String msg) {
       Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sortList){
            overviewViewModel.switchSortMode();
            sortToDoItems();
            showMessage("Sorting...");
            return true;
        }
        else if (item.getItemId() == R.id.deleteAllItemsLocally){
            showMessage("DELETE ALL ITEMS LOCALLY");
            showMessage("Delete all...");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void sortToDoItems(){
        this.listData.sort(this.overviewViewModel.getCurrentSortMode());
        this.toDoListViewAdapter.notifyDataSetChanged();
    }

    public void onCheckedChangeListView(ToDoItem item){
        this.operationRunner.run(
                () -> crudOperations.updateToDoItem(item),
                updateditem -> {
                    this.sortToDoItems();
                    showMessage("Checked change for: " + item.getName());
                }
        );
    }
}

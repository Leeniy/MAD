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
import android.widget.TextView;

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

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
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

    private FloatingActionButton fab;

    private IToDoItemCRUDOperations crudOperations;

    private ProgressBar progressBar;

    private MADAsyncOperationRunner operationRunner;

    public static final Comparator<ToDoItem> NAME_COMPARATOR = Comparator.comparing(ToDoItem::getName);
    public static final Comparator<ToDoItem> CHECKED_AND_NAME_COMPARATOR = Comparator.comparing(ToDoItem::isChecked).thenComparing(ToDoItem::getName);

    private Comparator<ToDoItem> currentComparator = NAME_COMPARATOR;

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
        setContentView(R.layout.activity_overview);

        this.crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();
        //this.crudOperations = new SimpleToDoCRUPOperationsImpl();

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

                ToDoItem item = super.getItem(position);
                ActivityOverviewListitemBinding itemBinding = existingListToDoItemView != null
                        ? (ActivityOverviewListitemBinding) existingListToDoItemView.getTag()
                        : DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_overview_listitem, null, false);
                itemBinding.setItem(item);
                itemBinding.setController(OverviewActivity.this);

                View itemView = itemBinding.getRoot();
                itemView.setTag(itemBinding);
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

        OverviewViewModelImpl overviewViewModel = new ViewModelProvider(this).get(OverviewViewModelImpl.class);

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
                createdToDoItem -> this.toDoListViewAdapter.add(createdToDoItem)
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
            this.currentComparator = CHECKED_AND_NAME_COMPARATOR;
            sortToDoItems();
            return true;
        }
        else if (item.getItemId() == R.id.deleteAllItemsLocally){
            showMessage("DELETE ALL ITEMS LOCALLY");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void sortToDoItems(){
        this.listData.sort(this.currentComparator);
        this.toDoListViewAdapter.notifyDataSetChanged();
    }

    public void onCheckedChangeListView(ToDoItem item){
        this.operationRunner.run(
                () -> crudOperations.updateToDoItem(item),
                updateditem -> {
                    this.sortToDoItems();
                }
        );

    }
}

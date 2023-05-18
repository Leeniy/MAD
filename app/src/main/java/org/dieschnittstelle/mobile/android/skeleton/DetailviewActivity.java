package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 10;

    public static final int ITEM_EDITED = 20;

    private TextView itemToDoView;
    private TextView itemDescriptionView;

    private FloatingActionButton fab;

    private ToDoItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //A.1 determine the view
        setContentView(R.layout.activity_detailview);

        //A.2 prepare initialising the view by reading out its element
        itemToDoView = findViewById(R.id.ToDoName);
        itemDescriptionView = findViewById(R.id.todoDescription);
        fab = findViewById(R.id.fab);

        //B bind data to the view element
        this.item = (ToDoItem) getIntent().getSerializableExtra(ARG_ITEM);
        if (item != null) {
            itemToDoView.setText(item.getName());
            itemDescriptionView.setText(item.getDescription());
        } else {
            this.item = new ToDoItem();
        }


        //C prepare the view for user interaction
        fab.setOnClickListener(view -> {
            onItemSaved();
        });
    }

    protected void onItemSaved() {
        String itemName = itemToDoView.getText().toString();
        String itemDescription = itemDescriptionView.getText().toString();
        this.item.setName(itemName);
        this.item.setDescription(itemDescription);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM, this.item);
        setResult(this.item.getId() == 0L ? ITEM_CREATED : ITEM_EDITED, returnIntent);
        finish();
    }
}
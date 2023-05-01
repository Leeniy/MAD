package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailviewActivity extends AppCompatActivity {

    public static final int ITEM_CREATED = 10;

    public static final int ITEM_EDITED = 20;

    private TextView itemToDoView;
    private TextView itemDescriptionView;

    private FloatingActionButton fab;

    private boolean createMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //A.1 determine the view
        setContentView(R.layout.activity_detailview);

        //A.2 prepare initialising the view by reading out its elemnt
        itemToDoView = findViewById(R.id.ToDoName);
        itemDescriptionView = findViewById(R.id.todoDescription);
        fab = findViewById(R.id.fab);

        //B bind data to the view element
        String item = getIntent().getStringExtra("item");
        if (item != null){
            itemToDoView.setText(item);
        }
        else {
            this.createMode = true;
        }


        //C prepare the view for user interaction
        fab.setOnClickListener(view -> {
            onItemSaved();
        });
    }

    protected void onItemSaved(){
        String item = itemToDoView.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("item", item);
        setResult(createMode ? ITEM_CREATED : ITEM_EDITED, returnIntent);
        finish();
    }
}

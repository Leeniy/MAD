package org.dieschnittstelle.mobile.android.skeleton;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailviewActivity extends AppCompatActivity {

    private TextView itemToDoView;
    private TextView itemDescriptionView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //A determine the view
        setContentView(R.layout.activity_detailview);

        //B prepare initialising the view by reading out its elemnt
        itemToDoView = findViewById(R.id.itemToDo);
        itemDescriptionView = findViewById(R.id.todoDescription);

        //C bind data to the view element
        String item = getIntent().getStringExtra("item");

        itemToDoView.setText(item);
    }
}

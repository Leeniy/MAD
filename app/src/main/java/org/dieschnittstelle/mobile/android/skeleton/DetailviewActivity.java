package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailviewViewModelImpl;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 10;

    public static final int ITEM_EDITED = 20;

    private ActivityDetailviewBinding binding;

    private DetailviewViewModelImpl viewmodel;

    public DetailviewActivity(){
        Log.i(DetailviewActivity.class.getSimpleName(), "consructor invoked");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(DetailviewActivity.class.getSimpleName(), "onCreate invoked");

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);

        this.viewmodel = new ViewModelProvider(this).get(DetailviewViewModelImpl.class);

        if (this.viewmodel.getItem() == null) {
            ToDoItem item = (ToDoItem) getIntent().getSerializableExtra(ARG_ITEM);
            if (item == null){
                this.viewmodel.setItem(new ToDoItem());
            }
            else {
                this.viewmodel.setItem(item);
            }
        }
        else {
            Log.i(DetailviewActivity.class.getSimpleName(), "use item from viewmodel: " + this.viewmodel.getItem());
        }

        this.viewmodel.getSavedOcurred().observe(this, occurred -> {
            onItemSaved();
        });

        this.binding.setViewmodel(this.viewmodel);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onItemSaved() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM, this.viewmodel.getItem());
        setResult(this.viewmodel.getItem().getId() == 0L ? ITEM_CREATED : ITEM_EDITED, returnIntent);
        finish();
    }

    public void onItemDelete(){

    }

}
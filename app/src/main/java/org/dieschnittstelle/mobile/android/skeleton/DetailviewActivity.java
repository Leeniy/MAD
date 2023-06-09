package org.dieschnittstelle.mobile.android.skeleton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import org.checkerframework.checker.units.qual.C;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailviewViewModelImpl;

import java.util.Calendar;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 10;

    public static final int ITEM_EDITED = 20;

    private ActivityDetailviewBinding binding;

    private DetailviewViewModelImpl viewmodel;

    int cyear, cmonth, cday, chour, cminutes;
    String time;
    String date;

    String expiry;

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

//        this.binding.btnDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar calender = Calendar.getInstance();
//                cyear = calender.get(Calendar.YEAR);
//                cmonth = calender.get(Calendar.MONTH);
//                cday = calender.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog =
//                        new DatePickerDialog(DetailviewActivity.this, new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                binding.showDate.setText(dayOfMonth+"."+(month+1)+"."+year);
//                            }
//                        }, cyear, cmonth, cday);
//                datePickerDialog.getDatePicker().setMinDate(calender.getTimeInMillis()-1000);
//                datePickerDialog.show();
//                date = String.valueOf(cday+cmonth+cyear);
//            }
//        });
//
//        this.binding.btnTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar calendar = Calendar.getInstance();
//                chour = calendar.get(Calendar.HOUR);
//                cminutes = calendar.get(Calendar.MINUTE);
//                TimePickerDialog timePickerDialog =
//                        new TimePickerDialog(DetailviewActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                binding.showTime.setText(hourOfDay+":"+minute);
//                            }
//                        }, chour, cminutes, true);
//                timePickerDialog.show();
//                time = String.valueOf(chour+cminutes);
//            }
//        });
//
//        expiry = date + time;
//        this.viewmodel.getItem().setExpiry(expiry);
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
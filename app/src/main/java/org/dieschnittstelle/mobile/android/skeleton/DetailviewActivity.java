package org.dieschnittstelle.mobile.android.skeleton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewListcontactBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailviewViewModelImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 10;

    public static final int ITEM_EDITED = 20;

    private static String LOGGER = DetailviewActivity.class.getSimpleName();

    private ActivityDetailviewBinding binding;

    DetailviewViewModelImpl detailViewmodel;

    private String contactName;

    private ActivityResultLauncher<Intent> showContectsLuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    onContactSelected(result.getData());
                }
            }
    );

    IToDoItemCRUDOperations crudOperations;

    int cyear, cmonth, cday, chour, cminutes;

    String time;
    String date;

    String vv;
    Date df;

    Long expiry;

    public DetailviewActivity(){
        Log.i(DetailviewActivity.class.getSimpleName(), "consructor invoked");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(DetailviewActivity.class.getSimpleName(), "onCreate invoked");
        setContentView(R.layout.activity_detailview);
        this.detailViewmodel = new ViewModelProvider(this).get(DetailviewViewModelImpl.class);

        crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);

        this.binding.setLifecycleOwner(this);

        if (this.detailViewmodel.getItem() == null) {
            ToDoItem item = (ToDoItem) getIntent().getSerializableExtra(ARG_ITEM);
            if (item == null){
                this.detailViewmodel.setItem(new ToDoItem());
            }
            else {
                this.detailViewmodel.setItem(item);
            }
        }
        else {
            Log.i(DetailviewActivity.class.getSimpleName(), "use item from viewmodel: " + this.detailViewmodel.getItem());
        }

        this.detailViewmodel.getSavedOcurred().observe(this, occurred -> {
            onItemSaved();
        });

        this.binding.setViewmodel(this.detailViewmodel);

        this.expiry = this.detailViewmodel.getItem().getExpiry();

        if (expiry != null){
            df = new java.util.Date((long)expiry);
        } else {
            expiry = System.currentTimeMillis();
            df = new java.util.Date((long)expiry);
        }

        vv = new SimpleDateFormat("dd.MM.yyyy hh:mm").format(df);

        date = vv.substring(0,10);
        binding.showDate.setText(date);
        time = vv.substring(11,16);
        binding.showTime.setText(time);

        this.binding.btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calender = Calendar.getInstance();
                cyear = Integer.parseInt(vv.substring(6,10));
                cmonth = Integer.parseInt(vv.substring(3,5))-1;
                cday = Integer.parseInt(vv.substring(0,2));
                Log.i(OverviewActivity.class.getSimpleName(), "year: " + cyear + " month: " + cmonth + " day: " + cday);
                calender.set(cyear, cmonth, cday);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(DetailviewActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date = String.format("%02d.%02d.%04d", dayOfMonth, (month + 1), year);
                                binding.showDate.setText(date);
                                setNewDateTime();
                            }
                        }, cyear, cmonth, cday);
                datePickerDialog.show();
            }
        });

        this.binding.btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                chour = Integer.parseInt(vv.substring(11,13));
                cminutes = Integer.parseInt(vv.substring(14,16));
                calendar.set(chour, cminutes);
                Log.i(OverviewActivity.class.getSimpleName(), "hour: " + chour + " minutes: " + cminutes);

                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(DetailviewActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time = String.format("%02d:%02d", hourOfDay, minute);
                                binding.showTime.setText(time);
                                setNewDateTime();
                            }
                        }, chour, cminutes, true);
                timePickerDialog.show();
                calendar.set(Calendar.HOUR, chour);
                calendar.set(Calendar.MINUTE, cminutes);
            }
        });

        this.binding.fabDelete.setOnClickListener(view -> {
            showAlertDialog();
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETE");
        builder.setMessage("Are you sure?");

        // add the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                crudOperations.deleteToDoItem(detailViewmodel.getItem().getId());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onItemSaved() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM, this.detailViewmodel.getItem());
        setResult(this.detailViewmodel.getItem().getId() == 0L ? ITEM_CREATED : ITEM_EDITED, returnIntent);
        setNewDateTime();
        finish();
    }

    public void setNewDateTime(){
        vv = date + " " +time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        try {
            df = simpleDateFormat.parse(vv);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Log.i(DetailviewActivity.class.getSimpleName(), "date check " + df);
        expiry = df.getTime();
        detailViewmodel.getItem().setExpiry(expiry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.selectContact) {
            selectContect();
            return true;
        } else if (item.getItemId() == R.id.sendSMS){
            sendSMS();
            return true;
        } else if (item.getItemId() == R.id.sendMail){
            sendMail();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sendSMS(){
        Uri receiverPhoneUri = Uri.parse("smsto:" + "0123456789");
        Intent sendSMSIntent = new Intent( Intent.ACTION_SENDTO, receiverPhoneUri);
        sendSMSIntent.putExtra("sms_body", this.detailViewmodel.getItem().getName() + (this.detailViewmodel.getItem().getDescription() != null ? ": " + this.detailViewmodel.getItem().getDescription() : ""));
        startActivity(sendSMSIntent);
    }

    private void sendMail(){
        Uri receiverMailUri = Uri.parse("mailto:" + "test@test.de");
        Intent sendMailIntent = new Intent( Intent.ACTION_SENDTO, receiverMailUri);
        sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, this.detailViewmodel.getItem().getName());
        sendMailIntent.putExtra (Intent.EXTRA_TEXT, this.detailViewmodel.getItem().getDescription() != null ? ": " + this.detailViewmodel.getItem().getDescription() : "");
        startActivity(sendMailIntent);
    }

    private void selectContect() {
        this.showContectsLuncher.launch(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI));
    }
    @SuppressLint("Range")
    private void onContactSelected(Intent resultData) {
        Log.i(LOGGER, "onCOntectSelected(): " + resultData);
        Uri selectedContactUri = resultData.getData();
        Log.i(LOGGER, "onContactSelected(): selected ContactUri: " + selectedContactUri);

        Cursor cursor = getContentResolver().query(selectedContactUri, null, null, null, null);

        if (cursor.moveToFirst()){
             String contectName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
             this.contactName = contectName;
             Log.i(LOGGER, "contactName: " + contectName);
             long intentContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Log.i(LOGGER, "internalContactId: " + intentContactId);
             this.detailViewmodel.getItem().getContactId().add(String.valueOf(intentContactId));
             showContactDetailsForInternalId(intentContactId);
        }
    }

    public String getContactName() {
        return contactName;
    }

    private long lastSelectedInternalCOntactId = -1;

    @SuppressLint("Range")
    public void showContactDetailsForInternalId (long internalContectId){
        lastSelectedInternalCOntactId = internalContectId;
        if (hasContactPermission()) {
            Log.i(LOGGER, "showContactDetailsForInternalId(): " + internalContectId);
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(internalContectId)}, null);
            if (cursor.moveToFirst()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i(LOGGER, "contactName for id: " + contactName);
            }
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?", new String[]{String.valueOf(internalContectId)}, null);
            while (cursor.moveToNext()){
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int phoneNumberType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                boolean isMobile = (phoneNumberType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                Log.i(LOGGER, "phoneNumber: "+ phoneNumber);
                Log.i(LOGGER, "isMobile: " + isMobile);
            }

            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?", new String[]{String.valueOf(internalContectId)}, null);
            while (cursor.moveToNext()){
                String emailaddr = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                Log.i(LOGGER, "isMobile: " + emailaddr);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(LOGGER, "onRequestPermsissionResult(): " + Arrays.asList(permissions)+ ", "+ Arrays.asList(grantResults));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (lastSelectedInternalCOntactId != -1){
            showContactDetailsForInternalId(lastSelectedInternalCOntactId);
        }
    }

    public boolean hasContactPermission() {
        int hasReadContactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        return false;
    }

}
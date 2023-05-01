package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.Arrays;

public class OverviewActivity extends AppCompatActivity {

    private ViewGroup listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overview);
        this.listView = findViewById(R.id.listView);

        Arrays.asList("Rita", "Ron", "George")
                .forEach(listitem-> {
                   TextView itemView = (TextView) getLayoutInflater().inflate(R.layout.activity_overview_listitem, null);
                   itemView.setText(listitem);
                   itemView.setOnClickListener(view -> {
                       onListItemSelected(String.valueOf(((TextView)view).getText()));
                   });
                   this.listView.addView(itemView);
                });

    }

    protected void onListItemSelected(String listitem){
//        DetailviewActivity detailviewActivity = new DetailviewActivity();
//        detailviewActivity.onCreate(null);
        Intent callDetailviewIntent = new Intent(this, DetailviewActivity.class);
        callDetailviewIntent.putExtra("item", listitem);
        startActivity(callDetailviewIntent);
    }

    protected void showMessage(String msg){
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }
}

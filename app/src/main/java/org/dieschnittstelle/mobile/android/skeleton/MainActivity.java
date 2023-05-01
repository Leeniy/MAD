package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Auswahl der darzustellenden Ansicht
        setContentView(R.layout.activity_main);
        TextView welcomeText = findViewById(R.id.welcomeText);

        // 2) Befuellung der Ansicht mit Inhalten
        welcomeText.setText(R.string.welcome_message_alternative);

        // 3) Vorbereitung der Ansicht fuer Nutzerinteraktion
        welcomeText.setOnClickListener(view
                -> Toast.makeText(this, R.string.welcome_message_reply_roast, Toast.LENGTH_SHORT).show());
    }
}

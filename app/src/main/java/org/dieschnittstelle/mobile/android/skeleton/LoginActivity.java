package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private MutableLiveData<String> errorStatusPW = new MutableLiveData<>();

    private User user = new User();

    String mailT;
    String pwT;

    private IToDoItemCRUDOperations crudOperations;
    private boolean status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.binding.setLifecycleOwner(this);

        Intent intent = new Intent(this, OverviewActivity.class);

        binding.loginButton.setEnabled(false);

        this.binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mailV = findViewById(R.id.login_email);
                mailT = mailV.getText().toString();
                EditText pwV = findViewById(R.id.login_pw);
                pwT = pwV.getText().toString();

                try {
                    Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mailT + " pw: " + pwT);
                    User user = new User(mailT, pwT);

                    Log.i(OverviewActivity.class.getSimpleName(), "user: " + user);
                    if (mailT.equals("s@bht.de")  || pwT.equals("000000")){
                        startActivity(intent);
                        Log.i(OverviewActivity.class.getSimpleName(), "success");
                    } else {
                        //setContentView(R.layout.activity_login);
                        Log.i(OverviewActivity.class.getSimpleName(), "failed");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    public boolean checkFieldInputCompleted(int actionId) {
        EditText mailV = findViewById(R.id.login_email);
        mailT = mailV.getText().toString();
        Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mailT );
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( mailT.contains("@") || mailT.contains(".")){
                errorStatus.setValue("No Email");
            }
        }
        return false;
    }

    public boolean checkPWFieldInputCompleted(int actionId) {
        EditText pwV = findViewById(R.id.login_email);
        pwT = pwV.getText().toString();
        Log.i(OverviewActivity.class.getSimpleName(), " pw: " + pwT);
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( pwT == null || pwT.length() <=6){
                errorStatusPW.setValue("To Short!");
            }
        }
        return false;
    }

    public boolean onNameFieldInputChanged() {
        if (errorStatus.getValue() != null && errorStatus.getValue().length() > 0) {
            errorStatus.setValue(null);
        }
        return false;
    }

    public boolean onPWFieldInputChanged() {
        if (errorStatusPW.getValue() != null && errorStatusPW.getValue().length() > 0) {
            errorStatusPW.setValue(null);
        }
        return false;
    }

    public MutableLiveData<String> getErrorStatus() {
        return this.errorStatus;
    }

    public MutableLiveData<String> getErrorStatusPW() {
        return this.errorStatusPW;
    }

}

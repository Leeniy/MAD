package org.dieschnittstelle.mobile.android.skeleton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.User;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.LoginViewModelImpl;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private LoginViewModelImpl viewmodel;


    private IToDoItemCRUDOperations crudOperations;

    private ProgressBar progressBar;

    private MADAsyncOperationRunner operationRunner;

    TextInputEditText mail;

    TextInputEditText pw;

    private Boolean status = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        this.binding.setLifecycleOwner(this);

        this.crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();

        Intent intent = new Intent(this, OverviewActivity.class);

        this.progressBar = findViewById(R.id.progressBar);

        this.operationRunner = new MADAsyncOperationRunner(this, this.progressBar);

        this.viewmodel = new ViewModelProvider(this).get(LoginViewModelImpl.class);
        //binding.loginButton.setEnabled(false);

        this.mail = findViewById(R.id.login_email);
        this.pw = findViewById(R.id.login_pw);

        this.binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mail.getText().toString() + " pw: " + pw.getText().toString());
                    loginStatus(new User(mail.getText().toString(), pw.getText().toString()));

                    if (status){
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

    public void loginStatus(User user){
        operationRunner.run(
                () -> this.crudOperations.login(user),
                login -> {
                    this.status = true;
                }
        );
        Log.i(OverviewActivity.class.getSimpleName(), "status: " + this.status);
    }

}

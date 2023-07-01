package org.dieschnittstelle.mobile.android.skeleton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.generated.callback.OnEditorActionListener;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.User;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.ILoginViewModel;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.LoginViewModelImpl;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private LoginViewModelImpl loginViewmodel;

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();


    private IToDoItemCRUDOperations crudOperations;

    private ProgressBar progressBar;

    private MADAsyncOperationRunner operationRunner;

    private Intent intent;

    TextInputEditText mail;

    TextInputEditText pw;

    ILoginViewModel loginViewModel;

    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (((ToDoItemApplication) getApplication()).isOffLineMode()){
            startActivity(new Intent(this, OverviewActivity.class));
        }

        setContentView(R.layout.activity_login);

        this.loginViewmodel = new ViewModelProvider(this).get(LoginViewModelImpl.class);
        crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();

        //this.binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        this.binding.setLifecycleOwner(this);

        this.binding.setViemodel(this.loginViewmodel);


        intent = new Intent(this, OverviewActivity.class);

        this.progressBar = findViewById(R.id.progressBar);

        this.operationRunner = new MADAsyncOperationRunner(this, this.progressBar);

        this.mail = findViewById(R.id.login_email);
        this.pw = findViewById(R.id.login_pw);

        this.binding.loginButton.setEnabled(loginViewmodel.getLoginstatus());

        this.binding.loginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.loginButton.setEnabled(loginViewmodel.getLoginstatus());
                findViewById(R.id.errorFaildLogin).setVisibility(View.INVISIBLE);
            }
        });

        this.binding.loginPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.loginButton.setEnabled(loginViewmodel.getLoginstatus());
                findViewById(R.id.errorFaildLogin).setVisibility(View.INVISIBLE);
            }
        });

        this.binding.loginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginButton.setEnabled(loginViewmodel.getLoginstatus());
                findViewById(R.id.errorFaildLogin).setVisibility(View.INVISIBLE);
            }
        });

        this.binding.loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mail.getText().toString() + " pw: " + pw.getText().toString());
                    //loginStatus(new User(mail.getText().toString(), pw.getText().toString()));
                    User user = new User(mail.getText().toString(), pw.getText().toString());
                    Log.i(OverviewActivity.class.getSimpleName(), "user: " + user);
                    /*ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Loading..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);*/
                    operationRunner.run(
                            () -> crudOperations.login(user),
                            login -> {
                                if (login){
                                    startActivity(intent);
                                } else {
                                    Log.i(OverviewActivity.class.getSimpleName(), "status: " + login);
                                    findViewById(R.id.errorFaildLogin).setVisibility(View.VISIBLE);
                                }
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e)
                                {
                                    throw new RuntimeException(e);
                                }
                                //progressDialog.cancel();
                            });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }



}

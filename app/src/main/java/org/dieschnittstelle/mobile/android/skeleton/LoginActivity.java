package org.dieschnittstelle.mobile.android.skeleton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.PrimaryKey;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;
import org.dieschnittstelle.mobile.android.skeleton.model.User;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String mail;
    private String pw;

    private User user = new User();

    private boolean status;

    private IToDoItemCRUDOperations crudOperations;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.crudOperations = ((ToDoItemApplication) getApplication()).getCRUDOperations();


        this.binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = String.valueOf(binding.loginEmail.getText());
                pw = String.valueOf(binding.loginPw.getText());
                user = new User(mail, pw);
                Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mail + " pw: " + pw);

                status = crudOperations.login(user);

                if (status){
                    setContentView(R.layout.activity_overview);
                } else {
                    setContentView(R.layout.activity_login);
                }
            }
        });
    }
}

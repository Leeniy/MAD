package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.User;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.LoginViewModelImpl;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private LoginViewModelImpl viewmodel;

    private User user = new User();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.binding.setLifecycleOwner(this);

        Intent intent = new Intent(this, OverviewActivity.class);

        this.viewmodel = new ViewModelProvider(this).get(LoginViewModelImpl.class);
        //binding.loginButton.setEnabled(false);

        this.binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(OverviewActivity.class.getSimpleName(), "mail: " + viewmodel.getUser().getMail() + " pw: " + viewmodel.getUser().getPw());
                    User user = new User(viewmodel.getUser().getMail(), viewmodel.getUser().getPw());

                    Log.i(OverviewActivity.class.getSimpleName(), "user: " + user);
                    if (viewmodel.getUser().getMail().equals("s@bht.de")  || viewmodel.getUser().getMail().equals("000000")){
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
}

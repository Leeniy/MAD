package org.dieschnittstelle.mobile.android.skeleton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.PrimaryKey;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;
import org.dieschnittstelle.mobile.android.skeleton.model.User;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.LoginViewModelImpl;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private User user = new User();

    private LoginViewModelImpl viewModel;

    String mailT;
    String pwT;

    private boolean status;

    private IToDoItemCRUDOperations crudOperations;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.viewModel = new ViewModelProvider(this).get(LoginViewModelImpl.class);

        this.binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mailT + " pw: " + pwT);
                    if (viewModel.getUser().getMail() == "s@bht.de" || viewModel.getUser().getPw() == "000000"){
                        setContentView(R.layout.activity_overview);
                    } else {
                        setContentView(R.layout.activity_login);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }

}

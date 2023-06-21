package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.DetailviewActivity;
import org.dieschnittstelle.mobile.android.skeleton.OverviewActivity;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.model.User;

public class LoginViewModelImpl extends ViewModel implements ILoginViewModel {

    private User user;

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private MutableLiveData<String> errorStatusPW = new MutableLiveData<>();

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public boolean checkFieldInputCompleted(int actionId) {
        Log.i(OverviewActivity.class.getSimpleName(), "mail: " + user.getMail() );
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( user.getMail().contains("@") || user.getMail().contains(".")){
                errorStatus.setValue("No Email");
            }
        }
        return false;
    }

    @Override
    public boolean onNameFieldInputChanged() {
        if (errorStatus.getValue() != null && errorStatus.getValue().length() > 0) {
            errorStatus.setValue(null);
        }
        Log.i(DetailviewActivity.class.getSimpleName(), "error " + errorStatus);
        return false;
    }

    @Override
    public MutableLiveData<String> getErrorStatus() {
        return this.errorStatus;
    }

    @Override
    public boolean checkPWFieldInputCompleted(int actionId) {
        Log.i(OverviewActivity.class.getSimpleName(), " pw: " + user.getPw());
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( user.getPw() == null || user.getPw().length() ==6){
                errorStatusPW.setValue("To Short!");
            }
        }
        return false;
    }

    @Override
    public boolean onPWFieldInputChanged() {
        if (errorStatusPW.getValue() != null && errorStatusPW.getValue().length() > 0) {
            errorStatusPW.setValue(null);
        }
        return false;
    }

    @Override
    public MutableLiveData<String> getErrorStatusPW() {
        return this.errorStatusPW;
    }
}

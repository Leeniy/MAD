package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.view.inputmethod.EditorInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.User;


public class LoginViewModelImpl extends ViewModel implements ILoginViewmodel{

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private User user;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean checkFieldInputCompleted(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( user.getMail()== null || user.getMail().contains("@") || user.getMail().contains(".")){
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
        return false;
    }

    @Override
    public MutableLiveData<String> getErrorStatus() {
        return this.errorStatus;
    }
}

package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.model.User;

public interface ILoginViewModel {

    public User getUser();

    public boolean checkFieldInputCompleted(int actionId);

    public boolean onNameFieldInputChanged();

    public MutableLiveData<String> getErrorStatus();

    public boolean checkPWFieldInputCompleted(int actionId);

    public boolean onPWFieldInputChanged();

    public MutableLiveData<String> getErrorStatusPW();
}

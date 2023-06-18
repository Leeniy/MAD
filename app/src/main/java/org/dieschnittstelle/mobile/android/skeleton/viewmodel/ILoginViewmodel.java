package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.model.User;

public interface ILoginViewmodel {

    public User getUser();
    public boolean checkFieldInputCompleted(int actionId);

    public boolean onNameFieldInputChanged();

    public MutableLiveData<String> getErrorStatus();
}

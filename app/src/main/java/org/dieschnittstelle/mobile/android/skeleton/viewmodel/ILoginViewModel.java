package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.model.User;

public interface ILoginViewModel {

    public boolean checkFieldInputCompleted(int actionId);

    public boolean onNameFieldInputChanged();

    public MutableLiveData<String> getErrorStatus();

    public boolean checkPWFieldInputCompleted(int actionId);

    public boolean onPWFieldInputChanged();

    public MutableLiveData<String> getErrorStatusPW();

    public String getPw();

    public String getMail();

    public boolean getVisibility();

    public void setPw(String pw);

    public void setMail(String mail);

    public void setLoginstatus(Boolean loginstatus);

    public Boolean getLoginstatus();

}

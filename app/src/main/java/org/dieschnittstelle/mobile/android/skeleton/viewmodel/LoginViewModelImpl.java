package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;

import org.dieschnittstelle.mobile.android.skeleton.DetailviewActivity;
import org.dieschnittstelle.mobile.android.skeleton.OverviewActivity;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginViewModelImpl extends ViewModel implements ILoginViewModel {

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private MutableLiveData<String> errorStatusPW = new MutableLiveData<>();

    private String mail;

    private String pw;

    private boolean visibility = false;

    private Boolean loginstatus = false;

    private Boolean validMail = false;
    private Boolean validPW = false;

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getLoginstatus() {
        return loginstatus;
    }

    public void setLoginstatus(Boolean loginstatus) {
        this.loginstatus = loginstatus;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean checkFieldInputCompleted(int actionId) {
        Log.i(OverviewActivity.class.getSimpleName(), "checkField " + mail);
        String validRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

            if (mail == null || !mail.matches(validRegex)){
                errorStatus.setValue("Invalid Email Address");
                validMail = false;
            } else {
                validMail = true;
            }
            inputStatus();
        return false;
    }

    public boolean onNameFieldInputChanged() {
        if (errorStatus.getValue() != null && errorStatus.getValue().length() > 0) {
            errorStatus.setValue(null);
            setVisibility(false);
        }
        Log.i(DetailviewActivity.class.getSimpleName(), "error " + errorStatus);
        return false;
    }

    public MutableLiveData<String> getErrorStatus() {
        return this.errorStatus;
    }

    public boolean checkPWFieldInputCompleted(int actionId) {
        Log.i(OverviewActivity.class.getSimpleName(), " pw: " + pw);
            if ( pw == null || pw.length() != 6){
                errorStatusPW.setValue("Invalid PW Format");
                validPW = false;
            } else {
                validPW = true;
            }
            inputStatus();
        return false;
    }

    public boolean onPWFieldInputChanged() {
        if (errorStatusPW.getValue() != null && errorStatusPW.getValue().length() > 0) {
            errorStatusPW.setValue(null);
            setVisibility(false);
        }
        return false;
    }

    public MutableLiveData<String> getErrorStatusPW() {
        return this.errorStatusPW;
    }

    public void inputStatus (){
        if (validMail && validPW){
            loginstatus = true;
        } else {
            loginstatus = false;
        }
    }

}

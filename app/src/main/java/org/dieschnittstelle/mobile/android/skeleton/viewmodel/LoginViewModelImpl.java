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

public class LoginViewModelImpl extends ViewModel implements ILoginViewModel {

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private MutableLiveData<String> errorStatusPW = new MutableLiveData<>();

    private String mail;

    private String pw;

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

    public boolean checkFieldInputCompleted(int actionId) {
        Log.i(OverviewActivity.class.getSimpleName(), "mail: " + mail );
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( mail.contains("@") || mail.contains(".")){
                errorStatus.setValue("No Email");
            }
        }
        return false;
    }

    public boolean onNameFieldInputChanged() {
        if (errorStatus.getValue() != null && errorStatus.getValue().length() > 0) {
            errorStatus.setValue(null);
        }
        Log.i(DetailviewActivity.class.getSimpleName(), "error " + errorStatus);
        return false;
    }

    public MutableLiveData<String> getErrorStatus() {
        return this.errorStatus;
    }

    public boolean checkPWFieldInputCompleted(int actionId) {
        Log.i(OverviewActivity.class.getSimpleName(), " pw: " + pw);
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if ( pw == null || pw.length() ==6){
                errorStatusPW.setValue("To Short!");
            }
        }
        return false;
    }

    public boolean onPWFieldInputChanged() {
        if (errorStatusPW.getValue() != null && errorStatusPW.getValue().length() > 0) {
            errorStatusPW.setValue(null);
        }
        return false;
    }

    public MutableLiveData<String> getErrorStatusPW() {
        return this.errorStatusPW;
    }

}

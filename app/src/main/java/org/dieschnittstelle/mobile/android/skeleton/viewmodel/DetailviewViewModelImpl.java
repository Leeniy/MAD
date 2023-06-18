package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.view.inputmethod.EditorInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

public class DetailviewViewModelImpl extends ViewModel implements IDetailviewViewmodel{

    private ToDoItem item;

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

    private final MutableLiveData<Boolean> savedOcurred = new MutableLiveData<>();
    @Override
    public ToDoItem getItem() {
        return item;
    }

    public MutableLiveData<Boolean> getSavedOcurred(){
        return savedOcurred;
    }

    @Override
    public void onItemSaved() {
        savedOcurred.setValue(true);
    }

    @Override
    public boolean checkFieldInputCompleted(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
            if (item.getName() == null || item.getName().length() <5){
                errorStatus.setValue("Name to Short!");
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

    @Override
    public void onDeleteItem() {

    }

    public void setItem(ToDoItem item) {
        this.item = item;
    }


}

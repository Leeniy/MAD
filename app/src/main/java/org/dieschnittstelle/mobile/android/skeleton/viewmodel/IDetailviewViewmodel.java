package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

public interface IDetailviewViewmodel {

    public ToDoItem getItem();

    public void onItemSaved();

    public boolean checkFieldInputCompleted(int actionId);

    public boolean onNameFieldInputChanged();

    public MutableLiveData<String> getErrorStatus();

    public void onDeleteItem();

}

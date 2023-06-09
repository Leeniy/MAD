package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

public class DetailviewViewModelImpl extends ViewModel implements IDetailviewViewmodel{

    private ToDoItem item;

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

    public void setItem(ToDoItem item) {
        this.item = item;
    }

    @Override
    public void onItemDelete() {

    }

}

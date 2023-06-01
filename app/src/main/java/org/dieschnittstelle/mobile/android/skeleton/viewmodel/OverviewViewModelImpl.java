package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

import java.util.List;

public class OverviewViewModelImpl extends ViewModel implements IOverviewViewModel{

    private List<ToDoItem> items;


    public void setItems(List<ToDoItem> items) {
        this.items = items;
    }

    @Override
    public List<ToDoItem> getToDoItem() {
        return items;
    }
}

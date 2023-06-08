package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.view.View;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

public interface IDetailviewViewmodel {

    public ToDoItem getItem();

    public void onItemSaved();

    public void onItemDelete();
}

package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

public interface IDetailviewViewmodel {

    public ToDoItem getItem();

    public void onItemSaved();
}

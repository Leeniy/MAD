package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

import java.util.Comparator;
import java.util.List;

public interface IOverviewViewModel {

    public List<ToDoItem> getToDoItem();

    public Comparator<ToDoItem> getCurrentSortMode();

    public void switchSortMode();

    public ToDoItem getItem();
}

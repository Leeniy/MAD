package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

import java.util.Comparator;
import java.util.List;

public class OverviewViewModelImpl extends ViewModel implements IOverviewViewModel{

    public static final Comparator<ToDoItem> DONE = Comparator.comparing(ToDoItem::isChecked);
    public static final Comparator<ToDoItem> FAVOURITE = Comparator.comparing(ToDoItem::isFavourite);

    public static final Comparator<ToDoItem> DATE_TIME = Comparator.comparing(ToDoItem::getExpiry);
    public static final Comparator<ToDoItem> FAVOURITE_AND_DONE_AND_DATE_COMPARATOR = DONE.thenComparing(FAVOURITE.reversed());
    public static final Comparator<ToDoItem> CHECKED_AND_NAME_COMPARATOR = Comparator.comparing(ToDoItem::isChecked).thenComparing(ToDoItem::getName).thenComparing(DATE_TIME.reversed());
    private Comparator<ToDoItem> currentComparator = FAVOURITE_AND_DONE_AND_DATE_COMPARATOR;
    private List<ToDoItem> items;
    private ToDoItem item;

    public void setItems(List<ToDoItem> items) {
        this.items = items;
    }

    @Override
    public List<ToDoItem> getToDoItem() {
        return items;
    }

    public void setItem(ToDoItem item) {
        this.item = item;
    }

    public Comparator<ToDoItem> getCurrentSortMode(){
        return this.currentComparator;
    }

    @Override
    public void switchSortMode() {
        this.currentComparator = CHECKED_AND_NAME_COMPARATOR;
    }

}

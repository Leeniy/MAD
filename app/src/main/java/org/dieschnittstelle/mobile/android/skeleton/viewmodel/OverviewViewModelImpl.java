package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDoItem;

import java.util.Comparator;
import java.util.List;

public class OverviewViewModelImpl extends ViewModel implements IOverviewViewModel{

    public static final Comparator<ToDoItem> DONE = Comparator.comparing(ToDoItem::isChecked);
    public static final Comparator<ToDoItem> FAVOURITE = Comparator.comparing(ToDoItem::isFavourite);
    public static final Comparator<ToDoItem> DATE_TIME = Comparator.comparing(ToDoItem::getExpiry);
    public static final Comparator<ToDoItem> DONE_AND_FAVOURITE_DATE_COMPARATOR = DONE.thenComparing(FAVOURITE.reversed()).thenComparing(DATE_TIME);
    public static final Comparator<ToDoItem> DONE_AND_DATE_AND_FAVOURITE_COMPARATOR = DONE.thenComparing(DATE_TIME).thenComparing(FAVOURITE.reversed());
    private Comparator<ToDoItem> currentComparator = DONE_AND_FAVOURITE_DATE_COMPARATOR;
    private String comparator = "DFDT";
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
        if(this.comparator == "DFDT") {
            this.currentComparator = DONE_AND_DATE_AND_FAVOURITE_COMPARATOR;
            this.comparator = "DDTF";
        } else {
            this.currentComparator = DONE_AND_FAVOURITE_DATE_COMPARATOR;
            this.comparator = "DFDT";
        }

    }

    @Override
    public ToDoItem getItem() {
        return item;
    }

}

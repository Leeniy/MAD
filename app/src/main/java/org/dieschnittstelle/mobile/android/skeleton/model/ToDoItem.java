package org.dieschnittstelle.mobile.android.skeleton.model;

import android.app.DatePickerDialog;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class ToDoItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;

    @SerializedName("done")
    private boolean checked;
    private boolean favourite;

    public ToDoItem() {
    }

    public ToDoItem(String name) {
        this();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoItem toDoItem = (ToDoItem) o;
        return id == toDoItem.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


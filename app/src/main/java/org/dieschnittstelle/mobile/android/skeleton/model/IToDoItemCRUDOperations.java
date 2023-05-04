package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

public interface IToDoItemCRUDOperations {

    public ToDoItem createToDoItem (ToDoItem item);

    public List<ToDoItem> readAllToDoItems();

    public ToDoItem readToDoItem (long id);

    public boolean updateToDoItem (ToDoItem item);

    public boolean deleteToDoItem (long id);

}

package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

public interface IToDoItemCRUDOperations {

    public ToDoItem createToDoItem(ToDoItem item);

    public List<ToDoItem> readAllToDoItems();

    public ToDoItem readToDoItem(long id);

    public ToDoItem updateToDoItem(ToDoItem item);

    public boolean deleteToDoItem(long id);

    public boolean deleteAllTodoItems(boolean remote);

    public boolean deleteAllLocaToDoItems();

    public boolean deleteAllRemoteToDoItems();

    public Boolean login(User user);

}

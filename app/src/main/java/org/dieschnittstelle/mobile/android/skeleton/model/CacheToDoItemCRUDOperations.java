package org.dieschnittstelle.mobile.android.skeleton.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheToDoItemCRUDOperations implements IToDoItemCRUDOperations{

    private Map<Long, ToDoItem> toDoItemMap = new HashMap<>();
    private IToDoItemCRUDOperations realCrudOperations;

    public CacheToDoItemCRUDOperations (IToDoItemCRUDOperations realCrudOperations){
        this.realCrudOperations = realCrudOperations;
    }

    @Override
    public ToDoItem createToDoItem(ToDoItem item) {
        ToDoItem created = realCrudOperations.createToDoItem(item);
        toDoItemMap.put(created.getId(), created);
        return item;
    }

    @Override
    public List<ToDoItem> readAllToDoItems() {
        /*if (toDoItemMap.size() != 0) {
            realCrudOperations.readAllToDoItems().forEach(item -> {
                if (!toDoItemMap.containsValue(item.getId())) {
                    toDoItemMap.put(item.getId(), item);
                    Log.i(CacheToDoItemCRUDOperations.class.getSimpleName(), "sync");
                }
            });
        } else {
            realCrudOperations.readAllToDoItems().forEach(item -> {
                toDoItemMap.put(item.getId(), item);
            });

        Log.i(CacheToDoItemCRUDOperations.class.getSimpleName(), "fail");
        return new ArrayList<>(toDoItemMap.values());*/
        if (realCrudOperations.readAllToDoItems() != null) {
            realCrudOperations.readAllToDoItems().forEach(item -> {
                toDoItemMap.put(item.getId(), item);
            });
            Log.i(CacheToDoItemCRUDOperations.class.getSimpleName(), "local");
        } else {
            realCrudOperations.readAllToDoItems().forEach(item -> {
                if (!toDoItemMap.containsKey(item.getId())) {
                    toDoItemMap.put(item.getId(), item);
                    Log.i(CacheToDoItemCRUDOperations.class.getSimpleName(), "sync");
                }
            });
        }
        return new ArrayList<>(toDoItemMap.values());
    }

    @Override
    public ToDoItem readToDoItem(long id) {
        if (toDoItemMap.containsKey(id)){
            ToDoItem item = realCrudOperations.readToDoItem(id);
            if (item != null) {
                toDoItemMap.put(item.getId(), item);
            }
            return item;
        }
        return toDoItemMap.get(id);

    }

    @Override
    public ToDoItem updateToDoItem(ToDoItem item) {
        ToDoItem updated = this.realCrudOperations.updateToDoItem(item);
        toDoItemMap.put(item.getId(), updated);

        return updated;
    }

    @Override
    public boolean deleteToDoItem(long id) {
        if (this.realCrudOperations.deleteToDoItem(id)){
            toDoItemMap.remove(id);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean deleteAllTodoItems(boolean remote) {
        if (remote){
            return this.realCrudOperations.deleteAllTodoItems(remote);
        } else {
            return this.realCrudOperations.deleteAllTodoItems(remote);
        }
    }

    @Override
    public boolean login(User user) {
        return this.realCrudOperations.login(user);
    }
}

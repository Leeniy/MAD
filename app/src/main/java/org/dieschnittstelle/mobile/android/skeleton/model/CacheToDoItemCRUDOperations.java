package org.dieschnittstelle.mobile.android.skeleton.model;

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
        if (toDoItemMap.size() == 0) {
            realCrudOperations.readAllToDoItems().forEach(item -> {
                toDoItemMap.put(item.getId(), item);
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
        return false;
    }

    @Override
    public boolean login(User user) {
        return false;
    }
}

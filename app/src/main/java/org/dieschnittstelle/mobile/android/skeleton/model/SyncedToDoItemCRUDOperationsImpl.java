package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

public class SyncedToDoItemCRUDOperationsImpl implements IToDoItemCRUDOperations{

    private IToDoItemCRUDOperations localCRUD;
    private IToDoItemCRUDOperations remoteCRUD;

    private boolean synced;

    public SyncedToDoItemCRUDOperationsImpl (IToDoItemCRUDOperations localCRUD, IToDoItemCRUDOperations remoteCRUD){
        this.localCRUD = localCRUD;
        this.remoteCRUD = remoteCRUD;
    }

    @Override
    public ToDoItem createToDoItem(ToDoItem item) {
        ToDoItem created = localCRUD.createToDoItem(item);
        remoteCRUD.createToDoItem(created);
        return created;
    }

    @Override
    public List<ToDoItem> readAllToDoItems() {
        if (!synced) {
            this.syncLocalAndRemote();
            this.synced = true;
        }
        return localCRUD.readAllToDoItems();
    }

    private void syncLocalAndRemote() {
        if (this.localCRUD.readAllToDoItems() != null) {
            deleteAllTodoItems(true);
            for (ToDoItem item : localCRUD.readAllToDoItems()) {
                remoteCRUD.createToDoItem(item);
            }
        } else {
            deleteAllTodoItems(false);
            for (ToDoItem item : remoteCRUD.readAllToDoItems()) {
                localCRUD.createToDoItem(item);
            }
        }
    }

    @Override
    public ToDoItem readToDoItem(long id) {
        return localCRUD.readToDoItem(id);
    }

    @Override
    public ToDoItem updateToDoItem(ToDoItem item) {
        ToDoItem updated = localCRUD.updateToDoItem(item);
        remoteCRUD.updateToDoItem(updated);
        return updated;
    }

    @Override
    public boolean deleteToDoItem(long id) {
        if (localCRUD.deleteToDoItem(id)){
            return remoteCRUD.deleteToDoItem(id);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean deleteAllTodoItems(boolean remote) {
        if (remote){
            return this.remoteCRUD.deleteAllTodoItems(remote);
        } else {
            return this.localCRUD.deleteAllTodoItems(remote);
        }
    }

    @Override
    public boolean login(User user) {
        return this.remoteCRUD.login(user);
    }
}

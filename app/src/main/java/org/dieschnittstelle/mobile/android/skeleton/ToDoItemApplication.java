package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoItemCRUDOperationsImpl;

public class ToDoItemApplication extends Application {

    public IToDoItemCRUDOperations getCRUDOperations(){
        IToDoItemCRUDOperations crudOperations = new RetrofitToDoItemCRUDOperationsImpl();
        Toast.makeText(this, "Using CRUD Impl:" + crudOperations.getClass(), Toast.LENGTH_SHORT).show();
        return crudOperations;
    }
}

package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.CacheToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.SyncedToDoItemCRUDOperationsImpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ToDoItemApplication extends Application {

    private IToDoItemCRUDOperations crudOperations;

    private boolean offLineMode;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (checkConnectivity().get()) {
                    IToDoItemCRUDOperations crudOperations = new SyncedToDoItemCRUDOperationsImpl(
                            new RoomToDoItemCRUDOperationsImpl(this),
                            new RetrofitToDoItemCRUDOperationsImpl());
                    this.crudOperations = new CacheToDoItemCRUDOperations(crudOperations);
                    Toast.makeText(this, "Using synched data access", Toast.LENGTH_LONG).show();
            } else {
                this.crudOperations = new CacheToDoItemCRUDOperations(new RoomToDoItemCRUDOperationsImpl(this));
                this.offLineMode = true;
                Toast.makeText(this, "Remote api not accessible. Using local data access", Toast.LENGTH_LONG).show();
            }

        }
           catch (Exception e){
            //this.crudOperations = new RoomToDoItemCRUDOperationsImpl(this);
            this.offLineMode = true;
               throw new RuntimeException("Got excepting trying to run future for checking conectivity: ");
       }
    }

    public IToDoItemCRUDOperations getCRUDOperations(){
        Toast.makeText(this, "Using CRUD Impl:" + crudOperations.getClass(), Toast.LENGTH_SHORT).show();
        return this.crudOperations;
    }

    public CompletableFuture<Boolean> checkConnectivity(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        new Thread( () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("http://10.0.2.2:8080/api/todos").openConnection();
                connection.setConnectTimeout(500);
                connection.setReadTimeout(500);
                //connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                connection.getInputStream();
                future.complete(true);
            }
            catch (Exception e){
                future.complete(false);
            }
        }).start();

        return future;
    }

    public boolean isOffLineMode() {
        return offLineMode;
    }
}

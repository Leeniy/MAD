package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.IToDoItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoItemCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomToDoItemCRUDOperationsImpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ToDoItemApplication extends Application {

    private IToDoItemCRUDOperations crudOperations = new RetrofitToDoItemCRUDOperationsImpl();

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        try {
//            if (checkConnectivity().get()) {
//                this.crudOperations = new RetrofitToDoItemCRUDOperationsImpl();
//            } else {
//                this.crudOperations = new RoomToDoItemCRUDOperationsImpl(this);
//            }
//        }
//           catch (Exception e){
//            throw new RuntimeException("Got exception trying to run future for checking connectivity");
//       }
//    }

    public IToDoItemCRUDOperations getCRUDOperations(){
        Toast.makeText(this, "Using CRUD Impl:" + crudOperations.getClass(), Toast.LENGTH_SHORT).show();
        return crudOperations;
    }

    public Future<Boolean> checkConnectivity(){
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        new Thread( () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("http://10.0.2.2:8080/api/todos").openConnection();
                connection.setConnectTimeout(500);
                connection.setReadTimeout(500);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                connection.getInputStream();
                result.complete(true);
            }
            catch (Exception e){
                result.complete(false);
            }
        }).start();

        return result;
    }
}

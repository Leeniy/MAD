package org.dieschnittstelle.mobile.android.skeleton.model;

import androidx.annotation.RequiresPermission;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitToDoItemCRUDOperationsImpl implements IToDoItemCRUDOperations{

    public static interface ToDoItemResource{

        @POST("/api/todos")
        public Call<ToDoItem> create(@Body ToDoItem item);

        @GET("/api/todos")
        public Call<List<ToDoItem>> readAll();

        @GET("/api/todos/{todoId}")
        public Call<ToDoItem> read(@Path("todoId") long id);

        @PUT("/api/todos/{todoId}")
        public Call<ToDoItem> update(@Path("todoId") long id, @Body ToDoItem item);

        @DELETE("/api/todos/{todoId}")
        public Call<Boolean> delete(@Path("todoId") long id);

        @DELETE("/api/todos")
        public Call<Boolean> deleteAllTodos();

        @PUT ("/api/users/auth")
        public Call<Boolean> login(@Body User user);

    }

    private ToDoItemResource toDoItemResource;

    public RetrofitToDoItemCRUDOperationsImpl(){
        Retrofit webapiBase = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.toDoItemResource = webapiBase.create(ToDoItemResource.class);
    }

    @Override
    public ToDoItem createToDoItem(ToDoItem item) {
        try {
           return this.toDoItemResource.create(item).execute().body();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ToDoItem> readAllToDoItems() {
        /*try {
            Thread.sleep(2500);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }*/
        try {
            return this.toDoItemResource.readAll().execute().body();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ToDoItem readToDoItem(long id) {
        try {
            return this.toDoItemResource.read(id).execute().body();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ToDoItem updateToDoItem(ToDoItem item) {
        try {
            this.toDoItemResource.update(item.getId(), item).execute().body();
            return item;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteToDoItem(long id) {
        try {
            this.toDoItemResource.delete(id).execute().body();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteAllTodoItems(boolean remote) {
        return false;
    }

    public boolean deleteAllTodos(){
        try {
            this.toDoItemResource.deleteAllTodos().execute().body();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(User user){
        try {
            this.toDoItemResource.login(user).execute().body();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

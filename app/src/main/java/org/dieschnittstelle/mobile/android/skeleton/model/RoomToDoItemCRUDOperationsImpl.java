package org.dieschnittstelle.mobile.android.skeleton.model;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

public class RoomToDoItemCRUDOperationsImpl implements IToDoItemCRUDOperations {

    @Dao
    public static interface ToDoItemDao {

        @Query("select * from todoitem")
        public List<ToDoItem> readAll();

        @Query("select * from todoitem where id == (:id)")
        public ToDoItem readById(long id);

        @Insert
        public long create(ToDoItem item);

        @Update
        public void update(ToDoItem item);

        @Delete
        public void delete(ToDoItem item);
    }

    @Database(entities = {ToDoItem.class}, version = 1)
    public static abstract class ToDoItemDatabase extends RoomDatabase {
        public abstract ToDoItemDao getDao();
    }

    private ToDoItemDatabase db;

    public RoomToDoItemCRUDOperationsImpl(Context context) {
        db = Room.databaseBuilder(context, ToDoItemDatabase.class, "todoitems.db").build();
    }

    @Override
    public ToDoItem createToDoItem(ToDoItem item) {
        long id = db.getDao().create(item);
        item.setId(id);
        return item;
    }

    @Override
    public List<ToDoItem> readAllToDoItems() {
        return db.getDao().readAll();
    }

    @Override
    public ToDoItem readToDoItem(long id) {
        return db.getDao().readById(id);
    }

    @Override
    public boolean updateToDoItem(ToDoItem item) {
        db.getDao().update(item);
        return true;
    }

    @Override
    public boolean deleteToDoItem(long id) {
        db.getDao().delete(readToDoItem(id));
        return true;
    }

    @Override
    public boolean login(User user) {
        return false;
    }
}

package com.mark.itunes.data;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EntityItem.class}, version = 1, exportSchema = false)
public abstract class ItemsDatabase extends RoomDatabase {

    public abstract ItemDao itemDao();

    private static volatile ItemsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ItemsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ItemsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ItemsDatabase.class, "items_database")
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}

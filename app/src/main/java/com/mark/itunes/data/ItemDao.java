package com.mark.itunes.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    LiveData<List<EntityItem>> getItems();

    @Query("SELECT trackId FROM item WHERE trackId in (:ids)")
    Single<List<EntityItemId>> getItemsByIds(List<Integer> ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EntityItem item);

    @Delete
    void delete(EntityItem item);
}

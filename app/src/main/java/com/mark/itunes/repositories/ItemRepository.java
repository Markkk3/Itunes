package com.mark.itunes.repositories;

import android.content.Context;

import com.mark.itunes.data.EntityItem;
import com.mark.itunes.data.EntityItemId;
import com.mark.itunes.data.ItemDao;
import com.mark.itunes.data.ItemsDatabase;
import com.mark.itunes.network.Item;
import com.mark.itunes.network.NetworkService;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class ItemRepository {
    private final static int LIMIT = 100;
    private final ItemDao itemDao;


    public ItemRepository(Context application) {
        ItemsDatabase itemsDatabase = ItemsDatabase.getDatabase(application);
        itemDao = itemsDatabase.itemDao();
    }


    public Observable<List<Item>> getSearch(String type, String query) {
        return NetworkService.getInstance().getApi().searchRequest(type, query, LIMIT)
            .map(searchResult -> Observable.fromIterable(searchResult.getResults()))
            .flatMap(x -> x)
            .toList().toObservable();
    }

    public LiveData<List<EntityItem>> getItems() {
        return itemDao.getItems();
    }

    public Single<List<EntityItemId>> getItemsByIds(List<Integer> ids) {
        return itemDao.getItemsByIds(ids);
    }

    public void insert(EntityItem item) {
        ItemsDatabase.databaseWriteExecutor.execute(() -> itemDao.insert(item));
    }

    public void delete(EntityItem item) {
        ItemsDatabase.databaseWriteExecutor.execute(() -> itemDao.delete(item));
    }
}

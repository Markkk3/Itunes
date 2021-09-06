package com.mark.itunes.ui.saved;

import android.app.Application;

import com.mark.itunes.data.EntityItem;
import com.mark.itunes.repositories.ItemRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SavedViewModel extends AndroidViewModel {

    private final ItemRepository itemRepository;

    private final LiveData<List<EntityItem>> mItems;


    public SavedViewModel(Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        mItems = itemRepository.getItems();
    }

    public LiveData<List<EntityItem>> getList() {
        return mItems;
    }

    public void deleteItem(EntityItem item) {
        itemRepository.delete(item);
    }
}
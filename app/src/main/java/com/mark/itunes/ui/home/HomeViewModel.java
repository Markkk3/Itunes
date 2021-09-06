package com.mark.itunes.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mark.itunes.ImageHelper;
import com.mark.itunes.data.EntityItem;
import com.mark.itunes.data.EntityItemId;
import com.mark.itunes.network.Item;
import com.mark.itunes.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.mark.itunes.ui.home.HomeFragment.MUSIC_TRACK;

public class HomeViewModel extends AndroidViewModel {

    private static final String SEARCH_TEXT = "HomeViewModel.search_text";
    private static final String PREFERENCES = "app_preferences";

    private final ItemRepository itemRepository;
    private final SharedPreferences sharedpreferences;
    private final MutableLiveData<String> mSearchText;

    private Observable<List<Item>> itemObservable;
    private final MutableLiveData<String> mFilterName;
    private final Application application;

    public HomeViewModel(Application application) {
        super(application);
        this.application = application;
        itemRepository = new ItemRepository(application);
        sharedpreferences = getApplication().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        mSearchText = new MutableLiveData<>();
        mSearchText.setValue(getSavedText());
        mFilterName = new MutableLiveData<>();
        mFilterName.setValue(MUSIC_TRACK);
    }

    public void performSearch() {
        itemObservable = itemRepository.getSearch(mFilterName.getValue(), mSearchText.getValue());
    }

    public void saveItem(Item item, Bitmap bitmap) {
        if (bitmap != null) {
            insertItem(item.getEntityItem(ImageHelper.getByteFromImageView(bitmap)));
        } else {
            Glide.with(application)
                .as(Bitmap.class)
                .load(item.getArtworkUrl100())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        insertItem(item.getEntityItem(ImageHelper.getByteFromImageView(resource)));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        }
    }


    public Observable<List<Item>> getSearchItem() {
        return itemObservable;
    }


    public LiveData<String> getSearchText() {
        return mSearchText;
    }

    public LiveData<String> getFilter() {
        return mFilterName;
    }

    public void insertItem(EntityItem item) {
        itemRepository.insert(item);
    }

    public void setFilter(String filter) {
        if (!Objects.equals(mFilterName.getValue(), filter)) {
            mFilterName.setValue(filter);
        }
    }

    public void setSearchText(String searchText) {
        this.mSearchText.setValue(searchText);
    }

    private String getSavedText() {
        return sharedpreferences.getString(SEARCH_TEXT, null);
    }

    public void saveText() {
        sharedpreferences.edit().putString(SEARCH_TEXT, mSearchText.getValue()).apply();
    }

    public void updateSaved(List<Item> items, Runnable runnable) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Item item : items) {
            ids.add(item.getTrackId());
        }
        itemRepository.getItemsByIds(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableSingleObserver<List<EntityItemId>>() {
                @Override
                public void onSuccess(@NonNull List<EntityItemId> entityItemIds) {
                    for (Item item : items) {
                        for (EntityItemId entityItem : entityItemIds) {
                            if (item.getTrackId().equals(entityItem.trackId)) {
                                item.setSave(true);
                                break;
                            }
                        }
                    }
                    runnable.run();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }
            });
    }
}
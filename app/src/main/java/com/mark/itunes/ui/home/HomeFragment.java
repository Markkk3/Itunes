package com.mark.itunes.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mark.itunes.R;
import com.mark.itunes.adapter.ItemAdapter;
import com.mark.itunes.databinding.FragmentHomeBinding;
import com.mark.itunes.network.Item;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class HomeFragment extends Fragment {

    private final static String AUDIOBOOK = "audiobook";
    public final static String MUSIC_TRACK = "musicTrack";
    private final static String SOFTWARE = "software";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ItemAdapter itemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
            new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(this::searchItems);
        homeViewModel.getSearchText().observe(getViewLifecycleOwner(), s -> {
            homeViewModel.performSearch();
            searchItems();
        });
        homeViewModel.getFilter().observe(getViewLifecycleOwner(), s -> {
            homeViewModel.performSearch();
            searchItems();
        });
        itemAdapter = new ItemAdapter(new ArrayList<>(), (item, bitmap, pos) -> {
            homeViewModel.saveItem(item, bitmap);
            item.setSave(true);
            itemAdapter.notifyItemChanged(pos);
        });
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setAdapter(itemAdapter);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }


    private void searchItems() {
        compositeDisposable.clear();
        DisposableObserver<List<Item>> initialSearchObserver = getInitialObserver();
        compositeDisposable.add(initialSearchObserver);
        homeViewModel.getSearchItem()
            .doOnSubscribe(d -> swipeRefreshLayout.setRefreshing(true))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(initialSearchObserver);
    }

    private DisposableObserver<List<Item>> getInitialObserver() {
        return new DisposableObserver<List<Item>>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Item> items) {
                if (items.size() == 0) {
                    itemAdapter.updateAdapter(items);
                    Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                } else {
                    homeViewModel.updateSaved(items, () -> itemAdapter.updateAdapter(items));
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Toast.makeText(getActivity(), getString(R.string.error_format, e.getMessage()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView =
            (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    homeViewModel.setSearchText(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void openFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.filter_title)
            .setItems(R.array.items, (dialog, which) -> {
                switch (which) {
                    case 0:
                        homeViewModel.setFilter(MUSIC_TRACK);
                        break;
                    case 1:
                        homeViewModel.setFilter(AUDIOBOOK);
                        break;
                    case 2:
                        homeViewModel.setFilter(SOFTWARE);
                        break;
                }
            });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            openFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeViewModel.saveText();
        binding = null;
        compositeDisposable.clear();
    }

}
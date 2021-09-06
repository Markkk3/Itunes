package com.mark.itunes.ui.saved;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.itunes.R;
import com.mark.itunes.adapter.EntityItemAdapter;
import com.mark.itunes.databinding.FragmentSavedBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

public class SavedFragment extends Fragment {

    private SavedViewModel savedViewModel;
    private FragmentSavedBinding binding;
    private EntityItemAdapter itemAdapter;
    private MediaPlayer mediaPlayer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel =
            new ViewModelProvider(this).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);

        itemAdapter = new EntityItemAdapter(new ArrayList<>(), item -> deleteConfirmDialog(() -> {
            mediaPlayer.start();
            savedViewModel.deleteItem(item);
        }));
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setAdapter(itemAdapter);
        savedViewModel.getList().observe(getViewLifecycleOwner(), items -> itemAdapter.updateAdapter(items));
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.delete);
        return binding.getRoot();
    }

    private void deleteConfirmDialog(Runnable runnable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.delete_mess);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> runnable.run());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
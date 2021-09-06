package com.mark.itunes.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mark.itunes.R;
import com.mark.itunes.databinding.ListItemBinding;
import com.mark.itunes.network.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> items;
    private final ItemAdapter.onSaveClickListener onSaveClickListener;


    public ItemAdapter(List<Item> itemsList, onSaveClickListener listener) {
        items = itemsList;
        onSaveClickListener = listener;
    }

    public void updateAdapter(List<Item> itemsList) {
        items = itemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Item item = items.get(position);
        viewHolder.textViewName.setText(item.getTrackCensoredName());
        viewHolder.textViewDescription.setText(item.getArtistName());
        viewHolder.imagePreView.setImageDrawable(null);
        if (item.isSaved()) {
            viewHolder.imageSave.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            viewHolder.imageSave.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
        Glide.with(viewHolder.imagePreView.getContext())
            .load(item.getArtworkUrl100())
            .into(viewHolder.imagePreView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final ImageView imagePreView, imageSave;

        public ViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            textViewName = binding.textViewName;
            textViewDescription = binding.textViewDescription;
            imagePreView = binding.imagePreView;
            imageSave = binding.imageSave;
            imageSave.setOnClickListener(v -> onSaveClickListener.onClick(items.get(getBindingAdapterPosition()),
                imagePreView.getDrawable() != null ? ((BitmapDrawable) imagePreView.getDrawable()).getBitmap() : null, getBindingAdapterPosition()));
        }
    }

    public interface onSaveClickListener {
        void onClick(Item item, Bitmap bitmap, int position);
    }
}

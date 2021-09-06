package com.mark.itunes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.itunes.ImageHelper;
import com.mark.itunes.R;
import com.mark.itunes.data.EntityItem;
import com.mark.itunes.databinding.ListItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EntityItemAdapter extends RecyclerView.Adapter<EntityItemAdapter.ViewHolder> {

    private List<EntityItem> items;
    private final EntityItemAdapter.onSaveClickListener onSaveClickListener;


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName, textViewDescription;
        private final ImageView imagePreView;

        public ViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            textViewName = binding.textViewName;
            textViewDescription = binding.textViewDescription;
            imagePreView = binding.imagePreView;
            ImageView imageSave = binding.imageSave;
            imageSave.setImageResource(R.drawable.ic_baseline_delete_outline_24);
            imageSave.setOnClickListener(v -> onSaveClickListener.onClick(items.get(getBindingAdapterPosition())));
        }

    }


    public EntityItemAdapter(List<EntityItem> itemsList, onSaveClickListener listener) {
        items = itemsList;
        onSaveClickListener = listener;
    }

    public void updateAdapter(List<EntityItem> itemsList) {
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
        EntityItem item = items.get(position);
        viewHolder.textViewName.setText(item.getTrackName());
        viewHolder.textViewDescription.setText(item.getArtistName());
        viewHolder.imagePreView.setImageDrawable(null);
        viewHolder.imagePreView.setImageBitmap(ImageHelper.getBitmapFromByte(item.getImage()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface onSaveClickListener {
        void onClick(EntityItem item);
    }
}

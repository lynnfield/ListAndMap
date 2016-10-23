package com.lynnfield.listandmap;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynnfield.listandmap.databinding.AddressListItemBinding;

import java.util.Collections;
import java.util.List;

class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.VH> {
    private static final int NO_SELECTED = -1;
    @Nullable
    private OnItemSelectedListener listener;
    @NonNull
    private List<Address> data = Collections.emptyList();
    private int selectedIndex = NO_SELECTED;

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = AddressListItemBinding.inflate(inflater, parent, false).getRoot();
        final VH vh = new VH(v);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AddressListAdapter.this.notifyItemChanged(selectedIndex);
                selectedIndex = vh.getAdapterPosition();
                AddressListAdapter.this.notifyItemChanged(selectedIndex);
                if (listener != null)
                    listener.onItemSelected(data.get(selectedIndex));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final AddressListItemBinding b = AddressListItemBinding.bind(holder.itemView);
        b.setText(data.get(position).getText());
        b.address.setChecked(position == selectedIndex);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull final List<Address> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(@Nullable final OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(final Address address);
    }

    static class VH extends RecyclerView.ViewHolder {
        VH(final View itemView) {
            super(itemView);
        }
    }
}

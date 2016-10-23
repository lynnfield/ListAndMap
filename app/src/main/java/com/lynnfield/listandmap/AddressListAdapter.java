package com.lynnfield.listandmap;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynnfield.listandmap.databinding.AddressListItemBinding;

import java.util.Collections;
import java.util.List;

class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.VH> {
    private static final int NO_SELECTED = -1;
    private final ItemTouchHelper helper;
    @Nullable
    private OnItemSelectedListener listener;
    @NonNull
    private List<Address> data = Collections.emptyList();
    private int selectedIndex = NO_SELECTED;

    public AddressListAdapter() {
        super();
        ItemTouchHelper.SimpleCallback sc =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
                    @Override
                    public boolean onMove(
                            final RecyclerView rv,
                            final RecyclerView.ViewHolder vh,
                            final RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            final RecyclerView.ViewHolder vh,
                            final int direction) {
                        final int pos = vh.getAdapterPosition();
                        data.remove(pos);
                        if (selectedIndex == pos)
                            selectedIndex = NO_SELECTED;
                        notifyItemRemoved(pos);
                    }
                };
        helper = new ItemTouchHelper(sc);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView rv) {
        super.onAttachedToRecyclerView(rv);
        helper.attachToRecyclerView(rv);
    }

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = AddressListItemBinding.inflate(inflater, parent, false).getRoot();
        final VH vh = new VH(v);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setNewSelected(vh.getAdapterPosition());
                if (listener != null)
                    listener.onItemSelected(data.get(selectedIndex));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final AddressListItemBinding b = DataBindingUtil.getBinding(holder.itemView);
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

    public void setSelected(int selectedIndex) {
        setNewSelected(selectedIndex);
    }

    public void setListener(@Nullable final OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void setNewSelected(final int newSelected) {
        final int oldSelected = selectedIndex;
        selectedIndex = newSelected;
        AddressListAdapter.this.notifyItemChanged(oldSelected);
        AddressListAdapter.this.notifyItemChanged(selectedIndex);
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

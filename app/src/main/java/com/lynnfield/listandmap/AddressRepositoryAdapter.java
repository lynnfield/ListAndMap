package com.lynnfield.listandmap;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynnfield.listandmap.databinding.AddressListItemBinding;
import com.lynnfield.listandmap.events.SelectAddressEvent;

import org.greenrobot.eventbus.EventBus;

public class AddressRepositoryAdapter extends RecyclerView.Adapter<AddressRepositoryAdapter.VH> {
    private static final int NO_SELECTED = -1;
    @NonNull
    private final AddressesRepository repository;
    private int selectedIndex = NO_SELECTED;

    public AddressRepositoryAdapter(@NonNull final AddressesRepository repository) {
        super();
        this.repository = repository;
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(final int positionStart, final int itemCount) {
                if (positionStart <= selectedIndex && selectedIndex < positionStart + itemCount)
                    selectedIndex = NO_SELECTED;
            }
        });
    }

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = AddressListItemBinding.inflate(inflater, parent, false).getRoot();
        final VH vh = new VH(v);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int pos = vh.getAdapterPosition();
                EventBus.getDefault().post(new SelectAddressEvent(repository.getData().get(pos)));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final AddressListItemBinding b = DataBindingUtil.getBinding(holder.itemView);
        b.setText(repository.getData().get(position).getText());
        b.address.setChecked(position == selectedIndex);
    }

    @Override
    public int getItemCount() {
        return repository.getData().size();
    }

    public void setSelected(int selectedIndex) {
        setNewSelected(selectedIndex);
    }

    private void setNewSelected(final int newSelected) {
        final int oldSelected = selectedIndex;
        selectedIndex = newSelected;
        AddressRepositoryAdapter.this.notifyItemChanged(oldSelected);
        AddressRepositoryAdapter.this.notifyItemChanged(selectedIndex);
    }

    static class VH extends RecyclerView.ViewHolder {
        VH(final View itemView) {
            super(itemView);
        }
    }
}

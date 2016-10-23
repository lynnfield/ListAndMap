package com.lynnfield.listandmap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynnfield.listandmap.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    @Nullable
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter;
    private AddressListAdapter.OnItemSelectedListener adapterListener;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater i, final ViewGroup parent, final Bundle bundle) {
        final FragmentListBinding b = FragmentListBinding.inflate(i, parent, false);
        if (adapter != null) {
            setListAdapter(adapter);
            adapter = null;
        }
        return b.getRoot();
    }

    public void setAdapter(@NonNull final RecyclerView.Adapter<? extends RecyclerView.ViewHolder> a) {
        if (isVisible())
            setListAdapter(a);
        else
            adapter = a;
    }

    private void setListAdapter(final @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> a) {
        final FragmentListBinding b = FragmentListBinding.bind(getView());
        boolean hasItems = a.getItemCount() != 0;

        b.list.setAdapter(a);
        b.list.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        b.noDataMessage.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    }
}

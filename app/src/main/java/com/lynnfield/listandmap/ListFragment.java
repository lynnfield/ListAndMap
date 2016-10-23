package com.lynnfield.listandmap;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynnfield.listandmap.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    private final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (isVisible()) {
                final FragmentListBinding b = DataBindingUtil.findBinding(getView());

                if (b == null)
                    return;

                syncNoDataMessage(b);
            }
        }
    };
    @Nullable
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter;
    private AddressListAdapter.OnItemSelectedListener adapterListener;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater i, final ViewGroup parent, final Bundle bundle) {
        final FragmentListBinding b = FragmentListBinding.inflate(i, parent, false);
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter == null)
            return;
        setListAdapter(adapter);
        adapter = null;
    }

    public void setAdapter(@NonNull final RecyclerView.Adapter<? extends RecyclerView.ViewHolder> a) {
        if (isVisible())
            setListAdapter(a);
        else
            adapter = a;
    }

    private void setListAdapter(final @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> a) {
        final FragmentListBinding b = DataBindingUtil.findBinding(getView());

        if (b == null)
            return;

        final RecyclerView.Adapter adapter = b.list.getAdapter();
        if (adapter != null)
            adapter.unregisterAdapterDataObserver(observer);

        b.list.setAdapter(a);
        a.registerAdapterDataObserver(observer);

        syncNoDataMessage(b);
    }

    private void syncNoDataMessage(@NonNull final FragmentListBinding b) {
        boolean hasItems = b.list.getAdapter().getItemCount() != 0;
        b.list.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        b.noDataMessage.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    }
}

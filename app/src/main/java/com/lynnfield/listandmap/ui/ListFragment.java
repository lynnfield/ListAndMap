package com.lynnfield.listandmap.ui;

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

    private final RecyclerView.AdapterDataObserver syncNoDataMessage = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(final int positionStart, final int itemCount) {
            syncNoDataMessage();
        }

        @Override
        public void onChanged() {
            syncNoDataMessage();
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount) {
            syncNoDataMessage();
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount, final Object payload) {
            syncNoDataMessage();
        }

        @Override
        public void onItemRangeMoved(final int fromPosition, final int toPosition, final int itemCount) {
            syncNoDataMessage();
        }

        @Override
        public void onItemRangeRemoved(final int positionStart, final int itemCount) {
            syncNoDataMessage();
        }

        private void syncNoDataMessage() {
            if (getView() != null) {
                final FragmentListBinding b = DataBindingUtil.findBinding(getView());

                if (b == null)
                    return;

                ListFragment.this.syncNoDataMessage(b);
            }
        }
    };
    @Nullable
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater i, final ViewGroup parent, final Bundle bundle) {
        final FragmentListBinding b = FragmentListBinding.inflate(i, parent, false);

        if (adapter != null) {
            setListAdapter(b, adapter);
            adapter = null;
        }

        return b.getRoot();
    }

    public void setAdapter(@NonNull final RecyclerView.Adapter<? extends RecyclerView.ViewHolder> a) {
        if (getView() == null) {
            adapter = a;
        } else {
            final FragmentListBinding b = DataBindingUtil.getBinding(getView());
            if (b != null)
                setListAdapter(b, a);
        }
    }

    private void setListAdapter(
            @NonNull FragmentListBinding b,
            @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> a) {
        final RecyclerView.Adapter rva = b.list.getAdapter();
        if (rva != null)
            rva.unregisterAdapterDataObserver(syncNoDataMessage);

        b.list.setAdapter(a);
        a.registerAdapterDataObserver(syncNoDataMessage);

        syncNoDataMessage(b);
    }

    private void syncNoDataMessage(
            @NonNull FragmentListBinding b) {
        boolean hasItems = b.list.getAdapter().getItemCount() != 0;

        b.list.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        b.noDataMessage.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    }
}

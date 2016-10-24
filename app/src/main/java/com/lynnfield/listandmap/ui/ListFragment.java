package com.lynnfield.listandmap.ui;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lynnfield.listandmap.AddressRepositoryAdapter;
import com.lynnfield.listandmap.AddressesRepository;
import com.lynnfield.listandmap.databinding.FragmentListBinding;
import com.lynnfield.listandmap.events.AddressRemovedEvent;
import com.lynnfield.listandmap.events.NewAddressEvent;
import com.lynnfield.listandmap.events.SelectAddressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ListFragment extends Fragment {

    private final AddressesRepository repository = AddressesRepository.getInstance();
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
    private AddressRepositoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater i, final ViewGroup parent, final Bundle bundle) {
        adapter = new AddressRepositoryAdapter(repository);
        final ItemTouchHelper.SimpleCallback sc =
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
                        repository.remove(pos);
                    }
                };
        final FragmentListBinding b = FragmentListBinding.inflate(i, parent, false);
        final ItemTouchHelper h = new ItemTouchHelper(sc);

        b.list.setAdapter(adapter);
        adapter.registerAdapterDataObserver(syncNoDataMessage);
        h.attachToRecyclerView(b.list);

        syncNoDataMessage(b);

        return b.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewAddress(NewAddressEvent e) {
        final int i = repository.getData().indexOf(e.address);
        adapter.notifyItemInserted(i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressSelected(SelectAddressEvent e) {
        final int i = repository.getData().indexOf(e.address);
        adapter.setSelected(i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressRemoved(AddressRemovedEvent e) {
        adapter.notifyItemRemoved(e.index);
    }

    private void syncNoDataMessage(
            @NonNull FragmentListBinding b) {
        boolean hasItems = b.list.getAdapter().getItemCount() != 0;

        b.list.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        b.noDataMessage.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    }
}

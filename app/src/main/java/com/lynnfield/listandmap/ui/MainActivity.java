package com.lynnfield.listandmap.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;

import com.lynnfield.listandmap.AddressListAdapter;
import com.lynnfield.listandmap.R;
import com.lynnfield.listandmap.databinding.ActivityMainBinding;
import com.lynnfield.listandmap.events.NewAddressEvent;
import com.lynnfield.listandmap.events.RemoveAddressEvent;
import com.lynnfield.listandmap.events.SelectAddressEvent;
import com.lynnfield.listandmap.models.Address;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @NonNull
    private final List<Address> data = new ArrayList<>();
    private final AddressListAdapter addressListAdapter = new AddressListAdapter();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        addressListAdapter.setData(data);

        final ListFragment lf = (ListFragment) getFragmentManager().findFragmentById(R.id.address_list);
        lf.setAdapter(addressListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressListAdapter.setData(data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewAddress(@NonNull final NewAddressEvent e) {
        data.add(e.address);
        addressListAdapter.notifyItemInserted(data.size() - 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressSelected(final SelectAddressEvent e) {
        addressListAdapter.setSelected(data.indexOf(e.address));
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressRemoved(final RemoveAddressEvent e) {
        final int i = data.indexOf(e.address);
        if (i >= 0) {
            data.remove(i);
            addressListAdapter.notifyItemRemoved(i);
        }
    }
}

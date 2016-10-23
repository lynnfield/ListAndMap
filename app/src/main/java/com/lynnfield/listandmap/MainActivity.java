package com.lynnfield.listandmap;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lynnfield.listandmap.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddressListAdapter.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private ListFragment listFragment;
    private AddressListAdapter addressListAdapter;
    private List<Address> dummyData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        listFragment = new ListFragment();
        addressListAdapter = new AddressListAdapter();
        addressListAdapter.setListener(this);

        listFragment.setAdapter(addressListAdapter);

        for (int i = 0; i < 50; ++i)
            dummyData.add(new Address("Address #" + i, i, i));

        getFragmentManager()
                .beginTransaction()
                .add(R.id.root, listFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressListAdapter.setData(dummyData);
    }

    @Override
    public void onItemSelected(final Address address) {
        // TODO: 24.10.2016 show on map
    }
}

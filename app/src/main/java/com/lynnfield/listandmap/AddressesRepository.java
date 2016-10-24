package com.lynnfield.listandmap;

import com.lynnfield.listandmap.events.AddressRemovedEvent;
import com.lynnfield.listandmap.events.NewAddressEvent;
import com.lynnfield.listandmap.models.Address;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressesRepository {
    private static AddressesRepository ourInstance = new AddressesRepository();

    private final List<Address> addresses = new ArrayList<>();

    private AddressesRepository() {
    }

    public static AddressesRepository getInstance() {
        return ourInstance;
    }

    public List<Address> getData() {
        return Collections.unmodifiableList(addresses);
    }

    public void add(final Address a) {
        addresses.add(a);
        EventBus.getDefault().post(new NewAddressEvent(a));
    }

    public void remove(final Address a) {
        final int i = addresses.indexOf(a);
        if (i >= 0) {
            addresses.remove(i);
            EventBus.getDefault().post(new AddressRemovedEvent(a, i));
        }
    }

    public void remove(final int index) {
        final Address a = addresses.remove(index);
        EventBus.getDefault().post(new AddressRemovedEvent(a, index));
    }
}

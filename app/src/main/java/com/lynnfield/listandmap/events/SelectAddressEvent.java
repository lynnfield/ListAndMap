package com.lynnfield.listandmap.events;

import com.lynnfield.listandmap.models.Address;

public class SelectAddressEvent extends AddressEvent {
    public SelectAddressEvent(final Address address) {
        super(address);
    }
}

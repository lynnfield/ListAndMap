package com.lynnfield.listandmap.events;

import com.lynnfield.listandmap.models.Address;

public class NewAddressEvent extends AddressEvent {
    public NewAddressEvent(final Address address) {
        super(address);
    }
}

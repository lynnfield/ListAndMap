package com.lynnfield.listandmap.events;

import com.lynnfield.listandmap.models.Address;

class AddressEvent {
    public final Address address;

    AddressEvent(final Address address) {
        this.address = address;
    }
}

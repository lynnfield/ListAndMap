package com.lynnfield.listandmap.events;

import com.lynnfield.listandmap.models.Address;

public class AddressRemovedEvent extends RemoveAddressEvent {
    public final int index;

    public AddressRemovedEvent(final Address address, final int index) {
        super(address);
        this.index = index;
    }
}

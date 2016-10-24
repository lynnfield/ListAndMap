package com.lynnfield.listandmap.events;

import com.lynnfield.listandmap.models.Address;

public class RemoveAddressEvent extends AddressEvent {
    public RemoveAddressEvent(final Address address) {
        super(address);
    }
}

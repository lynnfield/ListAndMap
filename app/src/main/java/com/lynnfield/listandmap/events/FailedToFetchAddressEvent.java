package com.lynnfield.listandmap.events;

import com.lynnfield.listandmap.models.Address;

public class FailedToFetchAddressEvent extends AddressEvent {
    public FailedToFetchAddressEvent(final Address address) {
        super(address);
    }
}

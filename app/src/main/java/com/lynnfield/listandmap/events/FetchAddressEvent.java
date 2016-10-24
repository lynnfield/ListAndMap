package com.lynnfield.listandmap.events;


import com.lynnfield.listandmap.models.Address;

public class FetchAddressEvent extends AddressEvent {
    public FetchAddressEvent(final Address address) {
        super(address);
    }
}

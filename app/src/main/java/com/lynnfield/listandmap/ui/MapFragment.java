package com.lynnfield.listandmap.ui;

import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lynnfield.listandmap.R;
import com.lynnfield.listandmap.events.FailedToFetchAddressEvent;
import com.lynnfield.listandmap.events.FetchAddressEvent;
import com.lynnfield.listandmap.events.NewAddressEvent;
import com.lynnfield.listandmap.events.RemoveAddressEvent;
import com.lynnfield.listandmap.events.SelectAddressEvent;
import com.lynnfield.listandmap.models.Address;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback {
    private final Map<Address, Marker> addressMarker = new HashMap<>();
    @Nullable
    private GoogleMap map;
    @Nullable
    private Address focusTarget;
    private GoogleMap.OnMapClickListener fetchAddress = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(final LatLng l) {
            final Address a = new Address(null, l.latitude, l.longitude);
            EventBus.getDefault().post(new FetchAddressEvent(a));
        }
    };
    private GoogleMap.OnMarkerClickListener notifyListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker m) {
            if (m.getTag() instanceof Address) {
                EventBus.getDefault().post(new SelectAddressEvent((Address) m.getTag()));
                return true;
            }

            return false;
        }
    };
    private Geocoder geocoder;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
        geocoder = new Geocoder(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onMapReady(final GoogleMap m) {
        map = m;
        map.setOnMapClickListener(fetchAddress);
        map.setOnMarkerClickListener(notifyListener);

        if (focusTarget == null)
            return;

        focusOn(focusTarget);

        focusTarget = null;
    }

    public void focusOn(@NonNull final Address a) {
        if (map == null)
            focusTarget = a;
        else
            map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(a.getLatitude(), a.getLongitude()), 13));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onFetchAddress(FetchAddressEvent event) {
        final Address a = event.address;
        try {
            final List<android.location.Address> l =
                    geocoder.getFromLocation(a.getLatitude(), a.getLongitude(), 1);
            if (l == null || l.isEmpty()) {
                EventBus.getDefault().post(new FailedToFetchAddressEvent(a));
            } else {
                final android.location.Address addr = l.get(0);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < addr.getMaxAddressLineIndex(); i++)
                    sb.append(addr.getAddressLine(i)).append(',');

                sb.deleteCharAt(sb.length() - 1);

                a.setText(sb.toString());
                EventBus.getDefault().post(new NewAddressEvent(a));
            }
        } catch (IOException e) {
            Log.e(MapFragment.class.getName(), "failed to load address", e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFailedToFetchAddress(FailedToFetchAddressEvent e) {
        if (getView() != null)
            Snackbar.make(getView(), R.string.failed_to_find_address, Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewAddress(NewAddressEvent e) {
        if (map == null)
            return;

        final Address a = e.address;
        final MarkerOptions mo = new MarkerOptions();
        mo.position(new LatLng(a.getLatitude(), a.getLongitude()));
        final Marker m = map.addMarker(mo);
        m.setTag(a);
        addressMarker.put(a, m);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressSelected(SelectAddressEvent e) {
        focusOn(e.address);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressRemoved(RemoveAddressEvent e) {
        final Marker m = addressMarker.remove(e.address);

        if (m == null)
            return;

        m.remove();
    }
}

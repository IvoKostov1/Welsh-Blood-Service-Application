package com.example.mythirdtry.Interface;

import com.example.mythirdtry.ui.Addresses;

import java.util.List;

public interface AddressLoadListener {
    void onAllAddressLoadSuccess(List<Addresses> addressList);
    void onAllAddressLoadFailed(String message);
}

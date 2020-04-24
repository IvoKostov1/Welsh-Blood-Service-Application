package com.example.mythirdtry.Interface;

import java.util.List;

public interface AllCitiesLoadListener {

    void onAllCitiesLoadSuccess(List<String> cityNameList);
    void onAllCitiesLoadFailed(String message);
}

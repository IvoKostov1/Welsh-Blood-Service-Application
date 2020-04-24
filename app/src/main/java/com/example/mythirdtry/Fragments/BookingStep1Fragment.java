package com.example.mythirdtry.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mythirdtry.Interface.AddressLoadListener;
import com.example.mythirdtry.Interface.AllCitiesLoadListener;
import com.example.mythirdtry.R;
import com.example.mythirdtry.SpacesItemDecoration;
import com.example.mythirdtry.ui.Addresses;
import com.example.mythirdtry.ui.Common;
import com.example.mythirdtry.ui.MyClinicAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements AllCitiesLoadListener, AddressLoadListener {

    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    @BindView(R.id.recycler_city)
    RecyclerView recycler_city;

    Unbinder unbinder;

    CollectionReference allCitiesRef;
    CollectionReference allAddressesRef;

    AllCitiesLoadListener allCitiesLoadListener;
    AddressLoadListener allAddressLoadListener;

    AlertDialog alertDialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance()
    {
        if(instance == null)
        {
            instance = new BookingStep1Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allCitiesRef = FirebaseFirestore.getInstance().collection("Clinics");
        allAddressesRef = FirebaseFirestore.getInstance().collection("Clinics");

        allCitiesLoadListener = this;
        allAddressLoadListener = this;

        alertDialog = new SpotsDialog.Builder().setContext(getContext()).build();
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one, container, false);

        //Binds booking fragment step 1 to be on the screen
        unbinder = ButterKnife.bind(this,itemView);

        intView();
        loadAllCities();

        return itemView;
    }

    //Sets up the recycler view for the clinics
    private void intView()
    {
        recycler_city.setHasFixedSize(true);
        recycler_city.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_city.addItemDecoration(new SpacesItemDecoration(4));
    }

    //Loads all cities from Firestore
    private void loadAllCities()
    {
        allCitiesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<String> citylist = new ArrayList<>();

                    citylist.add("Please choose a city");
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                    {
                        citylist.add(documentSnapshot.getId());
                    }
                    allCitiesLoadListener.onAllCitiesLoadSuccess(citylist);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                allCitiesLoadListener.onAllCitiesLoadFailed(e.getMessage());
            }
        });
    }


    //This method sets up all the cities in the material spinner
    @Override
    public void onAllCitiesLoadSuccess(List<String> cityNameList) {
        spinner.setItems(cityNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0)
                {
                    //When the user selects a city,
                    //all clinics from that city are displayed
                    loadClinicOfCity(item.toString());
                    recycler_city.setVisibility(View.VISIBLE);
                }
                else
                {
                    recycler_city.setVisibility(View.GONE);
                }
            }
        });
    }

    //This method loads all clinics from the selected city
    private void loadClinicOfCity(String cityName)
    {
        alertDialog.show();

        Common.city = cityName;

        allAddressesRef = FirebaseFirestore.getInstance().collection("Clinics")
                .document(cityName)
                .collection("moreClinics");

        allAddressesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Addresses> list = new ArrayList<>();

                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                    {
                        Addresses addresses = documentSnapshot.toObject(Addresses.class);
                        addresses.setClinicId(documentSnapshot.getId());
                        list.add(addresses);
                    }
                    allAddressLoadListener.onAllAddressLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                allAddressLoadListener.onAllAddressLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllCitiesLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllAddressLoadSuccess(List<Addresses> addressesList) {

        MyClinicAdapter adapter = new MyClinicAdapter(getActivity(), addressesList);
        recycler_city.setAdapter(adapter);

        alertDialog.dismiss();

    }

    @Override
    public void onAllAddressLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
}

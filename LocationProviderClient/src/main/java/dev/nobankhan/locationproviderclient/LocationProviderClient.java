package dev.nobankhan.locationproviderclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import dev.nobankhan.locationproviderclient.callbacks.ClientCallback;
import dev.nobankhan.locationproviderclient.exceptions.ClientException;

public class LocationProviderClient {
    private ClientCallback clientCallback;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private static final int REQUEST_CHECK_SETTINGS = 10001;

    //use this constructor if you are using it from a service
    //you won't be able to use methode requestpermission
    //to use the methode requestpermission use second constructor



    public LocationProviderClient(@NonNull Context context)
    {
        this.context = context;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest .setFastestInterval(2000);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    //use this constructor if you are using it from an activity
    public LocationProviderClient(@NonNull Context context, @NonNull Activity activity)
    {
        this.context = context;
        this.activity = activity;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest .setFastestInterval(2000);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }
    public boolean checkpermission() {
        return ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestpermission() throws ClientException{
        if(activity == null)
        {
            throw new ClientException("Activity is null use Constructor LocationProviderClient(Context context, Activity activity);");
        }
        String PERMISSION_STRING[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(activity, PERMISSION_STRING, 100);

    }


    public boolean isGPSturnedON()
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    public void getLocationWithCallback(ClientCallback clientCallback) throws ClientException {
        this.clientCallback = clientCallback;
        if(!checkpermission())
        {
            throw new ClientException("Permission Denied");
        }
        if(!isGPSturnedON())
        {
            throw new ClientException("GPS Turned OFF");
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(this);
                int i = locationResult.getLocations().size();
                Location l = locationResult.getLocations().get(i-1);
                clientCallback.CallBackFunc(l);
            }
        }, Looper.getMainLooper());
    }

   public void turnonLocation() throws ClientException {
        if(activity == null)
        {
            throw new ClientException("Activity is null use Constructor LocationProviderClient(Context context, Activity activity);");
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context.getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    //Location Already Turned On
                    return;

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(activity,REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });



    }


}

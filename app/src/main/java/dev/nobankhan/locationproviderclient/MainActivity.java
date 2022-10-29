package dev.nobankhan.locationproviderclient;

import static dev.nobankhan.locationproviderclient.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import dev.nobankhan.locationproviderclient.callbacks.ClientCallback;
import dev.nobankhan.locationproviderclient.exceptions.ClientException;

public class MainActivity extends AppCompatActivity {

    LocationProviderClient l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = new LocationProviderClient(this, this);
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(view -> {
            try {
                location();
            } catch (ClientException e) {
                Log.i("Clientexception", e.toString());
            }
        });

    }

    void location() throws ClientException {
        l.requestpermission();
        if(l.isGPSturnedON())
        {
            l.getLocationWithCallback(new ClientCallback() {
                @Override
                public void CallBackFunc(Location location) {
                    Toast.makeText(MainActivity.this, location.getLatitude()+"\n"+location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
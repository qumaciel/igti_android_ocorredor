package br.com.igti.android.ocorredor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Display;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maciel on 12/5/14.
 */
public class CorridaMapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private ArrayList<Location> mTrajeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        // Coloca o mapa na posição atual do dispositivo
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        // Pede a lista do trajeto da corrida
        mTrajeto = CorridaManager.get(this).getTrajeto();

        updateUI();
    }

    private void updateUI() {
        if (mGoogleMap == null || mTrajeto == null) {
            return;
        }

        PolylineOptions line = new PolylineOptions();
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (Location loc : mTrajeto) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Resources r = getResources();
            if (mTrajeto.indexOf(loc) == 0) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions starMarkerOptions = new MarkerOptions().position(latLng)
                        .title(r.getString(R.string.corrida_inicia))
                        .snippet(r.getString(R.string.corrida_iniciada_em_format, startDate));
                mGoogleMap.addMarker(starMarkerOptions);
            } else if (mTrajeto.indexOf(loc) == mTrajeto.size() - 1) {
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions finishMarkerOptions = new MarkerOptions().position(latLng)
                        .title(r.getString(R.string.corrida_finaliza))
                        .snippet(r.getString(R.string.corrida_finalizada_em_format, endDate));
                mGoogleMap.addMarker(finishMarkerOptions);
            }
            line.add(latLng);
            latLngBuilder.include(latLng);
        }
        mGoogleMap.addPolyline(line);
        Display display = this.getWindowManager().getDefaultDisplay();
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBounds, display.getWidth(), display.getHeight(), 15);
        mGoogleMap.moveCamera(movement);
    }
}

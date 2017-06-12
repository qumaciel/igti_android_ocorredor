package br.com.igti.android.ocorredor;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import java.util.ArrayList;

/**
 * Created by maciel on 12/4/14.
 */
public class CorridaManager {
    private static final String TAG = "RunManager";

    public static final String ACTION_LOCATION = "br.com.igti.android.ocorredor.ACTION_LOCATION";

    private static CorridaManager sCorridaManager;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private ArrayList<Location> mTrajeto;

    private  CorridaManager(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mTrajeto = new ArrayList<Location>();
    }

    public static CorridaManager get(Context c) {
        if(sCorridaManager == null) {
            sCorridaManager = new CorridaManager(c.getApplicationContext());
        }
        return sCorridaManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext,0,broadcast,flags);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;

        // inicia a requisição de Localização
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider,0,0,pi);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if(pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTracking() {
        return getLocationPendingIntent(false) != null;
    }

    public void addLocationTrajeto (Location location) {
        mTrajeto.add(location);
    }

    public ArrayList<Location> getTrajeto() {
        return mTrajeto;
    }
}

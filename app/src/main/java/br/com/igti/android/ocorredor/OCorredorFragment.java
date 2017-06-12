package br.com.igti.android.ocorredor;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by maciel on 12/4/14.
 */
public class OCorredorFragment extends Fragment {

    private Button mIniciarButton, mPararButton, mMapaButton;
    private TextView mIniciadaTextView, mLatitudeTextView,
            mLongitudeTextView, mAltitudeTextView, mDuracaoTextView;

    private CorridaManager mCorridaManager;

    private Corrida mCorrida;
    private Location mUltimaLocalizacao;


    private View.OnClickListener mIniciarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCorridaManager.startLocationUpdates();
            mCorrida = new Corrida();
            updateUI();
        }
    };

    private View.OnClickListener mPararListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCorridaManager.stopLocationUpdates();
            updateUI();
        }
    };

    private View.OnClickListener mMapaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), CorridaMapaActivity.class);
            startActivity(i);
        }
    };

    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {
        @Override
        protected void onLocationReceived(Context context, Location location) {
            if (!mCorridaManager.isTracking()) {
                return;
            }
            mUltimaLocalizacao = location;
            mCorridaManager.addLocationTrajeto(location);
            if (isVisible()) {
                updateUI();
            }
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText = enabled ? R.string.gps_habilitado : R.string.gps_desabilitado;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        }
    };

    public static OCorredorFragment newInstance() {
        OCorredorFragment fragment = new OCorredorFragment();
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCorridaManager = mCorridaManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ocorredor, container, false);

        mIniciadaTextView = (TextView)view.findViewById(R.id.corrida_iniciadaTextView);
        mLatitudeTextView = (TextView)view.findViewById(R.id.corrida_latitudeTextView);
        mLongitudeTextView = (TextView)view.findViewById(R.id.corrida_longitudeTextView);
        mAltitudeTextView = (TextView)view.findViewById(R.id.corrida_altitudeTextView);
        mDuracaoTextView = (TextView)view.findViewById(R.id.corrida_duracaoTextView);

        mIniciarButton = (Button)view.findViewById(R.id.corrida_iniciarButton);
        mIniciarButton.setOnClickListener(mIniciarListener);

        mPararButton = (Button)view.findViewById(R.id.corrida_pararButton);
        mPararButton.setOnClickListener(mPararListener);

        mMapaButton = (Button)view.findViewById(R.id.corrida_mapaButton);
        mMapaButton.setOnClickListener(mMapaListener);

        updateUI();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver,new IntentFilter(CorridaManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private void updateUI() {
        boolean iniciada = mCorridaManager.isTracking();

        mIniciarButton.setEnabled(!iniciada);
        mPararButton.setEnabled(iniciada);

        if (mCorrida != null) {
            mIniciadaTextView.setText(mCorrida.getDataInicio().toString());
        }

        int duracaoSegundos = 0;
        if (mCorrida != null && mUltimaLocalizacao != null) {
            duracaoSegundos = mCorrida.getDurationSeconds(mUltimaLocalizacao.getTime());
            mLatitudeTextView.setText(Double.toString(mUltimaLocalizacao.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mUltimaLocalizacao.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mUltimaLocalizacao.getAltitude()));
            mDuracaoTextView.setText(Corrida.formataDuracao(duracaoSegundos));
            mIniciadaTextView.setEnabled(!iniciada);
        }
    }
}

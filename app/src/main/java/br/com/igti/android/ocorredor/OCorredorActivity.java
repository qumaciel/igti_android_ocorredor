package br.com.igti.android.ocorredor;

import android.app.Fragment;


public class OCorredorActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return OCorredorFragment.newInstance();
    }
}

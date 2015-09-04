package com.just8.apps.whoopee;

import android.net.Uri;
import android.support.v4.app.Fragment;

public class WhoopeeEditActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        getIntent();
        return WhoopeeEditFragment.newInstance(); }

    public void onFragmentInteraction(Uri position) {
    }

}

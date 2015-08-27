package com.just8.apps.whoopee;

import android.net.Uri;
import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return WhoopeeFragment.newInstance("default"); }

    public void onFragmentInteraction(Uri position) {
    }

}

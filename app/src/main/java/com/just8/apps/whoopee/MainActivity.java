package com.just8.apps.whoopee;

import android.net.Uri;
import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity
        implements WhoopeeFragment.OnFragmentInteractionListener{

    @Override
    protected Fragment createFragment() {
        return WhoopeeFragment.newInstance("default", "param2"); }

    public void onFragmentInteraction(Uri position) {
    }

}

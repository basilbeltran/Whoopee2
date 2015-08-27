package com.just8.apps.whoopee;

import android.support.v4.app.Fragment;

/**
 * BB
 * Created by kandinski on 2015-08-26.
 */
public class WhoopeeListActivity extends SingleFragmentActivity{


        @Override
        protected Fragment createFragment(){
            return new WhoopeeListFragment();
        }

    }


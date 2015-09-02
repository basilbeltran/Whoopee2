package com.just8.apps.whoopee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class WhoopeePagerActivity extends FragmentActivity {          //holds a ViewPager
    private ViewPager mViewPager;
    private ArrayList<Whoopee> whoopees;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        mViewPager = new ViewPager(this);                         // ViewPager
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        whoopees = WhoopeeData.get().getWhoopees();
        if(whoopees.size() == 0){   // TODO move all this to WhoopeeData
            G.newInstall=true;
            WhoopeeData.reset();
            if (G.G_DEBUG) Log.v(U.getTag(), "G.newInstall is........ " + String.valueOf(G.newInstall));
            WhoopeeData.get().saveWhoopees();
            whoopees = WhoopeeData.get().getWhoopees();
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {       //  FragmentStatePagerAdapter.getItem(int pos)
            @Override
            public int getCount() {
                return whoopees.size();
            }

            @Override
            public Fragment getItem(int pos) {
                Whoopee whoopee = whoopees.get(pos);
                return WhoopeeFragment.newInstance(whoopee.getMWhoopeeName());        //PAGER  swipes left and right *NEW FRAGMENTS*
            }
        });

        mViewPager.setCurrentItem(0);


//this is for  when the pager is call from the list
        //UUID wID = (UUID) getIntent().getSerializableExtra(WhoopeeFragment.EXTRA_WHOOPEE_ID);         //TODO spin through whole ArrayList to match ID to set Pagers current item efficient ?
//        for (int i = 0; i < whoopees.size(); i++) {
//            if (whoopees.get(i).getID().equals(wID)) {               //TODO how does this work? What does mViewPager do with this int?
//                mViewPager.setCurrentItem(i);
//                break;
//            }
//        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }

            public void onPageSelected(int pos) {
                Whoopee whoopee = whoopees.get(pos);
                if (whoopee.getMWhoopeeName() != null) {
                    setTitle(whoopee.getMWhoopeeName());
                }
            }
        });
    }//onCreate


}//class
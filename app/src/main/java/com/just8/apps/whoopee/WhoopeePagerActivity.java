package com.just8.apps.whoopee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

public class WhoopeePagerActivity extends FragmentActivity {          //holds a ViewPager
    private ViewPager mViewPager;
    private ArrayList<Whoopee> mAllWhoopees;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        mViewPager = new ViewPager(this);                         // ViewPager
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mAllWhoopees = WhoopeeData.get().getWhoopees();


        if(mAllWhoopees.size() == 0){   // TODO DEVELOPMENT ERROR CONDITION  (re)move all this to WhoopeeData
            G.newInstall=true;
            WhoopeeData.reset();
            if (G.G_DEBUG) Log.v(U.getTag(), "CONFIG RESET newInstall is........ " + String.valueOf(G.newInstall));
            WhoopeeData.get().saveWhoopees();
            mAllWhoopees = WhoopeeData.get().getWhoopees();
        }




        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {       //  FragmentStatePagerAdapter.getItem(int pos)
            @Override
            public int getCount() {
                return mAllWhoopees.size();
            }

            @Override
            public Fragment getItem(int pos) {
                if (G.DEBUG) Log.v(U.getTag(), "NEW wFRAG SHOWING POSITION........ " + pos);
                return WhoopeePageFragment.newInstance(pos);        //PAGER  swipes left and right *NEW FRAGMENTS*
            }
        });

        mViewPager.setCurrentItem(0);


//this is for  when the pager is call from the list
        //UUID wID = (UUID) getIntent().getSerializableExtra(WhoopeePageFragment.EXTRA_WHOOPEE_ID);         //TODO spin through whole ArrayList to match ID to set Pagers current item efficient ?
//        for (int i = 0; i < mAllWhoopees.size(); i++) {
//            if (mAllWhoopees.get(i).getID().equals(wID)) {               //TODO how does this work? What does mViewPager do with this int?
//                mViewPager.setCurrentItem(i);
//                break;
//            }
//        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {
                if (G.DEBUG) Log.v(U.getTag(), "STATE........ " + state);

            }

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
                if (G.DEBUG) Log.v(U.getTag(), "POSITION:OFFSET:PIXELS........ " + pos+":"+posOffset+":"+posOffsetPixels );
            }

            public void onPageSelected(int pos) {
                if (G.DEBUG) Log.v(U.getTag(), "POSITION........ " + pos);
                Whoopee whoopee = mAllWhoopees.get(pos);
                if (whoopee.getMWhoopeeName() != null) {
                    setTitle(whoopee.getMWhoopeeName());
                }
            }
        });
    }//onCreate


}//class
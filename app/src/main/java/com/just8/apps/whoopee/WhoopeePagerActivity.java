package com.just8.apps.whoopee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * BB
 * Created by kandinski on 2015-08-26.
 */
public class WhoopeePagerActivity {

public class CrimePagerActivity extends FragmentActivity {          //holds a ViewPager
    private ViewPager mViewPager;
    private ArrayList<Whoopee> whoopees;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);                               // ViewPager
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        whoopees = WhoopeeManager.get(this).getWhoopees();
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {       //  FragmentStatePagerAdapter.getItem(int pos)
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

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(WhoopeeFragment.EXTRA_WHOOPEE_ID);         //TODO spin through whole ArrayList to match ID to set Pagers current item efficient ?

        for (int i = 0; i < whoopees.size(); i++) {
            if (whoopees.get(i).getId().equals(crimeId)) {               //TODO how does this work? What does mViewPager do with this int?
                mViewPager.setCurrentItem(i);
                break;
            }
        }

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

}
}//class
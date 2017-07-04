package io.hasura.drive_android.ui.launcher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.hasura.drive_android.R;


/**
 * Created by jaison on 30/03/17.
 */

public class OnBoardingViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = {
            "A safe place for files",
            "Take it all with you",
            "Do more together"
    };

    private String[] descriptions = {
            "Any file you keep in Drive backend up so that you can't lose it",
            "Drive syncs across devices so that every file stays with you",
            "Invite others to view or edit any file or folder that you choose"
    };

    private int[] imageResources = {
            R.drawable.safe,
            R.drawable.carry_around,
            R.drawable.cloud
    };

    public OnBoardingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OnBoardingFragment.newInstance(titles[position],descriptions[position],imageResources[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}

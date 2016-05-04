package com.fmi.evelina.unimobileapp.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ContactFragmentsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "Administration", "Lecturers"};
    private Context context;

    public ContactFragmentsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: {
                return new AdministrationContactsFragment();
            }
            case 1: {
                return new LecturersContactsFragment();
            }
            default: {
                Log.e("FMI_APP_EXCEPTION", "Unable to find tab fragment");
                return null;
            }
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
package com.example.blockphone;

/**
 * Created by Жамбыл on 29.03.2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    int type;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TabViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb,int type) {
        super(fm);
        this.type = type;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (type){
            case 1:
                switch (position) {
                    case 0:
                        return new TopTab1();
                    case 1:
                        return new TopTab2();
                }
            case 2:
                switch (position) {
                    case 0:
                        return new PresTab1();
                    case 1:
                        return new PresTab2();
                }


        }

        return new Fragment();
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
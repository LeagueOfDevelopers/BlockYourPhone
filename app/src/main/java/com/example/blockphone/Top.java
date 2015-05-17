package com.example.blockphone;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * Created by Жамбыл on 27.03.2015.
 */
public class Top  extends AppActivity {

    int NumbOfTabs = 2;
    ViewPager pager;
    TabViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Друзья","Все"};


    public Top(){
        super(R.id.tool_bar_2, "Рейтинг", R.id.RecyclerView, R.id.DrawerLayoutTop, R.id.layoutFromRecycler, 2);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);

        startUI();
        startLocalUI();
    }
    public void startLocalUI()
    {
        adapter =  new TabViewPagerAdapter(getSupportFragmentManager(),Titles, NumbOfTabs,1);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.Grey);
            }
        });




    }

}
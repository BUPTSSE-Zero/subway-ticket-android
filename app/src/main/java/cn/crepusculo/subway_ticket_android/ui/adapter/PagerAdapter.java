package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.crepusculo.subway_ticket_android.ui.fragment.settings.TicketHistoryFragment;
import cn.crepusculo.subway_ticket_android.ui.fragment.settings.TicketPayFragment;
import cn.crepusculo.subway_ticket_android.ui.fragment.settings.TicketSubmitFragment;


public class PagerAdapter extends FragmentPagerAdapter{
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                return new TicketPayFragment();
            case 1:
                return new TicketSubmitFragment();
            case 2:
                return new TicketHistoryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

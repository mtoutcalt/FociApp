package foci.bu.outcalt.fociapp.tab;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: InfoTab1 infoTab1 = new InfoTab1();
                return infoTab1;
            case 1: InfoTab2 infoTab2 = new InfoTab2();
                return infoTab2;
            case 2: InfoTab3 infoTab3 = new InfoTab3();
                return infoTab3;
            case 3: InfoTab4 infoTab4 = new InfoTab4();
                return infoTab4;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

package luminosit.sunmera.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import luminosit.sunmera.R;

/**
 * Created by GaryTang on 4/6/15.
 */
public class MyTabListener implements ActionBar.TabListener {

    Fragment fragment;

    public MyTabListener(Fragment fragment){
        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //called to update the container
        fragmentTransaction.replace(R.id.container_tabs, fragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}

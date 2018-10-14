package in.sensemusic.sense;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.Objects;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ViewPager
        ViewPager viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.viewpager);
        final FragmentManager fragmentManager = getChildFragmentManager();
        viewPager.setAdapter(new HomeAdapter(fragmentManager));


        TabLayout tabLayout = getActivity().findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            PlayerFragment playerFragment = new PlayerFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_main,playerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Home");
         //((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
    }

}

class HomeAdapter extends FragmentPagerAdapter {

    private String Tabtitle[] ={"Songs","Album","Artist"};

    HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        //Log.d("sense","Called GetITEM at pos : "+position);
        if(position == 0) {
            return new SongFragment();
        }
        if(position == 1) {
            return new AlbumFragment();
        }
        if(position == 2) {
            return new ArtistFragment();
        }
        //else default
        return new SongFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Tabtitle[position];
    }


}
package in.sensemusic.sense;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ViewPager
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        final FragmentManager fragmentManager = getChildFragmentManager();
        viewPager.setAdapter(new HomeAdapter(fragmentManager,getContext()));


        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlayerFragment playerFragment = new PlayerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main,playerFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

                /* Snackbar.make(view, "Opening Player Fragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) getActivity()).setActionBarTitle("Home");
         //((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
    }

}

class HomeAdapter extends FragmentPagerAdapter {

    final int PageCount = 3;
    private String Tabtitle[] ={"Songs","Album","Artist"};
    private Context context;

    public HomeAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return PageCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Log.d("sense","Called GetITEM at pos : "+position);
        Fragment fragment = new SongFragment();
        if(position == 0) {
            return fragment = new SongFragment();
        }
        if(position == 1) {
            return fragment = new AlbumFragment();
        }
        if(position == 2) {
            return fragment = new ArtistFragment();
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Tabtitle[position];
    }


}
package in.sensemusic.sense.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import in.sensemusic.sense.R;
import in.sensemusic.sense.activities.MainActivity;
import in.sensemusic.sense.adapters.HomeAdapter;

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
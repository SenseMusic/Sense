package in.sensemusic.sense.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.sensemusic.sense.fragments.AlbumFragment;
import in.sensemusic.sense.fragments.ArtistFragment;
import in.sensemusic.sense.fragments.SongFragment;

public class HomeAdapter extends FragmentPagerAdapter {

    private String Tabtitle[] ={"Songs","Album","Artist"};

    public HomeAdapter(FragmentManager fm) {
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

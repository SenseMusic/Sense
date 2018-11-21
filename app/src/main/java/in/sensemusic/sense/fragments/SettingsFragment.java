package in.sensemusic.sense.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import in.sensemusic.sense.R;
import in.sensemusic.sense.activities.MainActivity;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Settings");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("About");
    }
}

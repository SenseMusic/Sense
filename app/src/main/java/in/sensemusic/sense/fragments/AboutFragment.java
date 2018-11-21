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

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("About");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("About");
    }

}

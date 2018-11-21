package in.sensemusic.sense.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

import in.sensemusic.sense.R;
import in.sensemusic.sense.activities.MainActivity;

public class FeedbackFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText feedback = Objects.requireNonNull(getActivity()).findViewById(R.id.feed_feedback);
        Button submit =  getActivity().findViewById(R.id.feed_submit);
        submit.setOnClickListener(v -> {
            Intent sendMessage = new Intent(Intent.ACTION_SEND);
            sendMessage.putExtra(Intent.EXTRA_EMAIL,"sensemusic@sensemusic.in");
            sendMessage.putExtra(Intent.EXTRA_SUBJECT,"Feedback Sense");
            sendMessage.putExtra(Intent.EXTRA_TEXT,feedback.getText().toString());
            sendMessage.setType("message/rfc822");
            Intent chooser = Intent.createChooser(sendMessage,"Select Email Application");
            startActivity(chooser);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Feedback");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("Albums");
    }

}

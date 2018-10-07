package in.sensemusic.sense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText feedback = getActivity().findViewById(R.id.feed_feedback);
        Button submit =  getActivity().findViewById(R.id.feed_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendMessage = new Intent(Intent.ACTION_SEND);
                sendMessage.putExtra(Intent.EXTRA_EMAIL,"sensemusic@sensemusic.in");
                sendMessage.putExtra(Intent.EXTRA_SUBJECT,"Feedback Sense");
                sendMessage.putExtra(Intent.EXTRA_TEXT,feedback.getText().toString());
                sendMessage.setType("message/rfc822");
                Intent chooser = sendMessage.createChooser(sendMessage,"Select Email Application");
                startActivity(chooser);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) getActivity()).setActionBarTitle("Feedback");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("Albums");
    }

}

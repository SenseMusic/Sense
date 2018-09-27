package in.sensemusic.sense;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment {

    public static ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Called after mainactivity view created

        //Country Listview Entries Inflating
        ListView listViewCountry = (ListView) getActivity().findViewById(R.id.listview_country);
        ArrayList<String> arrayListCountry = new ArrayList<>();
        arrayListCountry.addAll(Arrays.asList(getResources().getStringArray(R.array.array_country)));

        // Use ArrayAdapter for list
        arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListCountry);

        listViewCountry.setAdapter(arrayAdapter);

        super.onActivityCreated(savedInstanceState);
    }

}

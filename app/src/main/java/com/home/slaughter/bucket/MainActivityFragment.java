package com.home.slaughter.bucket;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.andengine.engine.camera.Camera;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button button = (Button)getActivity().findViewById(R.id.startButton);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }


}

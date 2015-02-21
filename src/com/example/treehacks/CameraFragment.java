package com.example.treehacks;

import android.app.Activity;
import android.app.Fragment;

public class CameraFragment extends Fragment {
    public static final String TAG = "CameraFragment";

    private CameraFragmentListener listener;

    /**
     * On activity getting attached.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof CameraFragmentListener)) {
            throw new IllegalArgumentException(
                "Activity has to implement CameraFragmentListener interface"
            );
        }

        listener = (CameraFragmentListener) activity;
    }
}
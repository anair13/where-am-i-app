package com.example.treehacks;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


@SuppressWarnings("deprecation")
public class CameraActivity extends FragmentActivity {
	
	 public static FragmentManager fragmentManager;

	 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);
	    fragmentManager = getSupportFragmentManager();

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new CameraFragment())
                    .commit();
        }
    }
}
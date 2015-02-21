package com.example.treehacks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class CameraActivity extends Activity implements SensorEventListener {

    protected static final String TAG = null;
	private Camera mCamera;
    private CameraPreview mPreview;
    private SensorManager mSensorManager;
    private Sensor mCompass;
    private TextView mTextView;
	private static float heading;
	private static Location gps;
   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	// Gets Location
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
            	CameraActivity.gps = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mTextView = (TextView) findViewById(R.id.heading);
        
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        final PictureCallback mLearning = new PictureCallback() {
   
            /* TODO:
             * When Learning button is pressed, this method creates the .jpg
             * (it should POST the image along with the coordinates/direction).
             */
        	@Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

                if (pictureFile == null){
                    Log.d(TAG, "Error creating media file, check storage permissions");
                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                	Toast.makeText(getApplicationContext(),
								   "LEARNING-- Heading: "+ CameraActivity.heading +", Coords: "+ CameraActivity.gps.toString(),
								   Toast.LENGTH_LONG).show();
                    // post_image_for learning(picture, gps)
                	mCamera.startPreview();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        };
        
        final PictureCallback mPicture = new PictureCallback() {

            /* TODO:
             * When Picture button is pressed, this method creates the .jpg
             * (it should POST the image and get back the estimated location
             * and bring up a map or something).
             */
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null){
                    Log.d(TAG, "Error creating media file, check storage permissions");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    // post_image_and get_response(picture)
                    // bring_up_map()
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        };
        
        // Sets onClick handlers for buttons
        Button learningButton = (Button) findViewById(R.id.button_learning);
        learningButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mLearning);
                }
            }
        );
        
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        );
        
        // Brings buttons to front
        RelativeLayout learningLayout = (RelativeLayout) findViewById(R.id.button_learning_layout);
        learningLayout.bringToFront();
 
        RelativeLayout captureLayout = (RelativeLayout) findViewById(R.id.button_capture_layout);
        captureLayout.bringToFront();
        
        RelativeLayout headingLayout = (RelativeLayout) findViewById(R.id.heading_layout);
        headingLayout.bringToFront();
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    
    }
    
	 // The following method is required by the SensorEventListener interface;
	 // Hook this event to process updates;
	 public void onSensorChanged(SensorEvent event) {
	     float azimuth = Math.round(event.values[0]);
	     // The other values provided are: 
	     //  float pitch = event.values[1];
	     //  float roll = event.values[2];
	     mTextView.setText("Azimuth: " + Float.toString(azimuth));
	     CameraActivity.heading = azimuth;
	 }
	
	 @Override
	 protected void onPause() {
	     // Unregister the listener on the onPause() event to preserve battery life;
	     super.onPause();
	     mSensorManager.unregisterListener(this);
	 }
	
	 @Override
	 protected void onResume() {
	     super.onResume();
	     mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
	 }
	 
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
          return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "TreeHacks");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("TreeHacks", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
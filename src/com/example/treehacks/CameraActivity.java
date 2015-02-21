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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class CameraActivity extends FragmentActivity {
	
	 public static FragmentManager fragmentManager;

	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_camera);
		    fragmentManager = getSupportFragmentManager();

<<<<<<< Updated upstream
	        if (null == savedInstanceState) {
	            getFragmentManager().beginTransaction()
	                    .replace(R.id.container, new CameraFragment())
	                    .commit();
	        }
	    }
	 
=======
    // for posting online
    // should connection be established right away
    HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
    con.setRequestMethod("POST");
    con.setDoInput(true);
    con.setDoOutput(true);
    con.connect();

    private String delimiter = "--";
    private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";
    private String url = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
>>>>>>> Stashed changes


<<<<<<< Updated upstream
=======
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
                    // post_image_for learning(picture, gps)
                    // reset camera
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
                    connectForMultipart();
                    // bring_up_map()
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        };
        
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
        
        RelativeLayout learningLayout = (RelativeLayout) findViewById(R.id.button_learning_layout);
        learningLayout.bringToFront();
 
        RelativeLayout captureLayout = (RelativeLayout) findViewById(R.id.button_capture_layout);
        captureLayout.bringToFront();
        
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
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
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
    public void connectForMultipart() throws Exception {
        con = (HttpURLConnection) (new URL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.connect();
        os = con.getOutputStream();
    }
    // not sure if this is purely for sending text or for the name
    public void addFormPart(String paramName, String value) throws Exception {
        writeParamData(paramName, value);
    }

    private void writeParamData(String paramName, String value) throws Exception {
        os.write( (delimiter + boundary + "\r\n").getBytes());
        os.write( "Content-Type: text/plain\r\n".getBytes());
        os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
        os.write( ("\r\n" + value + "\r\n").getBytes());
    }
    //

>>>>>>> Stashed changes
}
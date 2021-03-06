package uoft.p3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PictureMode extends ActionBarActivity implements SensorEventListener {

    private static String myLocation;
    private boolean safeFlag;

    private Camera mCamera;
    private Preview mPreview;
    private Camera.PictureCallback mPicture;
    private Button capture, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    // Below is for shake
    private SensorManager sensorMgr;
    private static final int MOV_COUNTS = 2;
    private static final int MOV_THRESHOLD = 4;
    private static final float ALPHA = 0.8F;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 500; // milliseconds
    public GlobalClass globalVariable;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    double mLongitude;
    double mLatitude;

   // private String myLocation;

    // location manager

    // Gravity force on x,y,z axis
    private float gravity[] = new float[3];

    private int counter;
    private long firstMovTime;
    private ShakeListener listener;
    private Sensor s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_mode);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        safeFlag = true;
        globalVariable = new GlobalClass();
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation("address",
                getApplicationContext(), new GeocoderHandler());
        locationAddress.getAddressFromLocation("address",
                getApplicationContext(), new GeocoderHandler());
        initialize();
        if (mCamera == null) {
            //if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
        }
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMgr.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            //if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        mPreview = new Preview(myContext, mCamera);
        cameraPreview.addView(mPreview);
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the number of cameras
            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                //release the old camera instance
                //switch camera, from the front and the back and vice versa

                releaseCamera();
                chooseCamera();
            } else {
                Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //make a new picture file
                File pictureFile = getOutputMediaFile();
              //  new SaveImageTask().execute(data);
                if (pictureFile == null) {
                    return;
                }
                try {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    Toast toast = Toast.makeText(myContext, "Picture saved: " + pictureFile.getName(), Toast.LENGTH_LONG);
                    toast.show();

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
                //refresh camera to continue preview
                mPreview.refreshCamera(mCamera);
                safeFlag = true;
            }
        };
        return picture;
    }

    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         //   mCamera.takePicture(null, null, mPicture);
        }
    };

    //make picture and save to a folder
    private static File getOutputMediaFile() {
        //make a new file directory inside the "sdcard" folder
        File sdCard = Environment.getExternalStorageDirectory();
        File mediaStorageDir = new File(sdCard.getAbsolutePath() + "/wuyueCam");
        mediaStorageDir.mkdirs();
        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            //if you cannot make this folder return
            if (!mediaStorageDir.mkdirs()) {
                Log.d("SwA", " cannot save ");
                return null;
            }
        }

        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //and make a media file:
        String fileName = String.format("%s", myLocation);
        //String fileName = String.format("%s", timeStamp);
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + fileName + ".jpg");
        return mediaFile;
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float maxAcc = calcMaxAcceleration(event);
        Log.d("SwA", "Max Acc [" + maxAcc + "]");
        if (maxAcc >= MOV_THRESHOLD) {
             if (counter == 0) {
                counter++;
                firstMovTime = System.currentTimeMillis();
                Log.d("SwA", "First mov..");
            } else {
                 Handler handler = new Handler();
                 handler.postDelayed(new Runnable() {
                     public GeocodingLocation locationAddress;

                     public void run() {
                         if(safeFlag == true) {

                             locationAddress.getAddressFromLocation("address",
                                     getApplicationContext(), new GeocoderHandler());
                             mCamera.takePicture(null, null, mPicture);
                             // Todo : get the longitude and latitude

                             Log.d("SwA", "capture a picture ");
                             safeFlag=false;
                         }
                     }
                 }, 4000);
             }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private float calcGravityForce(float currentVal, int index) {
        return  ALPHA * gravity[index] + (1 - ALPHA) * currentVal;
    }
    private float calcMaxAcceleration(SensorEvent event) {
        gravity[0] = calcGravityForce(event.values[0], 0);
        gravity[1] = calcGravityForce(event.values[1], 1);
        gravity[2] = calcGravityForce(event.values[2], 2);

        float accX = event.values[0] - gravity[0];
        float accY = event.values[1] - gravity[1];
        float accZ = event.values[2] - gravity[2];
        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }
    public static interface ShakeListener {
        public void onShake();
    }
    private void resetAllData() {
        Log.d("SwA", "Reset all data");
        counter = 0;
        firstMovTime = System.currentTimeMillis();
    }
    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {
        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;
            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/wuyueCam");
                dir.mkdirs();
                String fileName = String.format("%s.jpg", myLocation);
                File outFile = new File(dir, fileName);
                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();
                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }
    }
    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress = null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    globalVariable.addLocation(locationAddress);
                    break;
                default:
                    bundle = message.getData();
                    //locationAddress = bundle.getString("address");
            }
            myLocation = locationAddress;
        }
    }

}



package com.example.omika.realityscanner;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class MainScreen extends Fragment {
    public MainScreen() {
        // Required empty public constructor
    }
    Context context;

    TextureView textureView;
    TextureView.SurfaceTextureListener surfaceTextureListener;






    private static final int RequestPermissions=1;


    private static final SparseIntArray orientaion=new SparseIntArray();
    static {
        orientaion.append(Surface.ROTATION_0,90);
        orientaion.append(Surface.ROTATION_90,0);
        orientaion.append(Surface.ROTATION_180,270);
        orientaion.append(Surface.ROTATION_270,180);
    }

    private String cameraId;
    private CameraCaptureSession cameraCaptureSession;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureBuilder;
    private Size imageDimensions;
    private ImageReader imageReader;
    CameraDevice.StateCallback stateCallback;

    private File file;
    private boolean hasFlash;
    private Handler BackgroundHandler;
    private HandlerThread BackgroundHandlerThread;



    private FloatingActionButton captureButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Camera interface to allow the image capturing.

        context=getActivity();

        View rootview= inflater.inflate(R.layout.fragment_main_screen, container, false);


        captureButton=rootview.findViewById(R.id.captureButton);

        textureView=rootview.findViewById(R.id.textureview);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });




        stateCallback =new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                cameraDevice=camera;
                createCameraPreview();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

                camera.close();
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

                camera.close();
            }
        };

        surfaceTextureListener=new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                getCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };

        textureView.setSurfaceTextureListener(surfaceTextureListener);



        return rootview;
    }



    //This method will call the API.
    private void getCamera() {


        try {
            CameraManager manager=(CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            cameraId=manager.getCameraIdList()[0];
            CameraCharacteristics cameraCharacteristics=manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap streamConfigurationMap=cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert streamConfigurationMap!=null;

            imageDimensions=streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];


            //Ask the required permissions.
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },RequestPermissions);

                return;
            }
            manager.openCamera(cameraId,stateCallback,null);



        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    //This method will allow the user to capture the image.
    private void takePicture() {
        if(cameraDevice == null)
            return;
        CameraManager manager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes=null;
            if(characteristics != null)
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

            //Capture image with custom size
            int width = 640;
            int height = 480;
            if(jpegSizes != null && jpegSizes.length > 0)
            {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Check orientation base on device
            int rotation =((AppCompatActivity)getActivity()). getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,orientaion.get(rotation));

            //Now create a storage path to save the image.
            String path= Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DCIM+"/";
            File directory=new File(path,"Reality Scanner");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Log.d("Reality Scanner", "failed to create directory");
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            file=new File(directory,"ImageName"+"_"+ timeStamp+".jpeg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);

                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                        {
                            if(image != null)
                                image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream outputStream = null;
                    try{
                        outputStream = new FileOutputStream(file);
                        outputStream.write(bytes);
                    }finally {
                        if(outputStream != null)
                            outputStream.close();
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener,BackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(context, "Saved "+file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.capture(captureBuilder.build(),captureListener,BackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            },BackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //This method will create a preview to show what's the camera pointing to.
    private void createCameraPreview() {


        try {
            SurfaceTexture surfaceTexture=textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(imageDimensions.getWidth(),imageDimensions.getHeight());
            Surface surface=new Surface(surfaceTexture);
            captureBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice==null){
                        return;
                    }
                    else {
                        cameraCaptureSession=session;
                        updatePreview();

                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //This method will update the preview if any changes take place with the camera device.
    private void updatePreview() {

        if (cameraDevice==null){
            return;
        }
        captureBuilder.set(CaptureRequest.CONTROL_MODE,CameraMetadata.CONTROL_MODE_AUTO);

        try {
            cameraCaptureSession.setRepeatingRequest(captureBuilder.build(),null,BackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==RequestPermissions){
            if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"This app cannot run without the required permissions.",Toast.LENGTH_SHORT);

            }
        }
    }


    private void startBackgroundThread() {

        BackgroundHandlerThread=new HandlerThread("Background thread for Main screen");
        BackgroundHandlerThread.start();
        BackgroundHandler=new Handler(BackgroundHandlerThread.getLooper());

    }
    private void stopBackgroundThread() {
        BackgroundHandlerThread.quitSafely();

        try {
            BackgroundHandlerThread.join();
            BackgroundHandler=null;
            BackgroundHandlerThread=null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        startBackgroundThread();
        if (textureView.isAvailable()){
            getCamera();
        }
        else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }



    }




    @Override
    public void onPause() {
        super.onPause();

        stopBackgroundThread();
    }


}

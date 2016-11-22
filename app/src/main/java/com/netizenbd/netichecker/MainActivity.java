package com.netizenbd.netichecker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.netizenbd.netichecker.sqlitedatabase.DataEntity;
import com.netizenbd.netichecker.sqlitedatabase.DataService;
import com.netizenbd.netichecker.sqlitedatabase.MyDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SurfaceView cameraView;
    TextView textView_showInfo;
    Button button_submit, button_eventManage;
    MyDbHelper myDbHelper;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    String qrData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        /**
         * Permission for marshmallow
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            createPermissions();
//        }
//        public void createPermissions(){
//            String permission = Manifest.permission.READ_SMS;
//            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED){
//                if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
//                    requestPermissions(new String[]{permission}),
//                            SMS_PERMISSION);
//                }
//            }
//        }

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        textView_showInfo = (TextView) findViewById(R.id.code_info);
        button_submit = (Button) findViewById(R.id.button_list);
        button_eventManage = (Button) findViewById(R.id.button_reset);
        button_submit.setOnClickListener(this);
        button_eventManage.setOnClickListener(this);

        myDbHelper = new MyDbHelper(this);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(400, 400)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //  int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    textView_showInfo.post(new Runnable() { // Use the post method of the TextView; receiveDetections does not run on the UI thread
                        public void run() {

                            // get qr data
                            String sQrData = barcodes.valueAt(0).displayValue;

                            /**
                             * If same data get again, then compare and use one time
                             */
                            if (qrData.equals(sQrData)) {
                                // This part will run again and again while reading same qr

                            } else {
                                // This part will run one time while reading same qr

                                String allData = "";
                                try {
                                    JSONObject json = null;
                                    json = new JSONObject(sQrData);

                                    allData = "Event:  " + json.getString("eventname") + "\n" + "Name: " + json.getString("name") +
                                            "\nArea:    " + json.getString("area") + "\nPhone: " + json.getString("phone");

                                } catch (JSONException e) {
                                    allData = sQrData;
                                    Toast.makeText(MainActivity.this, "QR Not Valid! - (Show Part)", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

//                                textView_showInfo.setText(barcodes.valueAt(0).displayValue); // Update the TextView
                                textView_showInfo.setText(allData); // Update the TextView
                                saveToSqlite(barcodes.valueAt(0).displayValue); // Save to db
                            }
                            // Keep qr data in a string to check data are same or not
                            qrData = barcodes.valueAt(0).displayValue;
                        }
                    });
                }
            }
        });


    } // End of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_list:
                startActivity(new Intent(getApplicationContext(), ParticipantList.class));
                break;

            case R.id.button_reset:
                // reset qr check value and set default text
                qrData = "";
                textView_showInfo.setText("Reading QR ...");
                break;
        }
    }

    private void saveToSqlite(String qrData) {

        boolean dataService = false;
        String eventID = "";
        String eventName = "";
        String participantID = "";
        String participantName = "";
        String participantPhone = "";
        String participantArea = "";

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());
        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        //  String dateTime = sdf.format(dt);
        String dateTime = currentTimestamp.toString();
        System.out.println("touhiddd: " + dateTime);


        try {

            JSONObject json = null;
            json = new JSONObject(qrData);
            eventID = json.getString("eventid");
            eventName = json.getString("eventname");
            participantID = json.getString("participantid");
            participantName = json.getString("name");
            participantPhone = json.getString("phone");
            participantArea = json.getString("area");

            dataService = new DataService().insertData(MainActivity.this, new DataEntity(
                    eventID,
                    eventName,
                    participantID,
                    participantName,
                    participantPhone,
                    participantArea
            ));

        } catch (JSONException e) {
            Toast.makeText(this, "QR Not Valid! - (Save Part)", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (dataService) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }

    }

}

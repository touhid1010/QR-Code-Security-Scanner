package com.netizenbd.netichecker;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.netizenbd.netichecker.fragments.SMS;
import com.netizenbd.netichecker.sqlitedatabase.DataEntity;
import com.netizenbd.netichecker.sqlitedatabase.DataService;
import com.netizenbd.netichecker.sqlitedatabase.MyDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SMS.OnFragmentInteractionListener {

    SurfaceView cameraView;
    TextView textView_showInfo;
    MyDbHelper myDbHelper;
    DataService dataService;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    String tempQrData = "";
    MySendSMS mySendSMS;
    CheckBox checkbox_sms;
    FrameLayout frameLayout_forFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Neti QR Checker");

        // fab
        FloatingActionButton fab_reset = (FloatingActionButton) findViewById(R.id.fab_reset);
        fab_reset.setOnClickListener(this);

        // drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // initialization
        frameLayout_forFragment = (FrameLayout) findViewById(R.id.frameLayout_forFragment);

        // Fragment
        manager = getSupportFragmentManager();



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


        // sms
        mySendSMS = new MySendSMS(this);
        checkbox_sms = (CheckBox) findViewById(R.id.checkbox_sms);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        textView_showInfo = (TextView) findViewById(R.id.code_info);


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
                            if (tempQrData.equals(sQrData)) {
                                // This part will run again and again while reading same qr

                            } else {
                                // This part will run one time while reading same qr

                                String allData = "";
                                String pName = "";
                                String pPhone = "";
                                try {
                                    JSONObject json = null;
                                    json = new JSONObject(sQrData);

                                    pName = json.getString("name");
                                    pPhone = json.getString("phone");

                                    allData = "Event:  " + json.getString("eventname") + "\n" + "Name: " + pName + " (" + json.getString("participanttype") + ")" +
                                            "\nArea:    " + json.getString("area") + "\nPhone: " + pPhone;

                                } catch (JSONException e) {
                                    allData = sQrData;
                                    Toast.makeText(MainActivity.this, "QR Not Valid! - (Show Part)", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

//                                textView_showInfo.setText(barcodes.valueAt(0).displayValue); // Update the TextView
                                textView_showInfo.setText(allData); // Update the TextView
                                saveToSqlite(sQrData); // Save to db

                            }
                            // Keep qr data in a string to check data are same or not
                            tempQrData = barcodes.valueAt(0).displayValue;
                        }
                    });
                }
            }
        });


    } // end of onCreate

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
              frameLayout_forFragment.removeAllViews();
        } else if (id == R.id.nav_participant_list) {
            startActivity(new Intent(this, ParticipantList.class));
        } else if (id == R.id.nav_sms) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.frameLayout_forFragment, new SMS()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_reset:
                // reset qr check value and set default text
                tempQrData = "";
                textView_showInfo.setText("Reading QR ...");
                Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    /**
     * Save method to save sqlite db
     *
     * @param qrData
     */
    private void saveToSqlite(String qrData) {

        boolean dataService = false;
        String eventID = "";
        String eventName = "";
        String participantID = "";
        String participateType = "";
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

        String pName = "";
        String pPhone = "";

        try {

            JSONObject json = null;
            json = new JSONObject(qrData);
            eventID = json.getString("eventid");
            eventName = json.getString("eventname");
            participantID = json.getString("participantid");
            participateType = json.getString("participanttype");
            participantName = json.getString("name");
            participantPhone = json.getString("phone");
            participantArea = json.getString("area");

            // for sms
            pName = json.getString("name");
            pPhone = json.getString("phone");

            dataService = new DataService(MainActivity.this).insertData(MainActivity.this, new DataEntity(
                    eventID,
                    eventName,
                    participantID,
                    participateType,
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

            /**
             * Send sms if successfully save data
             */
            if (checkbox_sms.isChecked()) {
                String smsBody = "Hello " + pName + ". Thank you for participating. -- Netizen IT Limited.";
                mySendSMS.sendMySMS(pPhone, smsBody);
            }

        } else {
            Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

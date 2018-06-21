package org.androidtown.babyhealthcare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.babyhealthcare.Activity.WebActivity;
import org.androidtown.babyhealthcare.Const.IntentConst;

public class HomeActivity extends BlunoLibrary implements AppCompatCallback {

    private static final int REQUEST_LOCATION_PERMISSIONS = 2;
    private static final String WEB_PAGE_URL = "https://www.naver.com/";

    private Button buttonScan;
    private Button buttonSerialSend;
    private EditText serialSendText;
    private TextView serialReceivedText;

    private AppCompatDelegate delegate;


    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        //let's leave this empty, for now
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.my_awesome_toolbar);
        delegate.setSupportActionBar(toolbar);

        onCreateProcess();                                                        //onCreate Process by BlunoLibrary

        // TODO: 2018-06-20 블루투스 스캔을 위한 앱 권한 요청 
        int permissionCoarse = Build.VERSION.SDK_INT >= 24 ? ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) :
                PackageManager.PERMISSION_GRANTED;

        if (permissionCoarse == PackageManager.PERMISSION_GRANTED) {
            //scanLeDevice(true);
            Toast.makeText(this, "블루투스 스캔 권한 허가", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "블루투스 스캔 권한 요청", Toast.LENGTH_SHORT).show();
            askForCoarseLocationPermission();
        }

        serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200

        serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);    //initial the EditText of the received data
        serialSendText = (EditText) findViewById(R.id.serialSendText);            //initial the EditText of the sending data
        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);        //initial the button for sending the data
        buttonScan = (Button) findViewById(R.id.buttonScan);                    //initial the button for scanning the BLE device

        Button buttonAmbulance = (Button)findViewById(R.id.button_ambulance);
        Button buttonInternet = (Button)findViewById(R.id.button_internet);


        buttonSerialSend.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            serialSend(serialSendText.getText().toString());                //send the data to the BLUNO
        });

        buttonScan.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device
        });


        buttonAmbulance.setOnClickListener(v -> {
            // TODO: 2018-06-20 앰뷸런스 버튼 누르면 실행할 코드 넣으면됩니다.

        });

        buttonInternet.setOnClickListener(v -> {
            // TODO: 2018-06-20 인터넷 버튼 누르면 실행할 코드 넣으면됩니다.
            Intent intent = new Intent(HomeActivity.this, WebActivity.class);
            intent.putExtra(IntentConst.WEB_URL, WEB_PAGE_URL);
            startActivity(intent);

        });


    }

    protected void onResume() {
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();                                                        //onResume Process by BlunoLibrary
    }


    private void askForCoarseLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //scanLeDevice(true);
                    Toast.makeText(this, "블루투스 스캔 권한 허가", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);                    //onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();                                                        //onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();                                                        //onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {                                            //Four connection state
            case isConnected:
                buttonScan.setText("Connected");
                break;
            case isConnecting:
                buttonScan.setText("Connecting");
                break;
            case isToScan:
                buttonScan.setText("Scan");
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {                            //Once connection data received, this function will be called
        // TODO Auto-generated method stub
        serialReceivedText.append(theString);                            //append the text into the EditText
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        ((ScrollView) serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }

}
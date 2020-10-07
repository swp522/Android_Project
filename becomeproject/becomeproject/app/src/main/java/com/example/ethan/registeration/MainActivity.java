package com.example.palmtree.become;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;

    private TextView tvId;

    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvId = (TextView) findViewById(R.id.tvId);

        beaconManager = new BeaconManager(this);

        // add this below:
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    Log.d("Dankook Uni", "Nearest places: " + nearestBeacon.getRssi());

                    int u_val = nearestBeacon.getRssi();

//                    long now = System.currentTimeMillis();
//                    Date date = new Date(now);
//                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                    String formatDate = sdfNow.format(date);

//                    final int date_arr = 0;

                    final long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    final String formatDate = sdfNow.format(date);

//                    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
//                    Date time = new Date();
//                    String time1 = format1.format(time);

                    System.out.println("!!!!!!!!!"+formatDate);

                    // nearestBeacon.getRssi() : 비콘의 수신 강도
                    tvId.setText(nearestBeacon.getRssi() + "");

                    if ( !isConnected && nearestBeacon.getRssi() > -70 ) {
                        isConnected = true;

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("알림")
                                .setMessage("비콘 연결")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show(); // 코드test 원래 연결종료 였음
                    }
                }
            }
        });

        region = new Region("ranged region",
                UUID.fromString("74278BDA-B644-4520-8F0C-720EAF059935"), 30001, 24402); // 본인이 연결할 Beacon의 ID와 Major / Minor Code를 알아야 한다.
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 블루투스 권한 승낙 및 블루투스 활성화
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        //beaconManager.stopRanging(region);

        super.onPause();
    }
}

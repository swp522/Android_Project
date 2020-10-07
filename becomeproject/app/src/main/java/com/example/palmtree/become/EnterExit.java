package com.example.palmtree.become;

import android.app.Application;

import android.app.Notification;

import android.app.NotificationManager;

import android.app.PendingIntent;

import android.content.Context;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.Beacon;

import com.estimote.sdk.BeaconManager;

import com.estimote.sdk.Region;


import org.json.JSONObject;

import java.util.List;

import java.util.UUID;

public class EnterExit extends Application {

    private BeaconManager beaconManager;
    private AlertDialog dialog;
    /**
     * Application을 설치할 때 실행됨.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());

        // Application 설치가 끝나면 Beacon Monitoring Service를 시작한다.
        // Application을 종료하더라도 Service가 계속 실행된다.
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("74278BDA-B644-4520-8F0C-720EAF059935"), // 본인이 연결할 Beacon의 ID와 Major / Minor Code를 알아야 한다.
                        30001, 24402));
            }
        });

        // Android 단말이 Beacon 의 송신 범위에 들어가거나, 나왔을 때를 체크한다.
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                AudioManager mAudioManager;
                mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                showNotification("들어옴", "비콘 연결됨" + list.get(0).getRssi());
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    //php 백단에 접속한 이후에 특정 json response를 받을 수 있게 하는 부분
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(EnterExit.this);
                                dialog = builder.setMessage("입장.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(EnterExit.this);
                                dialog = builder.setMessage("에러!")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                // 백단에 실질적으로 접속을 할 수 있게 해주는 부분
                int intRssi = list.get(0).getRssi();
                String rssiVal = String.valueOf(intRssi);
                String sessionId = SharedPreference.getInstance().getValue(EnterExit.this,"session_id","");
                Log.d("sessionId", "valuetest: "+ sessionId);

                EnterExitRequest enterExitRequest = new EnterExitRequest(sessionId,rssiVal, responseListener);
                RequestQueue queue = Volley.newRequestQueue(EnterExit.this);
                queue.add(enterExitRequest);
            }

            //퇴장시에는 rssi 값이 없는상태이므로
            @Override
            public void onExitedRegion(Region region) {
                AudioManager mAudioManager;
                mAudioManager  = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                showNotification("나감", "비콘 연결끊김");
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        });
    }

    /**
     * Notification으로 Beacon 의 신호가 연결되거나 끊겼음을 알림.
     * @param title
     * @param message
     */
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, LoginActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}


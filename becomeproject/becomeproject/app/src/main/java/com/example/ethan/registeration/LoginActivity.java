package com.example.palmtree.become;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private String userID;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);


        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();

                //빈 공간이 있으면 회원등록을 할 수 없도록 하는 로직
                if(userID.equals("") || userPassword.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("빈 항목이 없는지 확인해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    //php 백단에 접속한 이후에 특정 json response를 받을 수 있게 하는 부분
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage(userID+"님 반갑습니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                if(dialog != null){

                                    dialog.dismiss();
                                    dialog = null;
                                }

                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(loginIntent);

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("아이디나 비밀번호를 다시 한번 확인해주세요.")
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
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

    }

    protected void onStop(){
        super.onStop();
        if(dialog != null){

            dialog.dismiss();
            dialog = null;
        }

    }
}

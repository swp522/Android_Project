package com.example.palmtree.become;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private String userID;
    private String userPassword;
    private String userGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

//        RadioGroup genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
//        int genderGroupID = genderGroup.getCheckedRadioButtonId();
//        userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();
//
//        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int i) {
//                RadioButton genderButton = (RadioButton) findViewById(i);
//                userGender = genderButton.getText().toString();
//            }
//        });

        // 1.
        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        // 여기까지 회원가입 하러가는 button


        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();

                //
                RadioGroup genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
                int genderGroupID = genderGroup.getCheckedRadioButtonId();
                userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();
                //
                genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int i) {
                        RadioButton genderButton = (RadioButton) findViewById(i);
                        userGender = genderButton.getText().toString();
                    }
                });

                if(userID.equals("") || userPassword.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("빈 항목이 없는지 확인해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }   //로그인 시 빈칸 있는지 확인

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            userGender = jsonResponse.getString("userGender");
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

//                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
//                                LoginActivity.this.startActivity(loginIntent);

                                if(userGender.equals("학생")) {
                                    Intent loginIntent = new Intent(LoginActivity.this, stMainActivity.class);
                                    LoginActivity.this.startActivity(loginIntent);
                                }
                                else if(userGender.equals("교수")){
                                    Intent loginIntent = new Intent(LoginActivity.this, prMainActivity.class);
                                    LoginActivity.this.startActivity(loginIntent);
                                }
                            }//로그인 성공하고 화면전환까지


                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("아이디나 비밀번호를 다시 한번 확인해주세요.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                            }   //로그인 실패했을 경우
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                // 백단에 실질적으로 접속을 할 수 있게 해주는 부분
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, userGender, responseListener);
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

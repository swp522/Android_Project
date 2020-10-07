package com.example.palmtree.become;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //UserRegister.php 파일에 parameter들을 보내서 회원가입 시켜라 라고 요청을 보내는 클래스임

    final static private String URL = "http://52.78.116.177/UserRegister.php";


    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userGender, String userMajor, String userEmail, Response.Listener<String> listener){
        super(Method.POST , URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userGender", userGender);
        parameters.put("userMajor", userMajor);
        parameters.put("userEmail", userEmail);

    }

    public Map<String, String> getParams(){
        return parameters;
    }

}


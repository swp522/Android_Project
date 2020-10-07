package com.example.palmtree.become;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EnterExitRequest extends StringRequest {

    //UserRegister.php 파일에 parameter들을 보내서 회원가입 시켜라 라고 요청을 보내는 클래스임

    final static private String URL = "http://15.165.61.147/UserEnterExit.php";


    private Map<String, String> parameters;
    private String sessionId;
    public EnterExitRequest(String sessionId, String rssiVal, Response.Listener<String> listener){
        super(Method.POST , URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("rssiVal", rssiVal);
//        parameters.put("userID", userID);
        this.sessionId = sessionId;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        super.getHeaders().put("cookie",sessionId);
        return super.getHeaders();
    }

    public Map<String, String> getParams(){
        return parameters;
    }
}

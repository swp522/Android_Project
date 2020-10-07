package com.example.palmtree.become;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    //userID를 UserRegister.php에 보냄으로써 회원가입이 가능한 아이디인지 요청을 보내는 클래스

    final static private String URL = "http://15.165.61.147/UserValidate.php";

    private Map<String, String> parameters;

    public ValidateRequest(String userID, Response.Listener<String> listener){
        super(Method.POST , URL, listener,null);
        parameters = new HashMap();
        parameters.put("userID", userID);
    }

    public Map<String, String> getParams(){
        return parameters;
    }

}

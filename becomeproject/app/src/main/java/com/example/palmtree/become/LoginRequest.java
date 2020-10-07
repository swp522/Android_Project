package com.example.palmtree.become;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://15.165.61.147/UserLogin.php";

    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, String userGender, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userGender", userGender);
    }

    @Override
    public Map<String, String> getParams() {     //이건 어디 쓰인거지..?
        return parameters;
    }


//    @Override
//    protected Response<String> parseNetworkResponse(NetworkResponse response) {
//        // since we don't know which of the two underlying network vehicles
//        // will Volley use, we have to handle and store session cookies manually
//        MyApp.get().checkSessionCookie(response.headers);
//
//        return super.parseNetworkResponse(response);
//    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> headers = super.getHeaders();
//
//        if (headers == null
//                || headers.equals(Collections.emptyMap())) {
//            headers = new HashMap<String, String>();
//        }
//
//        MyApp.get().addSessionCookie(headers);
//
//        return headers;
//    }
}

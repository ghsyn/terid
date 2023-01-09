package com.example.terid_off;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static private String URL="http://terid.dothome.co.kr/UserValidate.php";
    private Map<String,String> parameters;

    public ValidateRequest(String userID, Response.Listener<String> listener){
        super(Request.Method.POST,URL,listener,null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
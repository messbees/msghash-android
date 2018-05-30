package com.gotowork.msghash;

import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPParser {
    public HTTPParser() {

    }

    public void sendRequest(byte[] bytes, String urlRequest, int port) throws IOException {
        StringBuilder parameters = new StringBuilder();
        try {
            parameters.append("file1=");
            parameters.append(URLEncoder.encode(new String(bytes), "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {

        }
        URL url = new URL(urlRequest);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset","UTF-8");
        connection.setRequestProperty("Content-Length",Integer.toString(parameters.toString().getBytes().length));

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(parameters.toString());
        wr.flush();
        wr.close();
        connection.disconnect();
    }

}
//TODO: port
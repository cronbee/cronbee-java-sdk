package com.aliengen.cronbee.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class HttpClient {

    private static String BASE_URL = "https://api.cronbee.com/monitor/";

    protected String uuid;
    protected String token;

    public HttpClient(String uuid) {
        this.uuid = uuid;
    }

    public boolean ping() {
        System.out.println("Receive ping! " + this.uuid);
        try {
            this.query("GET", BASE_URL + this.uuid + "/ping", null, null);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean start() {
        System.out.println("Receive start! " + this.uuid);
        try {
            this.token = this.query("GET", BASE_URL + this.uuid, null, null);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean event(String event) {
        return this.event(event, null);
    }

    public boolean event(String event, String eventMessage) {
        return this.event(event, eventMessage, null);
    }

    public boolean event(String event, String eventMessage, String eventData) {
        System.out.println("Received event: " + event + " "  + this.uuid);
        try {
            this.query("POST", BASE_URL + this.uuid + "/event/" + URLEncoder.encode(event, "UTF-8") + "?token=" + this.token, eventMessage, eventData);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private String query(String method, String url, String eventMessage, String eventData) throws NoSuchAlgorithmException, IOException {
        URL urlObject = new URL(url);
        SSLContext ctx = SSLContext.getInstance("TLS");
        SSLContext.setDefault(ctx);

        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();

        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Cronbee Java client 0.1)");

        if(eventMessage != null) {
            String query = "event_message=" + URLEncoder.encode(eventMessage, "UTF-8");
            if (eventData != null) {
                query += "&";
                query += "event_data=" + URLEncoder.encode(eventData, "UTF-8");
            }

            connection.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(query);
            output.close();
        }

        int responseCode = connection.getResponseCode();
        if(responseCode != 200) {
            throw new IOException("Invalid response code from Cronbee: " + responseCode);
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}

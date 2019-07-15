package com.aliengen.cronbee;

import com.aliengen.cronbee.util.HttpClient;

public class CronbeePipeline {

    private HttpClient client;

    public CronbeePipeline(HttpClient client) {
        this.client = client;
    }

    public boolean event(String event) {
        if (client == null) {
            return false;
        }
        return client.event(event);
    }

    public boolean error(String error_message, String error_data) {
        if (client == null) {
            return false;
        }
        return client.event("error", error_message, error_data);
    }

    public boolean stop() {
        if (client == null) {
            return false;
        }
        return client.event("stop");
    }
}

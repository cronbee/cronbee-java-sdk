package com.aliengen.cronbee;

import com.aliengen.cronbee.util.HttpClient;

public class Cronbee {
    private boolean checkValidUuid(String uuid) {
        return true;
    }

    public boolean ping(String uuid) {
        if (!checkValidUuid(uuid))
            return false;

        HttpClient client = new HttpClient(uuid);
        return client.ping();
    }

    public CronbeePipeline start(String uuid) {
        if (!checkValidUuid(uuid))
            return new CronbeePipeline(null);
        HttpClient client = new HttpClient(uuid);
        client.start();
        return new CronbeePipeline(client);
    }
}

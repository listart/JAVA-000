package org.geekuni;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class Client01 {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .get()
                .url("http://localhost:8801")
                .build();

        Call call = client.newCall(request);

        try {
            Response resp = call.execute();
            if (resp.code() == 200)
                System.out.println("recv:" + Objects.requireNonNull(resp.body()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

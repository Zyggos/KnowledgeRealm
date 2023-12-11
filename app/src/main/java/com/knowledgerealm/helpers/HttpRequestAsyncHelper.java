package com.knowledgerealm.helpers;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.CompletableFuture;

/**
 * HttpRequestHelperAsync is a helper class that makes HTTP requests asynchronously.
 */
public class HttpRequestAsyncHelper {
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Makes an HTTP GET request to the specified URL asynchronously.
     *
     * @param url the URL to make the request to.
     * @return a CompletableFuture that will contain the response body as a string.
     */
    public static CompletableFuture<String> getResponseAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(url);
            } catch (IOException e) {
                Log.e("HttpRequestHelperAsync", "getStringAsync: " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * Makes an HTTP GET request to the specified URL.
     *
     * @param url the URL to make the request to.
     * @return the response body as a string.
     * @throws IOException if there was an error making the HTTP request.
     */
    private static String getResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}


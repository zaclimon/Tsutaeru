package com.zaclimon.tsutaeru.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utility class for network related tasks
 *
 * @author zaclimon
 * Creation date: 13/08/17
 */

public class NetworkUtils {

    private static final int URLCONNECTION_CONNECTION_TIMEOUT_MS = 3000;  // 3 sec
    private static final int URLCONNECTION_READ_TIMEOUT_MS = 10000;  // 10 sec

    public static InputStream getNetworkInputStream(String url) throws IOException {

        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(URLCONNECTION_CONNECTION_TIMEOUT_MS);
        urlConnection.setReadTimeout(URLCONNECTION_READ_TIMEOUT_MS);
        InputStream inputStream = urlConnection.getInputStream();

        return (inputStream == null ? null : new BufferedInputStream(inputStream));
    }

}

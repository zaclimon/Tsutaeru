package com.zaclimon.tsutaeru.util

import java.io.InputStream
import java.net.URL

class NetworkUtils {

    companion object {
        // Connection timeout is 3 seconds
        private const val URLCONNECTION_CONNECTION_TIMEOUT_MS = 3000
        // Read timeout is 10 seconds
        private const val URLCONNECTION_READ_TIMEOUT_MS = 10000

        fun getNetworkInputStream(url: String): InputStream {
            val urlConnection = URL(url).openConnection().apply {
                connectTimeout = URLCONNECTION_CONNECTION_TIMEOUT_MS
                readTimeout = URLCONNECTION_READ_TIMEOUT_MS
            }
            val inputStream = urlConnection.getInputStream()
            return inputStream.buffered()
        }
    }

}
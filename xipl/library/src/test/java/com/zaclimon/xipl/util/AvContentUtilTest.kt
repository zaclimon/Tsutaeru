package com.zaclimon.xipl.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AvContentUtilTest {

    private val validPlaylistLine = "#EXTINF:-1 tvg-id=\"test\" tvg-name=\"Test Channel\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
    private val missingNameTagPlaylistLine = "#EXTINF:-1 tvg-id=\"test\" tvg-name=\"\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
    private val missingHeaderPlaylistLine = "tvg-id=\"test\" tvg-name=\"Test Channel\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
    private val playlistUrl = "http://test.com:25461/live/hello/world/12345.ts"
    private val contentCategory = "VOD"

    @Test
    fun `Creates an AV content with a valid playlist line, URL and group`() {
        val avContent = createAvContent(validPlaylistLine, playlistUrl, contentCategory)
        assertTrue(avContent != null)
        // Verify that we were able to get channel's information
        assertTrue(avContent?.id == "test".hashCode())
        assertTrue(avContent?.title == "Test Channel")
        assertTrue(avContent?.logo == "http://logo.com/abc.jpg")
        assertTrue(avContent?.group == "TEST")
    }

    @Test
    fun `Creates an AV content with missing title in the playlist line but with valid URL and group`() {
        val avContent = createAvContent(missingNameTagPlaylistLine, playlistUrl, contentCategory)
        assertTrue(avContent != null)
        // Verify that we were able to get channel's information
        assertTrue(avContent?.id == "test".hashCode())
        assertTrue(avContent?.title == "Test Channel")
        assertTrue(avContent?.logo == "http://logo.com/abc.jpg")
        assertTrue(avContent?.group == "TEST")
    }

    @Test
    fun `Creates an empty AV content when the playlist line misses the M3U header`() {
        val avContent = createAvContent(missingHeaderPlaylistLine, playlistUrl, contentCategory)
        assertTrue(avContent == null)
    }

    @Test
    fun `Creates an empty AV content when the playlist URL is blank`() {
        val avContent = createAvContent(validPlaylistLine, "", contentCategory)
        assertTrue(avContent == null)
    }

    @Test
    fun `Creates an empty AV content when playlist group is blank`() {
        val avContent = createAvContent(validPlaylistLine, playlistUrl, "")
        assertTrue(avContent == null)
    }

}
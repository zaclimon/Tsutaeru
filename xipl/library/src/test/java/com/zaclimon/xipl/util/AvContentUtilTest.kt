package com.zaclimon.xipl.util

import com.zaclimon.xipl.model.AvContent
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AvContentUtilTest {

    private val validPlaylistLine = "#EXTINF:-1 tvg-id=\"test\" tvg-name=\"Test Channel\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
    private val missingNameTagPlaylistLine = "#EXTINF:-1 tvg-id=\"test\" tvg-name=\"\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
    private val missingHeaderPlaylistLine = "tvg-id=\"test\" tvg-name=\"Test Channel\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
    private val playlistUrl = "http://test.com:25461/live/hello/world/12345.ts"
    private val contentCategory = "VOD"
    private val validPlaylistFile = AvContentUtilTest::class.java.classLoader.getResource("valid_test_playlist_multiline.txt").readText()

    @Test
    fun `Creates an AV content with a valid playlist line, URL and group`() {
        val avContent = createAvContent(validPlaylistLine, playlistUrl, contentCategory)
        // Verify that we were able to get channel's information
        assertTrue(avContent.id == "test".hashCode())
        assertTrue(avContent.title == "Test Channel")
        assertTrue(avContent.logo == "http://logo.com/abc.jpg")
        assertTrue(avContent.group == "TEST")
    }

    @Test
    fun `Creates an AV content when the title in the playlist line is missing but with a valid URL and group`() {
        val avContent = createAvContent(missingNameTagPlaylistLine, playlistUrl, contentCategory)
        // Verify that we were able to get channel's information
        assertTrue(avContent.id == "test".hashCode())
        assertTrue(avContent.title == "Test Channel")
        assertTrue(avContent.logo == "http://logo.com/abc.jpg")
        assertTrue(avContent.group == "TEST")
    }

    @Test
    fun `Creates an empty AV content when the playlist line misses the M3U header`() {
        val avContent = createAvContent(missingHeaderPlaylistLine, playlistUrl, contentCategory)
        assertTrue(avContent == AvContent())
    }

    @Test
    fun `Creates an empty AV content when the playlist URL is blank`() {
        val avContent = createAvContent(validPlaylistLine, "", contentCategory)
        assertTrue(avContent == AvContent())
    }

    @Test
    fun `Generates a list of 5 AV contents when the playlist file is valid and each lines are distinct`() {
        val list = generateAvContentList(validPlaylistFile)
        assertTrue(list.size == 5)
        assertTrue(list[0].title == "Test Channel")
        // Be sure to get the one that doesn't have a title specified in it's own tag
        assertTrue(list[list.lastIndex].title == "Test Channel5")
    }

}
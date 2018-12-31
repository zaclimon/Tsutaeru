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
    private val validPlaylistFile = AvContentUtilTest::class.java.classLoader.getResource("valid_test_playlist_multiline.m3u").readText()
    private val validPlaylistFileMissingUrl = AvContentUtilTest::class.java.classLoader.getResource("valid_test_playlist_multiline_missingurl.m3u").readText()
    private val validPlaylistFileSingle = AvContentUtilTest::class.java.classLoader.getResource("valid_test_playlist_singleline.m3u").readText()
    private val validPlaylistFileSingleMissingUrl = AvContentUtilTest::class.java.classLoader.getResource("valid_test_playlist_singleline_missingurl.m3u").readText()

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
        assertTrue(list.first().title == "Test Channel")
        // Be sure to get the one that doesn't have a title specified in it's own tag
        assertTrue(list.last().title == "Test Channel5")
    }

    @Test
    fun `Generates a list of 4 AV contents when the playlist file is valid, each lines are distinct, but one content is missing it's URL`() {
        val list = generateAvContentList(validPlaylistFileMissingUrl)
        assertTrue(list.size == 4)
        assertTrue(list.last().title == "Test Channel4")
    }

    @Test
    fun `Generates a list of 5 AV contents when the playlist file is valid and each lines are merged`() {
        val list = generateAvContentList(validPlaylistFileSingle)
        assertTrue(list.size == 5)
        assertTrue(list.first().title == "00:00(01.01) Eyewitness News at 5")
        // Be sure to get the one that doesn't have a title specified in it's own tag
        assertTrue(list.last().title == "12:30(01.01) Outback Adventures")
    }

    @Test
    fun `Generates a list of 4 AV contents when the playlist file is valid, each lines are merged, but one content is missing it's URL`() {
        val list = generateAvContentList(validPlaylistFileSingleMissingUrl)
        assertTrue(list.size == 4)
        assertTrue(list.last().title == "12:30(01.01) Outback Adventures")
    }
}
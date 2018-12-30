package com.zaclimon.xipl.util

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AvContentUtilTest {

    private lateinit var validPlaylistLine: String
    private lateinit var missingNameTagPlaylistLine: String
    private lateinit var missingHeaderPlaylistLine: String
    private lateinit var playlistUrl: String
    private lateinit var contentCategory: String

    @Before
    fun init() {
        validPlaylistLine = "#EXTINF:-1 tvg-id=\"test\" tvg-name=\"Test Channel\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
        missingNameTagPlaylistLine = "#EXTINF:-1 tvg-id=\"test\" tvg-name=\"\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
        missingHeaderPlaylistLine = "tvg-id=\"test\" tvg-name=\"Test Channel\" tvg-logo=\"http://logo.com/abc.jpg\" group-title=\"TEST\",Test Channel"
        playlistUrl = "http://test.com:25461/live/hello/world/12345.ts"
        contentCategory = "VOD"
    }

    @Test
    fun createAvContentTest_ValidLine() {
        val avContent = createAvContent(validPlaylistLine, playlistUrl, contentCategory)
        Assert.assertTrue(avContent != null)
        // Verify that we were able to get channel's information
        Assert.assertTrue(avContent?.id == "test".hashCode())
        Assert.assertTrue(avContent?.title == "Test Channel")
        Assert.assertTrue(avContent?.logo == "http://logo.com/abc.jpg")
        Assert.assertTrue(avContent?.group == "TEST")
    }

    @Test
    fun createAvContentTest_MissingNameTag() {
        val avContent = createAvContent(missingNameTagPlaylistLine, playlistUrl, contentCategory)
        Assert.assertTrue(avContent != null)
        // Verify that we were able to get channel's information
        Assert.assertTrue(avContent?.id == "test".hashCode())
        Assert.assertTrue(avContent?.title == "Test Channel")
        Assert.assertTrue(avContent?.logo == "http://logo.com/abc.jpg")
        Assert.assertTrue(avContent?.group == "TEST")
    }

    @Test
    fun createAvContentTest_MissingHeaderLine() {
        val avContent = createAvContent(missingHeaderPlaylistLine, playlistUrl, contentCategory)
        Assert.assertTrue(avContent == null)
    }

    @Test
    fun createAvContentTest_EmptyPlaylistLink() {
        val avContent = createAvContent(validPlaylistLine, "", contentCategory)
        Assert.assertTrue(avContent == null)
    }

    @Test
    fun createAvContentTest_EmptyContentCategory() {
        val avContent = createAvContent(validPlaylistLine, playlistUrl, "")
        Assert.assertTrue(avContent == null)
    }

}
package com.zaclimon.xipl.util

import com.zaclimon.xipl.model.AvContent

private val ATTRIBUTE_TVG_ID_PATTERN = Regex("tvg-id.\"(.*?)\"", RegexOption.IGNORE_CASE)
private val ATTRIBUTE_TVG_LOGO_PATTERN = Regex("tvg-logo.\"(.*?)\"", RegexOption.IGNORE_CASE)
private val ATTRIBUTE_TVG_NAME_PATTERN = Regex("tvg-name.\"(.*?)\"", RegexOption.IGNORE_CASE)
private val ATTRIBUTE_GROUP_TITLE_PATTERN = Regex("group-title.\"(.*?)\"", RegexOption.IGNORE_CASE)
private val COMMA_PATTERN = Regex(",.*")

fun generateAvContentList(playlist: String,
                          contentCategory: String = ""): List<AvContent> {
    val playlistLines = playlist.lines()

    if (playlistLines.size > 1) {
        return parseMultiLinePlaylist(playlist, contentCategory)
    } else if (playlistLines.size == 1) {
        return parseSingleLinePlaylist(playlist, contentCategory)
    }
    return emptyList()
}

private fun parseMultiLinePlaylist(playlist: String, contentCategory: String): List<AvContent> {
    val playlistLines = playlist.lines()
    val playlistContents = mutableListOf<AvContent>()
    for (i in playlistLines.indices) {
        // Check if the playlist line has an URL below it so it can be considered a valid AVContent
        if (playlistLines[i].startsWith("#EXTINF") && (i + 1) < playlistLines.size && playlistLines[i + 1].startsWith("http://")) {
            playlistContents.add(createAvContent(playlistLines[i], playlistLines[i + 1], contentCategory))
        }
    }
    return playlistContents
}

private fun parseSingleLinePlaylist(playlist: String, contentCategory: String): List<AvContent> {
    return emptyList()
}

fun createAvContent(playlistLine: String, contentLink: String, contentCategory: String): AvContent {

    if (!playlistLine.contains("#EXTINF:-1") || contentLink.isBlank()) return AvContent()

    val id = getAttributeFromPlaylistLine(playlistLine, ATTRIBUTE_TVG_ID_PATTERN).hashCode()
    val name = getAttributeFromPlaylistLine(playlistLine, ATTRIBUTE_TVG_NAME_PATTERN)
    val logo = getAttributeFromPlaylistLine(playlistLine, ATTRIBUTE_TVG_LOGO_PATTERN)
    val group = getAttributeFromPlaylistLine(playlistLine, ATTRIBUTE_GROUP_TITLE_PATTERN)

    if (name.isBlank()) return AvContent()
    return AvContent(name, logo, group, contentCategory, contentLink, id)
}

private fun getAttributeFromPlaylistLine(playlistLine: String, attribute: Regex): String {
    val parts = attribute.find(playlistLine)?.groupValues
    parts?.let {
        val value = parts[1]
        /*
        It might be possible that the title isn't in the tvg-name tag, retrieve it from the
        region after the comma.
       */
        if (value.isBlank() && attribute == ATTRIBUTE_TVG_NAME_PATTERN) {
            val newValue = COMMA_PATTERN.find(playlistLine)?.value?.substring(1)
            if (newValue != null) return newValue
        }
        return value
    }
    return ""
}
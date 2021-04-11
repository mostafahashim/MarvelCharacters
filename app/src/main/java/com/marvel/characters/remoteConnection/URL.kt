package com.marvel.characters.remoteConnection

object URL {

    fun getCharactersUrl(): String {
        var url = "characters"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterDetailsUrl(id: Int): String {
        var url = "characters/$id"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterComicsUrl(id: Int): String {
        var url = "characters/$id/comics"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterEventsUrl(id: Int): String {
        var url = "characters/$id/events"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterSeriesUrl(id: Int): String {
        var url = "characters/$id/series"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

    fun getCharacterStoriesUrl(id: Int): String {
        var url = "characters/$id/stories"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

}
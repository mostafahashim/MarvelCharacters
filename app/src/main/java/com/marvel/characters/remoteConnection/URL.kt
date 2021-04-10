package com.marvel.characters.remoteConnection

object URL {

    fun getWeatherUrl(): String {
        var url = "weather"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

}
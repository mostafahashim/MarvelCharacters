package com.marvel.characters.remoteConnection

object URL {

    fun getCharactersUrl(): String {
        var url = "characters"
        url = url.replace(" ".toRegex(), "%20")
        return url
    }

}
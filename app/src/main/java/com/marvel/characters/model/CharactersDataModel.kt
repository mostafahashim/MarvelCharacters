package com.marvel.characters.model

import java.io.Serializable

class CharactersDataModel : Serializable {
    lateinit var results: ArrayList<CharacterModel>
    var offset: Int = 0
    var limit: Int = 0
    var total: Long = 0
    var count: Int = 0

}
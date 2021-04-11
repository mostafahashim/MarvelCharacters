package com.marvel.characters.model

import java.io.Serializable

class CharacterComicDataModel : Serializable {
    lateinit var results: ArrayList<ItemModel>
    var offset: Int = 0
    var limit: Int = 0
    var total: Int = 0
    var count: Int = 0

}
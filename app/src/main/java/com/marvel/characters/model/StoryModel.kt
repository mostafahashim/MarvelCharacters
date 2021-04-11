package com.marvel.characters.model

import java.io.Serializable
import java.util.Comparator

data class StoryModel(
    var available: Int? = 0,
    var collectionURI: String? = "",
    var items: ArrayList<ItemModel>? = ArrayList(),
    var returned: Int? = 0,
) : Serializable

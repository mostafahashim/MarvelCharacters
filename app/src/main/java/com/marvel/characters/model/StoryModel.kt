package com.marvel.characters.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Comparator

data class StoryModel(
    var available: Int? = 0,
    var collectionURI: String? = "",
    var items: ArrayList<ItemModel>? = ArrayList(),
    var returned: Int? = 0,
) : Serializable

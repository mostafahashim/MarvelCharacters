package com.marvel.characters.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Comparator

data class ItemModel(
    var resourceURI: String? = "",
    var name: String? = "",
    var type: String? = "",
) : Serializable

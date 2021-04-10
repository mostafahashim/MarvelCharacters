package com.marvel.characters.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Comparator

data class ThumbnailModel(
    var path: String? = "",
    var extension: String? = ""
) : Serializable

package com.marvel.characters.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Comparator

data class ItemModel(
    var resourceURI: String? = "",
    var title: String? = "",
    var name: String? = "",
    var type: String? = "",
    var holderType: String? = "",
    var thumbnail: ThumbnailModel? = ThumbnailModel(),
    var columnWidth: Double = 0.0,
    var columnHeight: Double = 0.0
) : Serializable

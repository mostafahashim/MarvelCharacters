package com.marvel.characters.model

import java.io.Serializable

data class CharacterModel(
    var id: Int? = 0,
    var name: String? = "",
    var description: String? = "",
    var modified: String? = "",
    var thumbnail: ThumbnailModel? = ThumbnailModel(),
    var resourceURI: String? = "",
    var comics: StoryModel? = StoryModel(),
    var series: StoryModel? = StoryModel(),
    var stories: StoryModel? = StoryModel(),
    var events: StoryModel? = StoryModel(),
    var urls: ArrayList<URLModel>? = ArrayList(),
    var holderType: String? = "",
    var columnWidth: Double = 0.0,
    var columnHeight: Double = 0.0
) : Serializable

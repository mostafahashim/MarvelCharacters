package com.marvel.characters.model.responseAPIModel

import com.marvel.characters.model.CharacterComicDataModel
import com.marvel.characters.model.CharactersDataModel

class CharacterComicsResponseModel : ParentResponseModel() {
    lateinit var data: CharacterComicDataModel
}

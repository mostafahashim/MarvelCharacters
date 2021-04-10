package com.marvel.characters.model.responseAPIModel

import com.marvel.characters.model.CharactersDataModel

class CharactersListResponseModel : ParentResponseModel() {
    lateinit var data: CharactersDataModel
}

package com.marvel.characters.view.activity.main

import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.marvel.characters.MyApplication
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.view.activity.baseActivity.BaseActivityViewModel
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var compositeDisposable = CompositeDisposable()

    var isShowLoader = MutableLiveData<Boolean>()
    var isShowNoData = MutableLiveData<Boolean>()

    var characterModels: ArrayList<CharacterModel>? = ArrayList()


    init {
        isShowLoader.value = true
        isShowNoData.value = false

    }

    fun updateBooksAdapterColumnWidth(screenWidth: Int, isLandScape: Boolean) {
        var columnWidth = (150.00 * (if (isLandScape) (screenWidth / 2) else screenWidth)) / 360.00
//        recyclerImagesAdapter.setColumnWidthAndRatio(columnWidth)
//        recyclerImagesAdapter.notifyDataSetChanged()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun onButtonHomeClicked() {
    }

    interface Observer {
    }

}
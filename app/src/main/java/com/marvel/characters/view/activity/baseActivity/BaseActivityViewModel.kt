package com.marvel.characters.view.activity.baseActivity

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.marvel.characters.MyApplication
import com.marvel.characters.R
import com.marvel.characters.util.Preferences
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseActivityViewModel(
    var application: MyApplication
) : AndroidViewModel(application) {
    lateinit var baseViewModelObserver: BaseViewModelObserver
    var keyWord = MutableLiveData<String>()

    var baseCompositeDisposable = CompositeDisposable()

    init {
        keyWord.value = ""
    }

    fun onButtonBackClicked() {
        baseViewModelObserver.onBackButtonClicked()
    }

    fun onButtonMenuClicked() {
        baseViewModelObserver.onMenuButtonClicked()
    }

    fun onButtonAny2Clicked() {
        baseViewModelObserver.onAny2ButtonClicked()
    }

    fun onSearchClicked() {
        baseViewModelObserver.onSearchClicked()
    }



    fun clearAppPreferencesAndDB() {
        baseCompositeDisposable.add(Observable.fromCallable {
            Preferences.clearUserData()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }
            .subscribe(
                {
                },
                { }
            ))
    }

    interface BaseViewModelObserver {
        fun onBackButtonClicked()
        fun onMenuButtonClicked()
        fun onAny1ButtonClicked()
        fun onAny2ButtonClicked()
        fun onSearchClicked()
        fun onLoginAgain()
        fun onRestartApp(message: Int)
        fun openLoginToUseFeature()
    }
}
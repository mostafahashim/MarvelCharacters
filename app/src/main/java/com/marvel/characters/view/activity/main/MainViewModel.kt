package com.marvel.characters.view.activity.main

import android.os.SystemClock
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.marvel.characters.MyApplication
import com.marvel.characters.R
import com.marvel.characters.adapter.RecyclerCharacterHomeAdapter
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.observer.OnRecyclerItemClickListener
import com.marvel.characters.remoteConnection.JsonParser
import com.marvel.characters.remoteConnection.URL
import com.marvel.characters.remoteConnection.remoteService.RemoteCallback
import com.marvel.characters.remoteConnection.remoteService.startGetMethodUsingCoroutines
import com.marvel.characters.remoteConnection.setup.getDefaultParams
import com.marvel.characters.util.HashUtil
import com.marvel.characters.util.PrivateKeyMarvel
import com.marvel.characters.util.PublicKeyMarvel
import com.marvel.characters.view.activity.baseActivity.BaseActivityViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var isShowRefresh = MutableLiveData<Boolean>()
    var connectionErrorMessage = ""
    var isShowNoData = MutableLiveData<Boolean>()

    var characterModels: ArrayList<CharacterModel>? = ArrayList()
    var recyclerCharacterHomeAdapter: RecyclerCharacterHomeAdapter

    var limit = 40
    var total = 0

    init {
        isShowLoader.value = true
        isShowError.value = false
        isShowRefresh.value = false
        isShowNoData.value = false


        recyclerCharacterHomeAdapter = RecyclerCharacterHomeAdapter(0.0,
            characterModels!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                    observer.openCharacterDetails(characterModels!![position])
                }

            })

        getHomeDataApi()
    }

    fun updateBooksAdapterColumnWidth(screenWidth: Int) {
        var columnWidth = (360.00 * screenWidth) / 360.00
        recyclerCharacterHomeAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerCharacterHomeAdapter.notifyDataSetChanged()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getHomeDataApi() {
        var params = getDefaultParams(application, HashMap())
        params["limit"] = limit
        //items to skip
        params["offset"] = 0

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharactersUrl(),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                        isShowLoader.value = true
                        isShowError.value = false
                        isShowRefresh.value = false
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                        try {
                            Log.i("ApiError", errorMessage.toString())
                            isShowLoader.value = false
                            var responseModel =
                                JsonParser().getParentResponseModel(errorMessage.toString())
                            connectionErrorMessage = responseModel?.message
                                ?: application.context.getString(R.string.something_went_wrong_please_try_again_)
                        } catch (e: Exception) {
                            connectionErrorMessage =
                                application.context.getString(R.string.something_went_wrong_please_try_again_)
                        }
                        isShowError.value = true
                    }

                    override fun onSuccessConnection(response: Any?) {
                        isShowLoader.value = false
                        try {
                            var responseModel =
                                JsonParser().getCharactersListResponseModel(response.toString())
                            if (responseModel != null) {
                                characterModels = responseModel.data.results
                                if (characterModels.isNullOrEmpty()) {
                                    isShowNoData.value = true
                                } else {
                                    total = responseModel.data.total
                                    recyclerCharacterHomeAdapter.setList(characterModels!!)
                                }
                            } else {
                                connectionErrorMessage =
                                    application.context.getString(R.string.something_went_wrong_please_try_again_)
                                isShowError.value = true
                            }
                        } catch (e: Exception) {
                            connectionErrorMessage =
                                application.context.getString(R.string.something_went_wrong_please_try_again_)
                            isShowError.value = true
                        }
                    }

                    override fun onLoginAgain(errorMessage: Any?) {
                        onLoginAgain(errorMessage)
                    }
                })
        }
    }

    fun getNextItemsDataApi() {
        var params = getDefaultParams(application, HashMap())
        params["limit"] = limit
        params["offset"] = characterModels?.size!!

        viewModelScope.launch {
            startGetMethodUsingCoroutines(
                URL.getCharactersUrl(),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                        isShowRefresh.value = true
                        loadMore(true)
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                        isShowRefresh.value = false
                        loadMore(false)
                    }

                    override fun onSuccessConnection(response: Any?) {
                        isShowRefresh.value = false
                        try {
                            loadMore(false)
                            var responseModel =
                                JsonParser().getCharactersListResponseModel(response.toString())
                            if (responseModel != null) {
                                if (responseModel.data != null && !responseModel.data!!.results.isNullOrEmpty()) {
                                    characterModels!!.addAll(responseModel.data.results)
                                    recyclerCharacterHomeAdapter.notifyDataSetChanged()
                                    total = responseModel.data.total
                                }
                            }
                        } catch (e: Exception) {
                        }
                    }

                    override fun onLoginAgain(errorMessage: Any?) {
                        onLoginAgain(errorMessage)
                    }
                })
        }
    }

    fun loadMore(isAdd: Boolean) {
        if (isAdd) {
            var itemModel = CharacterModel()
            itemModel.holderType = "loadMore"
            characterModels!!.add(itemModel)
            recyclerCharacterHomeAdapter.notifyItemInserted(characterModels!!.size - 1)
        } else {
            characterModels!!.removeAt(characterModels!!.size - 1)
            recyclerCharacterHomeAdapter.notifyItemRemoved(characterModels!!.size)
        }
    }

    interface Observer {
        fun openCharacterDetails(characterModel: CharacterModel)
    }

}
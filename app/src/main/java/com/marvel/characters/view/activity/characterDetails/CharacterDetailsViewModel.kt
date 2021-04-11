package com.marvel.characters.view.activity.characterDetails

import android.os.SystemClock
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.marvel.characters.MyApplication
import com.marvel.characters.R
import com.marvel.characters.adapter.RecyclerCharacterHomeAdapter
import com.marvel.characters.adapter.RecyclerItemEventAdapter
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.model.ItemModel
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

class CharacterDetailsViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var isShowRefresh = MutableLiveData<Boolean>()
    var connectionErrorMessage = ""

    var characterModel: CharacterModel? = CharacterModel()
    var image = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()

    var isShowComics = MutableLiveData<Boolean>()
    var comicsItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerComicsItemEventAdapter: RecyclerItemEventAdapter

    var isShowEvents = MutableLiveData<Boolean>()
    var eventsItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerEventsItemEventAdapter: RecyclerItemEventAdapter

    var isShowStories = MutableLiveData<Boolean>()
    var storiesItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerStoriesItemEventAdapter: RecyclerItemEventAdapter

    var isShowSeries = MutableLiveData<Boolean>()
    var seriesItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerSeriesItemEventAdapter: RecyclerItemEventAdapter

    init {
        isShowLoader.value = true
        isShowError.value = false
        isShowRefresh.value = false
        isShowComics.value = false
        isShowEvents.value = false
        isShowStories.value = false
        isShowSeries.value = false
        image.value = ""
        name.value = ""
        description.value = ""

        recyclerComicsItemEventAdapter = RecyclerItemEventAdapter(0.0,
            comicsItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })

        recyclerEventsItemEventAdapter = RecyclerItemEventAdapter(0.0,
            eventsItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })

        recyclerSeriesItemEventAdapter = RecyclerItemEventAdapter(0.0,
            seriesItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })

        recyclerStoriesItemEventAdapter = RecyclerItemEventAdapter(0.0,
            storiesItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })
    }

    fun updateBooksAdapterColumnWidth(screenWidth: Int) {
        var columnWidth = (100.00 * screenWidth) / 360.00
        recyclerComicsItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerComicsItemEventAdapter.notifyDataSetChanged()

        recyclerEventsItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerEventsItemEventAdapter.notifyDataSetChanged()

        recyclerStoriesItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerStoriesItemEventAdapter.notifyDataSetChanged()

        recyclerSeriesItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerSeriesItemEventAdapter.notifyDataSetChanged()

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun setData() {
        image.value =
            "${characterModel?.thumbnail?.path}.${characterModel?.thumbnail?.extension}"
        name.value = characterModel?.name!!
        description.value = characterModel?.description!!

        getComicsDataApi()
        getEventsDataApi()
        getSeriesDataApi()
        getStoriesDataApi()
    }

    fun getHomeDataApi() {
        var params = getDefaultParams(application, HashMap())

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharacterDetailsUrl(characterModel?.id!!),
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
                                if (!responseModel.data.results.isNullOrEmpty()) {
                                    characterModel = responseModel.data.results[0]
                                    setData()
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

    private fun getComicsDataApi() {
        var params = getDefaultParams(application, HashMap())

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharacterComicsUrl(characterModel?.id!!),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                    }

                    override fun onSuccessConnection(response: Any?) {
                        try {
                            var responseModel =
                                JsonParser().getCharacterComicsResponseModel(response.toString())
                            if (responseModel != null) {
                                if (!responseModel.data.results.isNullOrEmpty()) {
                                    comicsItems = responseModel.data.results
                                    //comics
                                    isShowComics.value = !comicsItems?.isNullOrEmpty()!!
                                    if (isShowComics.value!!) {
                                        recyclerComicsItemEventAdapter.setList(comicsItems!!)
                                    }
                                }
                            } else {
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

    private fun getEventsDataApi() {
        var params = getDefaultParams(application, HashMap())

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharacterEventsUrl(characterModel?.id!!),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                    }

                    override fun onSuccessConnection(response: Any?) {
                        try {
                            var responseModel =
                                JsonParser().getCharacterComicsResponseModel(response.toString())
                            if (responseModel != null) {
                                if (!responseModel.data.results.isNullOrEmpty()) {
                                    eventsItems = responseModel.data.results
                                    //Events
                                    isShowEvents.value = !eventsItems?.isNullOrEmpty()!!
                                    if (isShowEvents.value!!) {
                                        recyclerEventsItemEventAdapter.setList(eventsItems!!)
                                    }
                                }
                            } else {
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

    private fun getSeriesDataApi() {
        var params = getDefaultParams(application, HashMap())

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharacterSeriesUrl(characterModel?.id!!),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                    }

                    override fun onSuccessConnection(response: Any?) {
                        try {
                            var responseModel =
                                JsonParser().getCharacterComicsResponseModel(response.toString())
                            if (responseModel != null) {
                                if (!responseModel.data.results.isNullOrEmpty()) {
                                    seriesItems = responseModel.data.results
                                    //Series
                                    isShowSeries.value = !seriesItems?.isNullOrEmpty()!!
                                    if (isShowSeries.value!!) {
                                        recyclerSeriesItemEventAdapter.setList(seriesItems!!)
                                    }
                                }
                            } else {
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

    private fun getStoriesDataApi() {
        var params = getDefaultParams(application, HashMap())

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharacterStoriesUrl(characterModel?.id!!),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                    }

                    override fun onSuccessConnection(response: Any?) {
                        try {
                            var responseModel =
                                JsonParser().getCharacterComicsResponseModel(response.toString())
                            if (responseModel != null) {
                                if (!responseModel.data.results.isNullOrEmpty()) {
                                    storiesItems = responseModel.data.results
                                    //Stories
                                    isShowStories.value = !storiesItems?.isNullOrEmpty()!!
                                    if (isShowStories.value!!) {
                                        recyclerStoriesItemEventAdapter.setList(storiesItems!!)
                                    }
                                }
                            } else {
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

    interface Observer {
        fun openImageViewer()
    }

}
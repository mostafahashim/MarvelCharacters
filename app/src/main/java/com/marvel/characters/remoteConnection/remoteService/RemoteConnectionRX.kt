package com.marvel.characters.remoteConnection.remoteService

import com.marvel.characters.remoteConnection.remoteService.APIInterface
import com.marvel.characters.remoteConnection.remoteService.ConnectionHandler
import com.marvel.characters.remoteConnection.setup.getDefaultHeaders
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

fun startGetTwoApisMethodUsingRX(
    token: String,
    urlFunction1: String,
    params1: MutableMap<String, Any>,
    urlFunction2: String,
    params2: MutableMap<String, Any>,
    remoteCallback: RemoteCallback
): Disposable {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    var result = ArrayList<Response<String>>()
    return apiInterface!!.doGetConnectionUsingRX(
        getDefaultHeaders(false),
        urlFunction1,
        params1
    )!!.flatMap { response1: Response<String> ->
        result.add(response1)
        return@flatMap apiInterface!!.doGetConnectionUsingRX(
            getDefaultHeaders(false),
            urlFunction2,
            params2
        )
    }.map { response2: Response<String> ->
        result.add(response2)
        return@map result
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { dis ->
            remoteCallback.onStartConnection()
        }
        .subscribe(
            { response: ArrayList<Response<String>> ->
                if ((response[0].code() == 200 || response[0].code() == 201) && response[0].isSuccessful
                    && (response[1].code() == 200 || response[1].code() == 201) && response[1].isSuccessful
                ) {
                    var allResults = ArrayList<String>()
                    allResults.add(response[0].body().toString())
                    allResults.add(response[1].body().toString())
                    remoteCallback.onSuccessConnection(allResults)
                } else if (response[0].code() == 401 && response[0].errorBody() != null) {
                    try {
                        //error response 1
                        remoteCallback.onLoginAgain(
                            JSONObject(
                                response[0].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onLoginAgain(e.message)
                    }
                } else if (response[1].code() == 401 && response[1].errorBody() != null) {
                    try {
                        remoteCallback.onLoginAgain(
                            JSONObject(
                                response[1].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onLoginAgain(e.message)
                    }
                } else if (response[0].errorBody() != null) {
                    try {
                        remoteCallback.onFailureConnection(
                            JSONObject(
                                response[0].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onFailureConnection(e.message)
                    }
                } else if (response[1].errorBody() != null) {
                    try {
                        remoteCallback.onFailureConnection(
                            JSONObject(
                                response[1].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onFailureConnection(e.message)
                    }
                } else {
                    remoteCallback.onFailureConnection("")
                }
            }, { t: Throwable ->
                remoteCallback.onFailureConnection(t.message)
            })
}

fun startGetMethodUsingRX(
    urlFunction: String,
    params: MutableMap<String, Any>,
    remoteCallback: RemoteCallback
): Disposable {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    return apiInterface!!.doGetConnectionUsingRX(
        getDefaultHeaders(false),
        urlFunction,
        params
    )!!.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { dis ->
            remoteCallback.onStartConnection()
        }
        .subscribe(
            { response: Response<String> ->
                if ((response.code() == 200 || response.code() == 201) && response.isSuccessful) {
                    remoteCallback.onSuccessConnection(response.body().toString())
                } else if (response.code() == 401 && response.errorBody() != null) {
                    try {
                        remoteCallback.onLoginAgain(
                            JSONObject(
                                response.errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onLoginAgain(e.message)
                    }
                } else if (response.errorBody() != null) {
                    try {
                        remoteCallback.onFailureConnection(
                            JSONObject(
                                response.errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onFailureConnection(e.message)
                    }
                } else {
                    remoteCallback.onFailureConnection("")
                }
            }, { t: Throwable ->
                remoteCallback.onFailureConnection(t.message)
            })
}

fun startPostMethodUsingRX(
    urlFunction: String,
    params: MultipartBody,
    remoteCallback: RemoteCallback
): Disposable {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    return apiInterface!!.doPostConnectionUsingRX(
        getDefaultHeaders(true),
        urlFunction,
        params
    )!!.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .doOnSubscribe {
            remoteCallback.onStartConnection()
        }
        .subscribe({ response: Response<String> ->
            if ((response.code() == 200 || response.code() == 201) && response.isSuccessful) {
                remoteCallback.onSuccessConnection(response.body()!!.toString())
            } else if (response.code() == 401 && response.errorBody() != null) {
                try {
                    remoteCallback.onLoginAgain(
                        JSONObject(
                            response.errorBody()!!.string()
                        ).toString()
                    )
                } catch (e: Exception) {
                    remoteCallback.onLoginAgain(e.message)
                }
            } else if (response.errorBody() != null) {
                try {
                    remoteCallback.onFailureConnection(
                        JSONObject(
                            response.errorBody()!!.string()
                        ).toString()
                    )
                } catch (e: Exception) {
                    remoteCallback.onFailureConnection(e.message)
                }
            } else {
                remoteCallback.onFailureConnection("")
            }
        }, { t: Throwable ->
            remoteCallback.onFailureConnection(t.message)
        })
}

fun startPostTwoApisMethodWithGSONParamsUsingRX(
    token: String,
    urlFunction1: String,
    params1: MutableMap<String, Any>,
    urlFunction2: String,
    params2: MutableMap<String, Any>,
    remoteCallback: RemoteCallback
): Disposable {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    val json1 = Gson().toJson(params1)
    val body1 = RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
        json1
    )
    val json2 = Gson().toJson(params1)
    val body2 = RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
        json2
    )
    var result = ArrayList<Response<String>>()
    return apiInterface!!.doPostConnectionUsingRX(
        getDefaultHeaders(false),
        urlFunction1,
        body1
    )!!.flatMap { response1: Response<String> ->
        result.add(response1)
        return@flatMap apiInterface!!.doPostConnectionUsingRX(
            getDefaultHeaders(false),
            urlFunction2,
            body2
        )
    }.map { response2: Response<String> ->
        result.add(response2)
        return@map result
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { dis ->
            remoteCallback.onStartConnection()
        }
        .subscribe(
            { response: ArrayList<Response<String>> ->
                if ((response[0].code() == 200 || response[0].code() == 201) && response[0].isSuccessful
                    && (response[1].code() == 200 || response[1].code() == 201) && response[1].isSuccessful
                ) {
                    var allResults = ArrayList<String>()
                    allResults.add(response[0].body().toString())
                    allResults.add(response[1].body().toString())
                    remoteCallback.onSuccessConnection(allResults)
                } else if (response[0].code() == 401 && response[0].errorBody() != null) {
                    try {
                        //error response 1
                        remoteCallback.onLoginAgain(
                            JSONObject(
                                response[0].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onLoginAgain(e.message)
                    }
                } else if (response[1].code() == 401 && response[1].errorBody() != null) {
                    try {
                        remoteCallback.onLoginAgain(
                            JSONObject(
                                response[1].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onLoginAgain(e.message)
                    }
                } else if (response[0].errorBody() != null) {
                    try {
                        remoteCallback.onFailureConnection(
                            JSONObject(
                                response[0].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onFailureConnection(e.message)
                    }
                } else if (response[1].errorBody() != null) {
                    try {
                        remoteCallback.onFailureConnection(
                            JSONObject(
                                response[1].errorBody()!!.string()
                            ).toString()
                        )
                    } catch (e: Exception) {
                        remoteCallback.onFailureConnection(e.message)
                    }
                } else {
                    remoteCallback.onFailureConnection("")
                }
            }, { t: Throwable ->
                //                connectionCallback.onFailureConnection(t.message)
                remoteCallback.onFailureConnection(t.message)
            })
}

fun startPostMethodWithGSONParamsUsingRX(
    urlFunction: String,
    params: MutableMap<String, Any>,
    remoteCallback: RemoteCallback
): Disposable {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    val json = Gson().toJson(params)
    val body = RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
        json
    )

    return apiInterface!!.doPostConnectionUsingRX(
        getDefaultHeaders(false),
        urlFunction,
        body
    )!!.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .doOnSubscribe {
            remoteCallback.onStartConnection()
        }
        .subscribe({ response: Response<String> ->
            if (response != null && (response.code() == 200 || response.code() == 201) && response.isSuccessful) {
                remoteCallback.onSuccessConnection(response.body()!!.toString())
            }  else if (response.code() == 401 && response.errorBody() != null) {
                try {
                    remoteCallback.onLoginAgain(
                        JSONObject(
                            response.errorBody()!!.string()
                        ).toString()
                    )
                } catch (e: Exception) {
                    remoteCallback.onLoginAgain(e.message)
                }
            } else if (response.errorBody() != null) {
                try {
                    remoteCallback.onFailureConnection(
                        JSONObject(
                            response.errorBody()!!.string()
                        ).toString()
                    )
                } catch (e: Exception) {
                    remoteCallback.onFailureConnection(e.message)
                }
            } else {
                remoteCallback.onFailureConnection("")
            }
        }, { t: Throwable ->
            remoteCallback.onFailureConnection(t.message)
        })
}
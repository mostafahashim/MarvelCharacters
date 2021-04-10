package com.marvel.characters.remoteConnection.remoteService

import android.os.AsyncTask
import com.marvel.characters.remoteConnection.remoteService.APIInterface
import com.marvel.characters.remoteConnection.remoteService.ConnectionHandler
import com.marvel.characters.remoteConnection.remoteService.RemoteCallback
import com.marvel.characters.remoteConnection.setup.getDefaultHeaders
import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun startGetMethodWithCustomUrl(
    baseUrl: String,
    urlFunction: String,
    params: MutableMap<String, Any?>,
    remoteCallback: RemoteCallback
) {
    val apiInterface = ConnectionHandler.getInstance().getClientWithCustomUrl(baseUrl)?.create(
        APIInterface::class.java)
    val call = apiInterface!!.doGetConnection(
        getDefaultHeaders(false),
        urlFunction,
        params
    )
    remoteCallback.onStartConnection()
    call.enqueue(object : Callback<String> {

        override fun onResponse(call: Call<String>, response: Response<String>) {
            if (response != null && (response.code() == 200 || response.code() == 201) && response.isSuccessful) {
                remoteCallback.onSuccessConnection(response.body()!!.toString())
            } else if (response.errorBody() != null) {
                try {
                    remoteCallback.onFailureConnection(
                        JSONObject(
                            response.errorBody()!!.string()
                        ).toString()
                    )
                } catch (e: Exception) {
                    remoteCallback.onFailureConnection(response.message().toString())
                }
            } else {
                remoteCallback.onFailureConnection(response.message().toString())
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            remoteCallback.onFailureConnection(t.message)
        }
    })
}

fun startDownloadFile(
    folderName: String,
    filePath: String,
    urlFunction: String,
    remoteCallback: RemoteCallback
) {
    val apiInterface =
        ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
//        var urlFunction1 = urlFunction.removePrefix("https://edara.com/Product/")
    val call = apiInterface!!.downloadFile(urlFunction)
    remoteCallback.onStartConnection()
    call.enqueue(object : Callback<ResponseBody> {

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            try {
                if (response.isSuccessful) {
                    class saveDownloadStreamingTask : AsyncTask<Void, Void, String>() {
                        override fun doInBackground(vararg params: Void?): String? {
                            var iStream: BufferedInputStream? = null
                            var output: FileOutputStream? = null
                            try {
                                val body = response.body()
                                iStream = BufferedInputStream(body!!.byteStream())
                                try {
                                    output = FileOutputStream(filePath)
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    val wallpaperDirectory = File(folderName)
                                    wallpaperDirectory.mkdirs()
                                    output = FileOutputStream(filePath)
                                }
                                response.body()
                                val buffer = ByteArray(1024)
                                var total: Long = 0
                                var count: Int
                                do {
                                    count = iStream.read(buffer)
                                    if (count == -1) {
                                        if (total < response.body()!!.contentLength())
                                            remoteCallback.onFailureConnection("")
                                        break
                                    }
                                    total += count.toLong()
                                    output!!.write(buffer, 0, count)
                                } while (true)
                                if (total == response.body()!!.contentLength()) {
                                    remoteCallback.onSuccessConnection("")
                                } else {
                                    remoteCallback.onFailureConnection("")
                                }
                            } catch (e: IOException) {
                                remoteCallback.onFailureConnection("")
                            } finally {
                                iStream?.close()
                                output?.close()
                            }
                            return null
                        }
                    }
                    saveDownloadStreamingTask().execute()
                } else {
                    remoteCallback.onFailureConnection("")
                }
            } catch (e: IOException) {
                remoteCallback.onFailureConnection("")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            //                connectionCallback.onFailureConnection(t.message)
            remoteCallback.onFailureConnection("")
        }
    })
}

fun startPostMethodWithGSONParams(
    urlFunction: String,
    params: MutableMap<String, Any?>,
    remoteCallback: RemoteCallback
) {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    val json = Gson().toJson(params)
    val body = RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
        json
    )
    val call = apiInterface!!.doPostConnection(
        getDefaultHeaders(false),
        urlFunction,
        body
    )
    remoteCallback.onStartConnection()
    call.enqueue(object : Callback<String> {

        override fun onResponse(call: Call<String>, response: Response<String>) {
            if (response != null && (response.code() == 200 || response.code() == 201) && response.isSuccessful) {
                remoteCallback.onSuccessConnection(response.body()!!.toString())
            } else if (response.errorBody() != null) {
                try {
                    remoteCallback.onFailureConnection(
                        JSONObject(
                            response.errorBody()!!.string()
                        ).toString()
                    )
                } catch (e: Exception) {
                    remoteCallback.onFailureConnection(response.message().toString())
                }
            } else {
                remoteCallback.onFailureConnection(response.message().toString())
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            remoteCallback.onFailureConnection(t.message)
        }
    })
}
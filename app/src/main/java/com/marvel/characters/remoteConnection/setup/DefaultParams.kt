package com.marvel.characters.remoteConnection.setup

import android.os.SystemClock
import com.marvel.characters.BuildConfig
import com.marvel.characters.MyApplication
import com.marvel.characters.util.HashUtil
import com.marvel.characters.util.Preferences
import com.marvel.characters.util.PrivateKeyMarvel
import com.marvel.characters.util.PublicKeyMarvel
import okhttp3.MultipartBody
import kotlin.collections.HashMap

fun getDefaultParams(
    application: MyApplication,
    params: HashMap<String, Any>
): MutableMap<String, Any> {
    params["apikey"] = "2bd481c7a55f24e9b5645495f192a5ae"
    var currentTimeStamp = SystemClock.elapsedRealtime()
    params["hash"] = HashUtil().toMD5("$currentTimeStamp$PrivateKeyMarvel$PublicKeyMarvel")
    params["ts"] = currentTimeStamp
    return params
}

fun getDefaultParams(
    application: MyApplication,
    builder: MultipartBody.Builder
): MultipartBody.Builder {
    builder.setType(MultipartBody.FORM)
    var token = Preferences.getUserToken()
    builder.addFormDataPart("notification_Token", token)
    builder.addFormDataPart("lang", Preferences.getApplicationLocale())
    builder.addFormDataPart("userId", Preferences.getUserID())
    builder.addFormDataPart("user_id", Preferences.getUserID())
    builder.addFormDataPart("user_type", Preferences.getUserType())
    builder.addFormDataPart("userType", Preferences.getUserType())
    builder.addFormDataPart("version_code", BuildConfig.VERSION_CODE.toString())
    builder.addFormDataPart("os_version", application.getOSVersion())
    builder.addFormDataPart("mobile_model", application.getDeviceModel())
    builder.addFormDataPart("applicationId", "0")
    builder.addFormDataPart("android", "true")
    return builder
}
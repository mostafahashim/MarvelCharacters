package com.marvel.characters.remoteConnection.setup

import com.marvel.characters.BuildConfig
import com.marvel.characters.MyApplication
import com.marvel.characters.util.Preferences
import okhttp3.MultipartBody
import kotlin.collections.HashMap

fun getDefaultParams(
    application: MyApplication,
    params: HashMap<String, Any>
): MutableMap<String, Any> {
    params["appid"] = "8fa4589dfa68cb117ddeb64b68e27777"
    params["units"] = "metric"
    params["lang"] = Preferences.getApplicationLocale()
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
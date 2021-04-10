package com.marvel.characters.remoteConnection.setup

import com.marvel.characters.BuildConfig
import com.marvel.characters.util.Preferences
import com.marvel.characters.util.appId
import kotlin.collections.HashMap

inline fun getDefaultHeaders(isFormData: Boolean): MutableMap<String, String> {
    var params = HashMap<String, String>()
    if (!isFormData)
        params["Content-Type"] = "application/json"

    params["Accept"] = "application/json"
    params["Accept-Language"] = Preferences.getApplicationLocale()
    params["app-id"] = appId
    params["Authorization"] = if (!Preferences.getAPIToken()
            .isNullOrEmpty()
    ) "Bearer ${Preferences.getAPIToken()}" else ""
    return params
}

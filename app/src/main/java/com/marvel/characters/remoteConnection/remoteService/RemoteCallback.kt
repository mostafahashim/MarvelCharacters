package com.marvel.characters.remoteConnection.remoteService

interface RemoteCallback {
     fun onStartConnection()

     fun onFailureConnection(errorMessage: Any?)

     fun onSuccessConnection(response: Any?){}
     fun onLoginAgain(errorMessage: Any?)
}
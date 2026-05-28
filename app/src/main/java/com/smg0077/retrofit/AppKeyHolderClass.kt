package com.smg0077.retrofit

class AppKeyHolderClass {
    companion object {
        lateinit var appKey: String
    }
    fun getAppKey(): String {
        return appKey
    }
    fun setAppKey(appKey2: String) {
        appKey = appKey2
    }
}




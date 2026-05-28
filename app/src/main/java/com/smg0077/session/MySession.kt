package com.smg0077.session

import android.content.Context
import android.content.SharedPreferences

class MySession(context: Context) {
    private var preferences:SharedPreferences
    private var sharedPrefEditor: SharedPreferences.Editor

    init {
        preferences= context.getSharedPreferences("APP_KEY",0)
        sharedPrefEditor=preferences.edit()
        sharedPrefEditor.apply()
    }

    fun setLogin(login:Boolean){
        sharedPrefEditor.putBoolean("KEY_LOGIN",login)
        sharedPrefEditor.commit()
    }

    fun getLogin():Boolean{
            return preferences.getBoolean("KEY_LOGIN",false)
    }

    fun setSession_username(USERNAME:String){
        sharedPrefEditor.putString("USER_NAME",USERNAME)
        sharedPrefEditor.commit()
    }
    fun setSession_userid(USERID:String){
        sharedPrefEditor.putString("unique_token",USERID)
        sharedPrefEditor.commit()
    }
    fun setSession_usermobile(USERMOBILE:String){
        sharedPrefEditor.putString("USER_MOBILE",USERMOBILE)
        sharedPrefEditor.commit()
    }
    fun setSession_securitykey(securitykey:String){              //security key
        sharedPrefEditor.putString("USER_Securitykey",securitykey)
        sharedPrefEditor.commit()
    }
    fun getSession_securitykey():String{
        return preferences.getString("USER_Securitykey","").toString()

    }
    fun setSession_Whatappnumber(securitykey:String){              //security key
        sharedPrefEditor.putString("USER_WHATSAPPNUMBER",securitykey)
        sharedPrefEditor.commit()
    }
    fun setNotification_Status(status:Boolean){
        sharedPrefEditor.putBoolean("NOTIFICATION",status)
        sharedPrefEditor.commit()
    }

    fun getSession_Whatappnumber():String{
        return preferences.getString("USER_WHATSAPPNUMBER","").toString()

    }

    fun getSession_username():String{
        return preferences.getString("USER_NAME","").toString()
    }
    fun getSession_userid():String{
        return preferences.getString("unique_token","").toString()
    }
    fun getSession_usermobile():String{
        return preferences.getString("USER_MOBILE","").toString()
    }

    fun getNotificationStatus():Boolean{
        return preferences.getBoolean("NOTIFICATION", true)
    }
}
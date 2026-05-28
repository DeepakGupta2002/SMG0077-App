package com.smg0077.retrofit

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiConnection {

    @POST("api-user-registration")
    fun registeruser(@Body register: JsonObject): Call<JsonObject>

    @POST("api-check-mobile")
    fun checkmobilevalid(@Body checkMobile: JsonObject): Call<JsonObject>

    @POST("api-user-login")
    fun userlogin(@Body login: JsonObject): Call<JsonObject>

    @POST("api-resend-otp")
    fun otpresent(@Body otpResent: JsonObject): Call<JsonObject>

    @POST("api-forget-check-mobile")
    fun ValideNumberForgotPassword(@Body validephone: JsonObject): Call<JsonObject>

    @POST("api-forgot-password")
    fun changeForgotPassword(@Body changepass: JsonObject): Call<JsonObject>

    @POST("api-game-rates")
    fun Maingamerates(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-get-profile")
    fun getprofile(@Body getprofile: JsonObject): Call<JsonObject>

    @POST("api-profile-update")
    fun updateprofiledata(@Body getprofileupdate: JsonObject): Call<JsonObject>

    @POST("api-fcm-update")
    fun updatefcm(@Body getprofileupdate: JsonObject): Call<JsonObject>

    @POST("api-notification-setting")
    fun notificationsetting(@Body getprofileupdate: JsonObject): Call<JsonObject>

    @POST("api-get-notice")
    fun getnotice(@Body getnotice: JsonObject): Call<JsonObject>

    @POST("api-get-dashboard-data")
    fun getdashboardData(@Body getdashboardData: JsonObject): Call<JsonObject>

    @POST("api-change-password")
    fun updatepasswordApi(@Body getdashboardData: JsonObject): Call<JsonObject>

    @POST("api-bid-history-data")
    fun bidhistoryApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-wining-history-data")
    fun winhistoryApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-user-wallet-balance")
    fun refreshAmtApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-wallet-transaction-history")
    fun walletTranHistoryApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-user-withdraw-transaction-history")
    fun WithdrawTranHistoryApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-get-slider-images")
    fun SliderimageApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-get-banners")
    fun BannerApi(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-add-user-upi-details")
    fun AddusernumberAPI(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-get-user-payment-details")
    fun CheckuserUpinumberAPI(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-get-contact-details")
    fun ContactusAPI(@Body getbidhistory: JsonObject): Call<JsonObject>

    @POST("api-user-payment-method-list")
    fun PaymentmethodListAPI(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-user-withdraw-fund-request")
    fun WithdrowpointRequest(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-user-withdraw-transaction-history")
    fun WithdrowTransactionHistory(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-check-user-for-transfer-amt")
    fun CheckAccountTransferAmtAPI(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-user-transfer-wallet-balance")
    fun TransferuserAmountAPI(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-get-current-date")
    fun Bid_window_currentdate(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-check-games-active-inactive")
    fun CheckgameActiveApi(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-check-game-status")
    fun Checkgamestatus(@Body getcurrentdate: JsonObject): Call<JsonObject>

    @POST("api-submit-bid")
    fun SubmitBidApi(@Body sendbiddata: JsonObject): Call<JsonObject>

    @POST("api-galidisswar-game")
    fun starlinegamedata(@Body getgamedata: JsonObject): Call<JsonObject>

    @POST("api-galidisswar-game-rates")
    fun starlinegamerates(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-check-galidisswar-game-status")
    fun starlinegamestatusApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-check-galidisswar-games-active-inactive")
    fun starlinegameactiveInactiveApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-galidisswar-submit-bid")
    fun starlinegamebidSubmitApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-galidisswar-bid-history-data")
    fun starlinegamebidHistoyApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-galidisswar-wining-history-data")
    fun starlinegameWiningHistoyApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-admin-bank-details")
    fun AddpointbankdetailsApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-last-fund-request-detail")
    fun Addpoint_lastpointrequest_Api(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-add-money-via-upi")
    fun Addpoint_pointAdded_Api(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-get-auto-deposit-list")
    fun Addpoint_pointAdded_HistoryApi(@Body AddpointHistory: JsonObject): Call<JsonObject>

    @POST("api-how-to-play")
    fun apiHowToPlay(@Body howToPlay: JsonObject): Call<JsonObject>

    @POST("api-check-security-pin")
    fun securityPinCheck(@Body login: JsonObject): Call<JsonObject>

    @POST("api-get-app-key")
    fun AppKeyget(@Body login: JsonObject): Call<JsonObject>

    @POST("api-get-social-data")
    fun socialDataGet(@Body login: JsonObject): Call<JsonObject>

    @POST("api-starline-game")
    fun realstarlinegamedata(@Body login: JsonObject): Call<JsonObject>

    @POST("api-starline-wining-history-data")
    fun realstarlinegameWiningHistoyApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-starline-submit-bid")
    fun realstarlinegamebidSubmitApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-starline-bid-history-data")
    fun realstarlinegamebidHistoyApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-check-starline-game-status")
    fun realstarlinegamestatusApi(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-starline-game-rates")
    fun realstarlinegamerates(@Body Gamerates: JsonObject): Call<JsonObject>

    @POST("api-fund-payment-slip-upload")
    fun uploadscreenshot(
        @Body params: RequestBody
    ): Call<JsonObject>
}
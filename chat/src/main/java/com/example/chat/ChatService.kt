package com.example.chat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatService {
    @GET("api.php?key=free&appid=0")
    fun getMsg(@Query("msg")msg:String): Call<ReturnMsg>
}
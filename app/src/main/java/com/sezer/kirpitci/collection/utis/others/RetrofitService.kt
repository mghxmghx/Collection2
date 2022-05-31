package com.sezer.kirpitci.collection.utis.others

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface RetrofitService {
    @GET("exchange")
    @Headers(
        "X-RapidAPI-Host: currency-exchange.p.rapidapi.com",
        "X-RapidAPI-Key: 0e8071a668msh4c8d68cd7b5c343p1ad8fcjsn26c076f130b1"
    )
    fun getExchangeCurrency(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("q") q: String
    ): Call<String?>?

}
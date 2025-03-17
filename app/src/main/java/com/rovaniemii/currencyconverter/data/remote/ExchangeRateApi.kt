package com.rovaniemii.currencyconverter.data.remote

import com.rovaniemii.currencyconverter.core.BaseAPIConstants
import com.rovaniemii.currencyconverter.core.ExchangeRateDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET(BaseAPIConstants.CURRENCY_CONVERTER_URL)
    fun getCurrencyConverterData(
        @Path("authkey") authKey: String = BaseAPIConstants.AUTH_KEY,
        @Query("searchdate") searchDate: String,
    ): Response<ExchangeRateDTO>
}
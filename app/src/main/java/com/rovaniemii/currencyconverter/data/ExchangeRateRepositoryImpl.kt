package com.rovaniemii.currencyconverter.data

import com.rovaniemii.currencyconverter.data.remote.ExchangeRateApi
import com.rovaniemii.currencyconverter.domain.model.ExchangeRate
import com.rovaniemii.currencyconverter.domain.repository.ExchangeRateRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateApi: ExchangeRateApi,
) : ExchangeRateRepository {
    override fun getExchangeRates(
        searchDate: String,
    ): Flow<Result<List<ExchangeRate>>> = flow {
        try {
            val response = exchangeRateApi
                .getCurrencyConverterData(
                    searchDate = searchDate,
                )

            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    val exchangeRateList = dto.map {
                        ExchangeRate(
                            currencyCode = it.currencyCode,
                            dealBaseRate = it.dealBaseRate?.toDoubleOrNull()
                        )
                    }
                    emit(Result.success(exchangeRateList))
                } ?: emit(Result.failure(Exception("Empty response body")))
            } else {
                emit(Result.failure(Exception("API Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
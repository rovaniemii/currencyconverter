package com.rovaniemii.currencyconverter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovaniemii.currencyconverter.domain.usecase.ConvertCurrencyUseCase
import com.rovaniemii.currencyconverter.domain.usecase.GetExchangeRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
) : ViewModel() {
    private val convertCurrencyUseCase = ConvertCurrencyUseCase()

    // 환전을 원하는 금액 (원화) : 사용자 입력
    private val _convertedAmount = MutableStateFlow(0)
    val convertedAmount: StateFlow<Int> = _convertedAmount

    // 환율
    private val _exchangeRate = MutableStateFlow<Double?>(null)
    val exchangeRate: StateFlow<Double?> = _exchangeRate

    // 환전 금액
    private val _finalAmount = combine(
        convertedAmount,
        exchangeRate,
    ) { amount, rate ->
        rate ?: return@combine null
        convertCurrencyUseCase.execute(amount, rate)
    }
    val finalAmount: Flow<Double?> = _finalAmount

    init {
        val searchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        getExchangeRateUseCase.execute(searchDate)
            .map { result ->
                result
                    .onSuccess { rates ->
                        Log.d("확인", rates.map { it.dealBaseRate }.toString())

                        val rate = rates.find { it.currencyCode == "AED" }?.dealBaseRate
                        _exchangeRate.value = rate
                    }
                    .onFailure {
                        it.printStackTrace()
                        _exchangeRate.value = null
                    }
            }
            .launchIn(viewModelScope)
    }

    fun setAmount(amount: Int) {
        _convertedAmount.value = amount
    }
}
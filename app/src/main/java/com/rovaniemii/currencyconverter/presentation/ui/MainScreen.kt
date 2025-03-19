package com.rovaniemii.currencyconverter.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovaniemii.currencyconverter.presentation.viewmodel.CurrencyViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel = hiltViewModel(),
) {
    // 원화
    val convertedAmount by viewModel.convertedAmount.collectAsStateWithLifecycle()
    // 환율
    val exchangeRate by viewModel.exchangeRate.collectAsStateWithLifecycle()
    // 최종값
    val finalAmount by viewModel.finalAmount.collectAsStateWithLifecycle(null)

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        InputAmount(
            modifier = Modifier
                .fillMaxWidth(),
            initValue = if (convertedAmount == 0) "" else convertedAmount.toString(),
            onValueChange = {
                viewModel.setAmount(it.toInt())
            }
        )

        Text(
            text = "현재 환율: $exchangeRate",
        )

        Text(
            text = "계산 결과: $finalAmount",
        )
    }
}

@Composable
private fun InputAmount(
    modifier: Modifier = Modifier,
    initValue: String,
    onValueChange: (value: String) -> Unit,
) {
    BasicTextField(
        modifier = modifier,
        value = initValue,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(
                        vertical = 14.dp,
                        horizontal = 20.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    if (initValue.isEmpty()) {
                        Text(
                            text = "금액을 입력해주세요",
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    innerTextField()
                }
            }
        },
    )
}
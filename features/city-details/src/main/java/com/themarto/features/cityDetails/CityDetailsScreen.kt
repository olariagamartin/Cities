package com.themarto.features.cityDetails

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(
    onNavigateBack: () -> Unit = { },
    cityId: String,
    viewModel: CityDetailViewModel = koinViewModel { parametersOf(cityId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("CityDetailsScreen", "uiState: $uiState")

    CenterAlignedTopAppBar(
        modifier = Modifier,
        title = {
            Text(
                text = "City Detail"
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
    Surface {
        Text(cityId)
    }
}

@Preview
@Composable
private fun CityDetailsScreenPreview() {
    CityDetailsScreen(
        cityId = "123"
    )
}
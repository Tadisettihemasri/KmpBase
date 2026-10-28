package com.a4b.kmpbase.core.ui.state

import com.a4b.kmpbase.core.common.result.AppError
import com.a4b.kmpbase.core.common.result.AppResult

/** Screen-level load state. For paged lists use Paging's LoadState instead. */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val error: AppError) : UiState<Nothing>
}

fun <T> AppResult<T>.toUiState(): UiState<T> = when (this) {
    is AppResult.Success -> UiState.Success(data)
    is AppResult.Failure -> UiState.Error(error)
}

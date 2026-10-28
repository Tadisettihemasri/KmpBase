package com.a4b.kmpbase.core.common.result

/** Outcome of any operation that can fail. Repositories return this; ViewModels map it to UI state. */
sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Failure -> this
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> = also {
    if (it is AppResult.Success) action(it.data)
}

inline fun <T> AppResult<T>.onFailure(action: (AppError) -> Unit): AppResult<T> = also {
    if (it is AppResult.Failure) action(it.error)
}

fun <T> AppResult<T>.getOrNull(): T? = (this as? AppResult.Success)?.data

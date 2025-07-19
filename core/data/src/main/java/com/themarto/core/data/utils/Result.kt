@file:OptIn(ExperimentalContracts::class)

package com.themarto.core.data.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Result<T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error<T>(val error: String) : Result<T>()

}

fun <T> Result<T>.isSuccess(): Boolean{
    contract {
        returns(true) implies (this@isSuccess is Result.Success)
    }
    return this is Result.Success
}

fun <T> Result<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Result.Error)
    }
    return this is Result.Error
}
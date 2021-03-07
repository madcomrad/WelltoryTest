package com.welltory.test.model

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val errorMessage: String) : Result<T>()
}

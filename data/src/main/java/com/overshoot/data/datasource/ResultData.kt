package com.overshoot.data.datasource

open class ResultData<T: Any>

fun <T : Any> ResultData<T>.onSuccess(action: (T) -> Unit): ResultData<T> {
    if (this is Success) {
        action(this.data)
    }
    return this
}

fun <T: Any> ResultData<T>.onFailure(action: (String) -> Unit): ResultData<T> {
    if (this is Failure) {
        action(this.message)
    }
    return this
}

class Success<T: Any>(val data: T): ResultData<T>()

class Failure<T: Any>(val message: String): ResultData<T>()
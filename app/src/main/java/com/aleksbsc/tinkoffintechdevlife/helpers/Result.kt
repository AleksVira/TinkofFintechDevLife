package com.aleksbsc.tinkoffintechdevlife.helpers

sealed class Result<out T : Any>{

    data class Success<out T : Any>(val data : T) : Result<T>()

    data class Error(val msg: String)  : Result<Nothing>()
}
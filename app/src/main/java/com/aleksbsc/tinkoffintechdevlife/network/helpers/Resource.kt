@file:Suppress("unused")

package com.aleksbsc.tinkoffintechdevlife.network.helpers

sealed class Resource<out T> {

    sealed class Success<T> : Resource<T>() {

        abstract val value: T

        override fun toString() = "Success($value)"

        class Value<T>(override val value: T) : Success<T>()

        data class HttpResponseImpl<T>(
            override val value: T,
            override val statusCode: Int,
            override val statusMessage: String? = null,
            override val url: String? = null
        ) : Success<T>(), HttpResponse

        object Empty : Success<Nothing>() {

            override val value: Nothing get() = error("No value")

            override fun toString() = "Success"
        }
    }



    sealed class Failure<E : Throwable>(open val error: E? = null) : Resource<Nothing>() {

        override fun toString() = "Failure($error)"

        class Error(override val error: Throwable) : Failure<Throwable>(error)

        class HttpError(override val error: HttpException) : Failure<HttpException>(), HttpResponse {

            override val statusCode: Int get() = error.statusCode

            override val statusMessage: String? get() = error.statusMessage

            override val url: String? get() = error.url
        }
    }
}

typealias EmptyResult = Resource<Nothing>

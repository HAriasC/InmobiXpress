package com.inmobixpress.inmobixpress.data.network.utils

import com.inmobixpress.inmobixpress.data.network.model.NetworkResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

const val TIME_OUT = 30_000L

object Util {
    //const val BASE_URL = "http://10.0.2.2:8080"
    const val BASE_URL = "https://inmobixpress.uc.r.appspot.com"
}

suspend inline fun <reified T : Any> HttpResponse.toResult(): NetworkResult<T> {
    return when (status) {
        HttpStatusCode.OK, HttpStatusCode.Created, HttpStatusCode.Accepted -> NetworkResult.Success(
            body()
        )

        HttpStatusCode.BadRequest -> NetworkResult.Error(NetworkException(body()))
        HttpStatusCode.Unauthorized -> NetworkResult.Error(NetworkException(body()))
        HttpStatusCode.NotFound -> NetworkResult.Error(NetworkException(body()))
        HttpStatusCode.InternalServerError, HttpStatusCode.ServiceUnavailable -> NetworkResult.Error(
            NetworkException("Server Disruption! We are on fixing it.")
        )

        HttpStatusCode.GatewayTimeout -> NetworkResult.Error(
            NetworkException("Too much load at this time, try again later!")
        )

        else -> NetworkResult.Error(
            NetworkException("Something went wrong! Please try again or contact support.")
        )
    }
}

class NetworkException(message: String) : Exception(message)
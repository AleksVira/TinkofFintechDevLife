package com.aleksbsc.tinkoffintechdevlife.network.helpers

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?

}

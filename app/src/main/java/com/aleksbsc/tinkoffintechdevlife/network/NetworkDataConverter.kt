package com.aleksbsc.tinkoffintechdevlife.network

import com.aleksbsc.tinkoffintechdevlife.data.DevLifePictureData
import com.aleksbsc.tinkoffintechdevlife.network.models.MemesServerResponse

class NetworkDataConverter {

    fun toDevLifeData(resultData: MemesServerResponse): DevLifePictureData {
        return DevLifePictureData(
            id = resultData.id.toLong(),
            pictureUrl = resultData.gifURL,
            description = resultData.description
        )
    }
}
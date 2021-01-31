package com.aleksbsc.tinkoffintechdevlife.repository

import com.aleksbsc.tinkoffintechdevlife.data.DevLifePictureData
import com.aleksbsc.tinkoffintechdevlife.data.MemesCategory
import com.aleksbsc.tinkoffintechdevlife.helpers.Constants.MISTAKE
import com.aleksbsc.tinkoffintechdevlife.helpers.Result
import com.aleksbsc.tinkoffintechdevlife.network.DevLifeService
import com.aleksbsc.tinkoffintechdevlife.network.NetworkDataConverter
import com.aleksbsc.tinkoffintechdevlife.network.helpers.Resource
import com.aleksbsc.tinkoffintechdevlife.network.models.MemesServerResponse
import com.aleksbsc.tinkoffintechdevlife.network.models.ResponseList
import timber.log.Timber

class DevLifeRepository(
    private val devLifeService: DevLifeService,
    private val networkDataConverter: NetworkDataConverter
) {

    suspend fun getNextDevLifeList(
        itemType: MemesCategory,
        page: Int
    ): Result<List<DevLifePictureData>> {
        val networkResult: Resource<ResponseList> = when (itemType) {
            MemesCategory.LATEST -> devLifeService.getNewLatest(page)
            MemesCategory.TOP -> devLifeService.getNewTop(page)
            else -> Resource.Failure.Error(Throwable(MISTAKE))
        }
        return when (networkResult) {
            is Resource.Success -> {
                val resultList: MutableList<DevLifePictureData> = mutableListOf()
                networkResult.value.networkMemesList.forEach {
                    resultList.add(networkDataConverter.toDevLifeData(it))
                }
                Result.Success(resultList)
            }
            is Resource.Failure<*> -> {
                Result.Error(networkResult.error.toString())
            }
        }
    }

    suspend fun getSingleDevLifeList(itemType: MemesCategory): Result<DevLifePictureData> {
        val networkResult: Resource<MemesServerResponse> = when (itemType) {
            MemesCategory.RANDOM -> devLifeService.getNewRandom()
            else -> Resource.Failure.Error(Throwable(MISTAKE))
        }
        return when (networkResult) {
            is Resource.Success -> {
                val resultList: DevLifePictureData =
                    networkDataConverter.toDevLifeData(networkResult.value)
                Result.Success(resultList)
            }
            is Resource.Failure<*> -> {
                Result.Error(networkResult.error.toString())
            }
        }
    }
}


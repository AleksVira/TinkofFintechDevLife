package com.aleksbsc.tinkoffintechdevlife.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksbsc.tinkoffintechdevlife.data.DevLifePictureData
import com.aleksbsc.tinkoffintechdevlife.data.MemesCategory
import com.aleksbsc.tinkoffintechdevlife.helpers.Constants.GLIDE_ERROR
import com.aleksbsc.tinkoffintechdevlife.helpers.ConsumableValue
import com.aleksbsc.tinkoffintechdevlife.helpers.LoadingState
import com.aleksbsc.tinkoffintechdevlife.helpers.Result
import com.aleksbsc.tinkoffintechdevlife.repository.DevLifeRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class DevLifeViewModel(
    private val repository: DevLifeRepository
) : ViewModel() {

    private val latestDevLifePictureList: MutableList<DevLifePictureData> = mutableListOf()
    private val randomDevLifePictureList: ArrayList<DevLifePictureData> = arrayListOf()
    private val topDevLifePictureList: ArrayList<DevLifePictureData> = arrayListOf()

    private var picturesCategory: MemesCategory = MemesCategory.LATEST

    private var currentLatest = -1
    private var latestPage = 0

    private var currentRandom = -1

    private var currentTop = -1
    private var topPage = 0

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _backButtonState = MutableLiveData<ConsumableValue<Int>>()
    val backButtonState: LiveData<ConsumableValue<Int>>
        get() = _backButtonState

    private val _showErrorWarning = MutableLiveData<ConsumableValue<MemesCategory?>>()
    val showErrorWarning: LiveData<ConsumableValue<MemesCategory?>>
        get() = _showErrorWarning

    private val _currentLatestDevLifeData = MutableLiveData<DevLifePictureData>()
    val currentLatestDevLifePictureData: LiveData<DevLifePictureData>
        get() = _currentLatestDevLifeData

    private val _currentRandomDevLifeData = MutableLiveData<DevLifePictureData>()
    val currentRandomDevLifePictureData: LiveData<DevLifePictureData>
        get() = _currentRandomDevLifeData

    private val _currentTopDevLifeData = MutableLiveData<DevLifePictureData>()
    val currentTopDevLifePictureData: LiveData<DevLifePictureData>
        get() = _currentTopDevLifeData


    fun fetchNextPicture(itemType: MemesCategory) {
        when (itemType) {
            MemesCategory.LATEST -> {
                if (latestDevLifePictureList.isNotEmpty() && currentLatest < latestDevLifePictureList.lastIndex) {
                    _currentLatestDevLifeData.value = latestDevLifePictureList[currentLatest + 1]
                    _backButtonState.value = ConsumableValue(++currentLatest)
                } else {
                    viewModelScope.launch() {
                        withContext(Main) {
                            loadingStart()
                            _showErrorWarning.value = null
                            launch(IO) {
                                when (val newPortion =
                                    repository.getNextDevLifeList(itemType, latestPage)) {
                                    is Result.Success -> {
                                        latestDevLifePictureList.addAll(newPortion.data)
                                        _currentLatestDevLifeData.postValue(newPortion.data.first())
                                        _backButtonState.postValue(ConsumableValue(++currentLatest))
                                        latestPage++
                                        loadingSuccess()
                                    }
                                    is Result.Error -> {
                                        _loadingState.postValue(LoadingState.error(newPortion.msg))
                                        networkErrorWarning(itemType)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            MemesCategory.TOP -> {
                if (topDevLifePictureList.isNotEmpty() && currentTop < topDevLifePictureList.lastIndex) {
                    _currentTopDevLifeData.value = topDevLifePictureList[currentTop + 1]
                    _backButtonState.value = ConsumableValue(++currentTop)
                } else {
                    viewModelScope.launch() {
                        withContext(Main) {
                            loadingStart()
                            _showErrorWarning.value = null
                            launch(IO) {
                                when (val newPortion =
                                    repository.getNextDevLifeList(itemType, topPage)) {
                                    is Result.Success -> {
                                        topDevLifePictureList.addAll(newPortion.data)
                                        _currentTopDevLifeData.postValue(newPortion.data.first())
                                        _backButtonState.postValue(ConsumableValue(++currentTop))
                                        topPage++
                                        loadingSuccess()
                                    }
                                    is Result.Error -> {
                                        _loadingState.postValue(LoadingState.error(newPortion.msg))
                                        networkErrorWarning(itemType)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            MemesCategory.RANDOM -> {
                if (randomDevLifePictureList.isNotEmpty() && currentRandom < randomDevLifePictureList.lastIndex) {
                    _currentRandomDevLifeData.value = randomDevLifePictureList[currentRandom + 1]
                    _backButtonState.value = ConsumableValue(++currentRandom)
                } else {
                    viewModelScope.launch() {
                        withContext(Main) {
                            loadingStart()
                            _showErrorWarning.value = null
                            launch(IO) {
                                when (val newSingle = repository.getSingleDevLifeList(itemType)) {
                                    is Result.Success -> {
                                        randomDevLifePictureList.add(newSingle.data)
                                        _currentRandomDevLifeData.postValue(newSingle.data)
                                        _backButtonState.postValue(ConsumableValue(++currentRandom))
                                        loadingSuccess()
                                    }
                                    is Result.Error -> {
                                        _loadingState.postValue(LoadingState.error(newSingle.msg))
                                        networkErrorWarning(itemType)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun errorImageLoading() {
        Timber.e("MyTAG_DevLifeViewModel_errorImageLoading() error: GLIDE ERROR")
        _loadingState.postValue(LoadingState.error(GLIDE_ERROR))
    }

    fun loadingStart() {
        _loadingState.postValue(LoadingState.LOADING)
    }

    fun loadingSuccess() {
        _loadingState.postValue(LoadingState.LOADED)
    }

    fun backIconClicked(type: MemesCategory) {
        when (type) {
            MemesCategory.LATEST -> {
                _currentLatestDevLifeData.value = latestDevLifePictureList[--currentLatest]
                _backButtonState.value = ConsumableValue(currentLatest)
            }
            MemesCategory.RANDOM -> {
                _currentRandomDevLifeData.value = randomDevLifePictureList[--currentRandom]
                _backButtonState.value = ConsumableValue(currentRandom)
            }
            MemesCategory.TOP -> {
                _currentTopDevLifeData.value = topDevLifePictureList[--currentTop]
                _backButtonState.value = ConsumableValue(currentTop)
            }
        }
    }

    private fun networkErrorWarning(itemType: MemesCategory) {
        _showErrorWarning.postValue(ConsumableValue(itemType))
    }

    fun saveCurrentType(selectedPicturesCategory: MemesCategory) {
        this.picturesCategory = selectedPicturesCategory
        when (selectedPicturesCategory) {
            MemesCategory.LATEST -> {
                _backButtonState.value = ConsumableValue(currentLatest)
                if (currentLatest < 0) {
                    fetchNextPicture(selectedPicturesCategory)
                }
            }
            MemesCategory.RANDOM -> {
                _backButtonState.value = ConsumableValue(currentRandom)
                if (currentRandom < 0) {
                    fetchNextPicture(selectedPicturesCategory)
                }
            }
            MemesCategory.TOP -> {
                _backButtonState.value = ConsumableValue(currentTop)
                if (currentTop < 0) {
                    fetchNextPicture(selectedPicturesCategory)
                }
            }
        }
    }

    fun getCurrentType(): MemesCategory {
        return picturesCategory
    }

    fun requestLastMem(itemType: MemesCategory) {
        when (itemType) {
            MemesCategory.LATEST -> {
                if (latestDevLifePictureList.size == 0) {
                    fetchNextPicture(itemType)
                } else {
                    _currentLatestDevLifeData.value = latestDevLifePictureList[currentLatest]
                }
            }
            MemesCategory.RANDOM -> {
                if (randomDevLifePictureList.size == 0) {
                    fetchNextPicture(itemType)
                } else {
                    _currentRandomDevLifeData.value = randomDevLifePictureList[currentRandom]
                }
            }
            MemesCategory.TOP -> {
                if (topDevLifePictureList.size == 0) {
                    fetchNextPicture(itemType)
                } else {
                    _currentTopDevLifeData.value = topDevLifePictureList[currentTop]
                }

            }
        }
    }

    fun reloadClicked() {
        requestLastMem(getCurrentType())
    }
}
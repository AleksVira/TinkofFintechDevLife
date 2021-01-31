package com.aleksbsc.tinkoffintechdevlife.di

import com.aleksbsc.tinkoffintechdevlife.network.NetworkDataConverter
import com.aleksbsc.tinkoffintechdevlife.repository.DevLifeRepository
import com.aleksbsc.tinkoffintechdevlife.vm.DevLifeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appKoinModule: Module = module {

    single { NetworkDataConverter() }

    single {
        DevLifeRepository(
            networkDataConverter = get(),
            devLifeService = get()
        )
    }

    viewModel {
        DevLifeViewModel(repository = get())
    }
}
package com.ricardomuniz.whichcatits.di

import com.ricardomuniz.whichcatits.data.repository.CatApiService
import com.ricardomuniz.whichcatits.data.repository.CatRepository
import com.ricardomuniz.whichcatits.data.repository.CatRepositoryImpl
import com.ricardomuniz.whichcatits.domain.usecase.GetCatDetailUseCase
import com.ricardomuniz.whichcatits.domain.usecase.GetCatListUseCase
import com.ricardomuniz.whichcatits.domain.usecase.GetCatMoreListUseCase
import com.ricardomuniz.whichcatits.presentation.viewmodel.CatDetailViewModel
import com.ricardomuniz.whichcatits.presentation.viewmodel.CatListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val modules = module {

    single { provideCatApiService() }

    single<CatRepository> { CatRepositoryImpl(get()) }

    single<GetCatListUseCase> { GetCatListUseCase(get()) }

    single<GetCatMoreListUseCase> { GetCatMoreListUseCase(get()) }

    single<GetCatDetailUseCase> { GetCatDetailUseCase(get()) }

    viewModel { CatListViewModel(catListUseCase = get(), catMoreListUseCase = get()) }

    viewModel { CatDetailViewModel(catDetailUseCase = get()) }

}

private fun provideCatApiService(): CatApiService {
    return Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatApiService::class.java)
}

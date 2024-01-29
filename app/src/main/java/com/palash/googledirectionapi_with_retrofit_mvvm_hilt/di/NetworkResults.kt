package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.di

import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.api_service.DirectionsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkResults {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder().baseUrl("https://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun providesDirectionsService(retrofit: Retrofit) : DirectionsAPI {
        return retrofit.create(DirectionsAPI::class.java)

    }
}
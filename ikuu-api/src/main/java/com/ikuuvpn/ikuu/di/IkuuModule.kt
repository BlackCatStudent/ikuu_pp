package com.ikuuvpn.ikuu.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ikuuvpn.ikuu.api.IkuuApiService
import com.ikuuvpn.ikuu.auth.IkuuAuthManager
import com.ikuuvpn.ikuu.parser.SubscriptionParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IkuuModule {

    private const val BASE_URL = "https://ikuuu.nl/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideIkuuApiService(retrofit: Retrofit): IkuuApiService {
        return retrofit.create(IkuuApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideIkuuAuthManager(
        apiService: IkuuApiService
    ): IkuuAuthManager {
        return IkuuAuthManager(apiService)
    }

    @Provides
    @Singleton
    fun provideSubscriptionParser(gson: Gson): SubscriptionParser {
        return SubscriptionParser(gson)
    }
}
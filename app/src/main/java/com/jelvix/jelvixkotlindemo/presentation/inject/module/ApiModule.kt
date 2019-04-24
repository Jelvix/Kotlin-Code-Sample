/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject.module

import android.content.Context
import com.google.gson.*
import com.jelvix.jelvixkotlindemo.BuildConfig
import com.jelvix.jelvixkotlindemo.data.api.IApodApi
import com.jelvix.jelvixkotlindemo.data.api.INasaApi
import com.jelvix.jelvixkotlindemo.data.api.exception.ApiErrorInterceptor
import com.jelvix.jelvixkotlindemo.data.manager.IApiManager
import com.jelvix.jelvixkotlindemo.data.manager.impl.ApiManager
import com.jelvix.jelvixkotlindemo.presentation.inject.qualifier.ApodApiQualifier
import com.jelvix.jelvixkotlindemo.presentation.inject.qualifier.NasaApiQualifier
import com.jelvix.jelvixkotlindemo.presentation.inject.qualifier.TokenQualifier
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.lang3.time.FastDateFormat
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ApiModule {

    companion object {
        const val CONNECTION_TIMEOUT = 30L
        const val BASE_URL_NASA_API = "https://images-api.nasa.gov/"
        const val BASE_URL_APOD_API = "https://api.nasa.gov/"
    }

    @Provides
    @TokenQualifier
    fun provideToken(): String? = "DEMO_KEY"

    @Provides
    @Singleton
    fun provideGson(applicationContext: Context): Gson {
        return GsonBuilder().apply {
            val fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
            registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, typeOfT, context ->
                val time = fastDateFormat.format(src)
                return@JsonSerializer JsonPrimitive(time)
            })
            registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, typeOfT, context ->
                try {
                    return@JsonDeserializer fastDateFormat.parse(json.asString)
                } catch (e: ParseException) {
                    throw RuntimeException("Date parsing error: ", e)
                }
            })
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            serializeNulls()
        }.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@TokenQualifier tokenProvider: Provider<String?>, applicationContext: Context, gson: Gson): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(ApiErrorInterceptor(applicationContext, gson))
            // authorization
            addNetworkInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = if (originalHttpUrl.toString().startsWith(BASE_URL_APOD_API)) {
                    originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", tokenProvider.get())
                        .build()
                } else {
                    originalHttpUrl.newBuilder().build()
                }
                val requestBuilder = original.newBuilder().url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            // logging
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.HEADERS
                addNetworkInterceptor(interceptor)
                addInterceptor(ChuckInterceptor(applicationContext))
            }
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    @NasaApiQualifier
    fun provideNasaRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL_NASA_API)
                .build()
    }

    @Provides
    @Singleton
    @ApodApiQualifier
    fun provideApodRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl(BASE_URL_APOD_API)
            .build()
    }


    @Provides
    @Singleton
    fun provideNasaApi(@NasaApiQualifier retrofit: Retrofit): INasaApi = retrofit.create<INasaApi>(INasaApi::class.java)

    @Provides
    @Singleton
    fun provideApodApi(@ApodApiQualifier retrofit: Retrofit): IApodApi = retrofit.create<IApodApi>(IApodApi::class.java)

    @Provides
    @Singleton
    fun provideApiManager(nasaApi: INasaApi, apodApi: IApodApi): IApiManager = ApiManager(nasaApi, apodApi)
}

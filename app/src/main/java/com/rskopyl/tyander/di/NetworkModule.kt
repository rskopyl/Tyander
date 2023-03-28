package com.rskopyl.tyander.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rskopyl.tyander.data.remote.BiographyApi
import com.rskopyl.tyander.data.remote.ImageApi
import com.rskopyl.tyander.data.remote.MessageApi
import com.rskopyl.tyander.di.NetworkModule.Names.JSON_CONVERTER_FACTORY
import com.rskopyl.tyander.di.NetworkModule.Names.SCALARS_CONVERTER_FACTORY
import com.rskopyl.tyander.util.BIOGRAPHY_API_BASE_URL
import com.rskopyl.tyander.util.IMAGE_API_BASE_URL
import com.rskopyl.tyander.util.MESSAGE_API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @[Provides Named(JSON_CONVERTER_FACTORY)]
    fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @[Provides Named(SCALARS_CONVERTER_FACTORY)]
    fun provideScalarsConverterFactory(): Converter.Factory =
        ScalarsConverterFactory.create()

    @Provides
    @Singleton
    fun provideImageApi(
        @Named(JSON_CONVERTER_FACTORY)
        jsonConverterFactory: Converter.Factory
    ): ImageApi {
        return Retrofit.Builder()
            .baseUrl(IMAGE_API_BASE_URL)
            .addConverterFactory(jsonConverterFactory)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideBiographyApi(
        @Named(JSON_CONVERTER_FACTORY)
        jsonConverterFactory: Converter.Factory
    ): BiographyApi {
        return Retrofit.Builder()
            .baseUrl(BIOGRAPHY_API_BASE_URL)
            .addConverterFactory(jsonConverterFactory)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideMessageApi(
        @Named(SCALARS_CONVERTER_FACTORY)
        scalarConverterFactory: Converter.Factory
    ): MessageApi {
        return Retrofit.Builder()
            .baseUrl(MESSAGE_API_BASE_URL)
            .addConverterFactory(scalarConverterFactory)
            .build()
            .create()
    }

    private object Names {

        const val JSON_CONVERTER_FACTORY = "json_converter_factory"
        const val SCALARS_CONVERTER_FACTORY = "scalars_converter_factory"
    }
}
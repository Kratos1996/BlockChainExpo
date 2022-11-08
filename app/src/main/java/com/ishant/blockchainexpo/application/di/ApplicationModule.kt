package com.ishant.blockchainexpo.application.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.google.gson.Gson
import com.ishant.blockchainexpo.application.constance.AppConstance.APP_NAME
import com.ishant.blockchainexpo.application.constance.AppConstance.BASE_URL
import com.ishant.blockchainexpo.application.database.AppDB
import com.ishant.blockchainexpo.application.database.CoinDao
import com.ishant.blockchainexpo.application.websocket.WebSocketRepository
import com.ishant.blockchainexpo.application.websocket.WebSocketRepositoryImpl
import com.ishant.blockchainexpo.data.datasource.CoinDatasource
import com.ishant.blockchainexpo.data.remote.ApiInterface
import com.ishant.blockchainexpo.data.repository.CoinRepository
import com.ishant.blockchainexpo.data.repository.CoinRepositoryImpl
import com.ishant.blockchainexpo.data.repository.database.DatabaseRepository
import com.ishant.blockchainexpo.data.repository.database.DatabaseRepositoryImpl
import com.ishant.blockchainexpo.domain.usecase.CoinUseCase
import com.ishant.blockchainexpo.domain.usecase.CoinUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun provideConnectivityManager( @ApplicationContext context:Context):ConnectivityManager{
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()



    @Provides
    fun provideDispatcher(): DispatchersProviders = object : DispatchersProviders {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }


    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient()= HttpClient(CIO) {
        install(Logging)
        install(WebSockets) {
        }
        install(ContentNegotiation) {
            json()
        }
    }

    @Provides
    @Singleton
    fun provideWebSocketRepository(): WebSocketRepository=WebSocketRepositoryImpl(provideHttpClient())

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, APP_NAME)
            .fallbackToDestructiveMigration().build()
    }
    @Provides
    @Singleton
    fun providesPostDao(db: AppDB): CoinDao = db.getDao()
    @Provides
    @Singleton
    fun provideDatabaseRepository(db: AppDB): DatabaseRepository = DatabaseRepositoryImpl(db = db)

    @Provides
    @Singleton
    fun getCoinDataSource(coinDao:DatabaseRepository): CoinDatasource= CoinDatasource( coinDao )

    @Provides
    @Singleton
    fun getPagingConfig(): PagingConfig= PagingConfig(
        pageSize = 55,
        enablePlaceholders = false,
        initialLoadSize = 20
    )

    //Api repos and Use cases


    @Provides
    @Singleton
    fun provideRemoteRepository(api: ApiInterface): CoinRepository {
        return CoinRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCoinUseCase(
        repository: CoinRepository
    ): CoinUseCase {
        return CoinUseCaseImpl(repository, provideGson())
    }

}
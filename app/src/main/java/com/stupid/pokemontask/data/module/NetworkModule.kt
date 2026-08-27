package com.stupid.pokemontask.data.module

import com.stupid.pokemontask.data.network.PokemonRepositoryImp
import com.stupid.pokemontask.data.network.PokemonService
import com.stupid.pokemontask.domain.usecase.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//will create the instance
@Singleton
@Module
class NetworkModule {

    private val baseURL = "https://pokeapi.co/api/v2/pokemon"

    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideAPIService(retrofit: Retrofit) : PokemonService {
        return retrofit.create(PokemonService::class.java)
    }

    @Binds
    fun provideRepository(apiService: PokemonService): PokemonRepository {
        return PokemonRepositoryImp(apiService)
    }
}
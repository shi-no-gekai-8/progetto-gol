package com.example.laboratorio4

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    Questo oggetto contiene la configurazione base di Retrofit.

    Lo creiamo come object perché ci basta una sola istanza condivisa
    in tutta l'app.
*/
object RetrofitClient {

    /*
        Base URL del servizio OMDb.

        Deve sempre terminare con "/" perché Retrofit lo richiede
        per costruire correttamente gli endpoint.
    */
    private const val BASE_URL = "https://www.omdbapi.com/"

    /*
        Qui costruiamo Retrofit.

        addConverterFactory(GsonConverterFactory.create())
        dice a Retrofit di usare Gson per trasformare il JSON
        in una data class Kotlin.
    */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /*
        Creiamo il servizio API partendo dall'interfaccia OmdbApiService.

        Da questo momento potremo chiamare:
        RetrofitClient.api.getMovieByTitle(...)
    */
    val api: OmdbApiService = retrofit.create(OmdbApiService::class.java)
}
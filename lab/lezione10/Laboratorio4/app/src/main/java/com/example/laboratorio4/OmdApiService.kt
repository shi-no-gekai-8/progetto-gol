package com.example.laboratorio4

import retrofit2.http.GET
import retrofit2.http.Query

/*
    Questa interfaccia descrive le chiamate che possiamo fare al server OMDb.

    Retrofit leggerà questa interfaccia e creerà automaticamente il codice
    necessario per fare la richiesta HTTP.
*/
interface OmdbApiService {

    /*
        @GET("/")
        Indica che faremo una richiesta GET alla radice del sito:
        https://www.omdbapi.com/

        @Query("apikey")
        Aggiunge alla URL il parametro apikey.

        @Query("t")
        Aggiunge alla URL il titolo del film da cercare.

        Esempio finale:
        https://www.omdbapi.com/?apikey=LA_TUA_API_KEY&t=Batman
    */
    @GET("/")
    suspend fun getMovieByTitle(
        @Query("apikey") apiKey: String,
        @Query("t") title: String
    ): MovieResponse
}
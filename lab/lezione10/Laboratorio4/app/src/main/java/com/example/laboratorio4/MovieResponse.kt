package com.example.laboratorio4

import com.google.gson.annotations.SerializedName

/*
    Questa data class rappresenta il JSON che arriva da OMDb.

    OMDb usa nomi con la lettera maiuscola:
    "Title", "Year", "Plot", "Poster".

    In Kotlin preferiamo usare nomi in minuscolo:
    title, year, plot, poster.

    Per collegare i nomi del JSON ai nomi Kotlin usiamo @SerializedName.
*/
data class MovieResponse(
    @SerializedName("Title")
    val title: String?,

    @SerializedName("Year")
    val year: String?,

    @SerializedName("Plot")
    val plot: String?,

    @SerializedName("Poster")
    val poster: String?
)
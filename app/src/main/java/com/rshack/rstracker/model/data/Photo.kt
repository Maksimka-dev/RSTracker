package com.rshack.rstracker.model.data

import com.squareup.moshi.Json

data class Photo(
    @Json(name = "thumb")
    val thumb: String
)

data class Results(
    @Json(name = "results")
    val results: List<Urls>
)

data class Urls(
    @Json(name = "urls")
    val photo: Photo
)

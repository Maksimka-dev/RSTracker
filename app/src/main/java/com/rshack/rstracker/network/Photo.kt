package com.rshack.rstracker.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    @Json(name = "thumb")
    val thumb: String
) : Parcelable

@Parcelize
data class Results(
    @Json(name = "results")
    val results: List<Urls>
) : Parcelable

@Parcelize
data class Urls(
    @Json(name = "urls")
    val photo: Photo
) : Parcelable
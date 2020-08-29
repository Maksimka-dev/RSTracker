package com.rshack.rstracker.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val id: String,
    val date: Long,
    val distance: Float,
    val time: Long,
    var imgUrl: String = ""
) : Parcelable

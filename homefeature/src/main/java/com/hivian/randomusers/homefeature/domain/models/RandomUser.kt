package com.hivian.randomusers.homefeature.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address (

    val city: String,

    val state: String,

    val country: String,

    val latitude: Double,

    val longitude: Double

): Parcelable

@Parcelize
data class Dob(
    val age: Int,

    val date: String
): Parcelable

@Parcelize
data class RandomUser(

    val id: Int,

    val gender: String,

    val firstName: String,

    val lastName: String,

    val email: String,

    val phone: String,

    val cell: String,

    val picture: String,

    val address: Address,

    val dob: Dob,

    val nat: String,




    ): Parcelable {

    val fullName : String
        get() = "$firstName $lastName"

}

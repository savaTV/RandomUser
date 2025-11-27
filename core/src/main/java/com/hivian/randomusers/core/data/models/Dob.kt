package com.hivian.randomusers.core.data.models

data class Dob(
    val age: Int = 0,
    val date: String = "")
{
    companion object {
        val EMPTY = Dob()
    }
}
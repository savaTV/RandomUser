package com.hivian.randomusers.core.data.local.converters

import androidx.room.TypeConverter
import com.hivian.randomusers.core.data.models.Dob
import com.hivian.randomusers.core.domain.extensions.fromJson
import com.hivian.randomusers.core.domain.extensions.toJson

class DobConverter {

    @TypeConverter
    fun dobToJson(dob: Dob?): String? {
        return dob?.toJson()
    }

    @TypeConverter
    fun jsonToDob(json: String?): Dob? {
        return json?.fromJson<Dob>()
    }
}
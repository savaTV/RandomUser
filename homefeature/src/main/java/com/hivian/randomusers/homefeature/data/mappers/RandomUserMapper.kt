package com.hivian.randomusers.homefeature.data.mappers

import com.hivian.randomusers.core.data.models.RandomUserDTO
import com.hivian.randomusers.homefeature.domain.models.Address
import com.hivian.randomusers.homefeature.domain.models.Dob
import com.hivian.randomusers.homefeature.domain.models.RandomUser

enum class ImageSize {
    THUMBNAIL,
    MEDIUM,
    LARGE
}

fun RandomUserDTO.mapToRandomUser(imageSize: ImageSize): RandomUser {
    return RandomUser(
        id = localId,
        gender = gender,
        firstName = name.first,
        lastName = name.last,
        email = email,
        phone = phone,
        nat = nat,
        cell = cell,
        picture = when (imageSize) {
            ImageSize.THUMBNAIL -> picture.thumbnail
            ImageSize.MEDIUM -> picture.medium
            ImageSize.LARGE -> picture.large
        },
        address = Address(
            city = location.city,
            state = location.state,
            country = location.country,
            latitude = location.coordinates.latitude,
            longitude = location.coordinates.longitude,
        ),
        dob = Dob(
            age = dob.age,
            date = dob.date

        )
    )


}

fun List<RandomUserDTO>.mapToRandomUsers(imageSize: ImageSize): List<RandomUser> {
    return map { it.mapToRandomUser(imageSize) }
}

package hu.bme.aut.android.kliensalk_hf_2_android.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithReviews(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userCreatorId"
    )
    val reviews: List<Review>
)
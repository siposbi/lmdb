package hu.bme.aut.android.lmdb.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithMovies(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userCreatorId"
    )
    val movies: List<Movie>
)
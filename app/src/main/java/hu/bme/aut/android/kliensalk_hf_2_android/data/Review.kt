package hu.bme.aut.android.kliensalk_hf_2_android.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey(autoGenerate = true) val reviewId: Long = 0,
    val userCreatorId: Long,
    val title: String,
    val year : String,
    val genre: String,
    val plot: String,
    val posterUrl: String
)
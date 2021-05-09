package hu.bme.aut.android.kliensalk_hf_2_android.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Review(
    @PrimaryKey(autoGenerate = true) val reviewId: Long = 0,
    val userCreatorId: Long,
    val title: String,
    val year: String,
    val genre: String,
    val plot: String,
    val posterUrl: String
) : Parcelable
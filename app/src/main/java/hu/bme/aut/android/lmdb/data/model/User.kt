package hu.bme.aut.android.lmdb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val username: String,
    val password: String
)
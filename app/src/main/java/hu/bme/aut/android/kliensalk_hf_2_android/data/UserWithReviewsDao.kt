package hu.bme.aut.android.kliensalk_hf_2_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserWithReviewsDao {

    @Insert
    suspend fun register(user: User)

    @Transaction
    @Query("SELECT * FROM User WHERE username = :username and password = :password")
    suspend fun login(username: String, password: String): UserWithReviews
}
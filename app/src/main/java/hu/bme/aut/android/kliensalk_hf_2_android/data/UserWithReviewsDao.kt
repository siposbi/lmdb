package hu.bme.aut.android.kliensalk_hf_2_android.data

import androidx.room.*
import hu.bme.aut.android.kliensalk_hf_2_android.data.model.Review
import hu.bme.aut.android.kliensalk_hf_2_android.data.model.User

@Dao
interface UserWithReviewsDao {

    @Insert
    suspend fun register(user: User)

    @Transaction
    @Query("SELECT * FROM User WHERE UPPER(username) = UPPER(:username) and password = :password")
    suspend fun login(username: String, password: String): User

    @Transaction
    @Query("SELECT * FROM Review WHERE userCreatorId = :userId")
    fun getReviewsForUser(userId: Long): List<Review>

    @Query("SELECT COUNT(*) FROM User WHERE UPPER(username) = UPPER(:username)")
    suspend fun checkIfExists(username: String): Int

    @Update
    suspend fun updateReview(review: Review)

    @Insert
    suspend fun insertReview(review: Review): Long

    @Delete
    suspend fun deleteReview(review: Review)
}
package hu.bme.aut.android.kliensalk_hf_2_android.data

import androidx.room.*

@Dao
interface UserWithReviewsDao {

    @Insert
    suspend fun register(user: User)

    @Transaction
    @Query("SELECT * FROM User WHERE username = :username and password = :password")
    suspend fun login(username: String, password: String): User

    @Transaction
    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserData(userId: Long): UserWithReviews

    @Query("SELECT COUNT(*) FROM User WHERE username = :username")
    suspend fun checkIfExists(username: String): Int

    @Update
    suspend fun updateReview(review: Review)

    @Insert
    suspend fun insertReview(review: Review): Long

    @Delete
    suspend fun deleteReview(review: Review)
}
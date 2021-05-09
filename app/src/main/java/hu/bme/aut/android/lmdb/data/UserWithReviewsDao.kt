package hu.bme.aut.android.lmdb.data

import androidx.room.*
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.data.model.User

@Dao
interface UserWithReviewsDao {

    @Insert
    suspend fun register(user: User)

    @Transaction
    @Query("SELECT * FROM User WHERE UPPER(username) = UPPER(:username) and password = :password")
    suspend fun login(username: String, password: String): User

    @Transaction
    @Query("SELECT * FROM Movie WHERE userCreatorId = :userId")
    fun getReviewsForUser(userId: Long): List<Movie>

    @Query("SELECT COUNT(*) FROM User WHERE UPPER(username) = UPPER(:username)")
    suspend fun checkIfExists(username: String): Int

    @Update
    suspend fun updateReview(movie: Movie)

    @Insert
    suspend fun insertReview(movie: Movie): Long

    @Delete
    suspend fun deleteReview(movie: Movie)
}
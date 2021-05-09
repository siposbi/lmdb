package hu.bme.aut.android.lmdb.data

import androidx.room.*
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.data.model.User

@Dao
interface UserWithReviewsDao {

    @Insert
    suspend fun register(user: User)

    @Query("SELECT * FROM User WHERE UPPER(username) = UPPER(:username) and password = :password")
    suspend fun login(username: String, password: String): User

    @Query("SELECT COUNT(*) FROM User WHERE UPPER(username) = UPPER(:username)")
    suspend fun checkIfExists(username: String): Int

    @Insert
    suspend fun insertReview(movie: Movie): Long

    @Query("SELECT * FROM Movie WHERE userCreatorId = :userId")
    fun getReviewsForUser(userId: Long): List<Movie>

    @Update
    suspend fun updateReview(movie: Movie)

    @Delete
    suspend fun deleteReview(movie: Movie)
}
package hu.bme.aut.android.lmdb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.data.model.User

@Database(entities = [User::class, Movie::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userWithReviewsDao(): UserWithReviewsDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "lmdb_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package hu.bme.aut.android.kliensalk_hf_2_android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Review::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        fun getDatabase(applicationContext: Context): UserDatabase {
            return Room.databaseBuilder(
                applicationContext,
                UserDatabase::class.java,
                "user-reviews"
            ).build()
        }
    }

    abstract fun userWithReviewsDao(): UserWithReviewsDao
}
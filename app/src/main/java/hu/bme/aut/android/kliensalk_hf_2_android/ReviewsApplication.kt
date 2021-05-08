package hu.bme.aut.android.kliensalk_hf_2_android

import android.app.Application
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserDatabase
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserRepository

class ReviewsApplication : Application() {
    val database by lazy { UserDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userWithReviewsDao()) }
}
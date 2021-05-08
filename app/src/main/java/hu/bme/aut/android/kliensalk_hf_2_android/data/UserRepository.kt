package hu.bme.aut.android.kliensalk_hf_2_android.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserRepository (private val wordDao: UserWithReviewsDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    var allWords: Flow<List<Review>> = wordDao.getUserData(0)

    fun updateAllWords(id: Long){
       allWords = wordDao.getUserData(id)
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Review) {
        wordDao.insertReview(word)
    }
}
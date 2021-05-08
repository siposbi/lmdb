package hu.bme.aut.android.kliensalk_hf_2_android.viewmodel

import androidx.lifecycle.*
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserRepository
import kotlinx.coroutines.launch


class ReviewViewModel(private val repository: UserRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    var allWords: LiveData<List<Review>> = repository.allWords.asLiveData()

    fun updateAllWords(id: Long){
        repository.updateAllWords(id)
        allWords = repository.allWords.asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Review) = viewModelScope.launch {
        repository.insert(word)
    }
}

class WordViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
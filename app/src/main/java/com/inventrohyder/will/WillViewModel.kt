package com.inventrohyder.will

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class WillViewModel(private val repository: WillRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWills: LiveData<List<Will>> = repository.allWills.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(will: Will) = viewModelScope.launch {
        repository.insert(will)
    }
}

class WillViewModelFactory(private val repository: WillRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WillViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
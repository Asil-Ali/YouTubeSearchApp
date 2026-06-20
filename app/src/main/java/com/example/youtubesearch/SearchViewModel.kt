package com.example.youtubesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubesearch.model.VideoItem
import com.example.youtubesearch.repository.YoutubeRepository
import kotlinx.coroutines.launch

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(val videos: List<VideoItem>) : SearchState()
    data class Error(val message: String) : SearchState()
}

class SearchViewModel : ViewModel() {

    private val repository = YoutubeRepository()

    private val _state = MutableLiveData<SearchState>(SearchState.Idle)
    val state: LiveData<SearchState> = _state

    fun search(query: String, sortOrder: String = "relevance") {
        if (query.isBlank()) {
            _state.value = SearchState.Error("EMPTY_INPUT")
            return
        }

        _state.value = SearchState.Loading

        viewModelScope.launch {
            val result = repository.search(query.trim(), sortOrder)
            result.fold(
                onSuccess = { videos ->
                    _state.value = SearchState.Success(videos)
                },
                onFailure = { error ->
                    _state.value = SearchState.Error(error.message ?: "UNKNOWN_ERROR")
                }
            )
        }
    }
}

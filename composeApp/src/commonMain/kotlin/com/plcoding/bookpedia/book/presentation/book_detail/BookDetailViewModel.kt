package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookDetailViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: BookDetailAction) {
        when (action) {
            BookDetailAction.OnBackClick -> {

            }

            BookDetailAction.OnFavoriteClick -> {
            }

            is BookDetailAction.OnSelectedBookChanged -> {
                _state.value = _state.value.copy(
                    book = action.book
                )
            }
        }
    }
}
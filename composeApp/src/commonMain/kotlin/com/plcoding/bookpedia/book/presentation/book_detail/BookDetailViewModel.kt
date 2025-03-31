package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plcoding.bookpedia.app.Route
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailViewModel(
    val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart { updateBookDescription() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    val bookId = savedStateHandle.toRoute<Route.BookDetails>().bookId

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

    fun updateBookDescription() {
        viewModelScope.launch {
            val description = bookRepository
                .getBookDescription(bookId)
                .onSuccess { description ->
                    _state.value = _state.value.copy(
                        book = _state.value.book?.copy(
                            description = description
                        ),
                        isLoading = false
                    )
                }

        }
    }
}
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailViewModel(
    val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            updateBookDescription()
            observeFavoriteStatus()
        }
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
                _state.value.book?.let { book ->
                    viewModelScope.launch {
                        if (_state.value.isFavorite) {
                            bookRepository.deleteFavorite(bookId = bookId)
                        } else {
                            bookRepository.markAsFavorite(book = book)
                        }
                    }
                }
            }

            is BookDetailAction.OnSelectedBookChanged -> {
                _state.value = _state.value.copy(
                    book = action.book
                )
            }
        }
    }

    private fun observeFavoriteStatus() {
        bookRepository.isBookFavorite(bookId).distinctUntilChanged().onEach {
            _state.value = _state.value.copy(
                isFavorite = it
            )
        }.launchIn(
            scope = viewModelScope
        )

    }


    fun updateBookDescription() {
        viewModelScope.launch {
            bookRepository
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
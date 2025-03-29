package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toStringResource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val dataSource: BookRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(BookListState())
    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null

    val state = _state
        .onStart {
            if (cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnBookClick -> {

            }

            is BookListAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            is BookListAction.onTabSelected -> {
                _state.update { it.copy(selectedTabIndex = action.index) }

            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500)
            .onEach {
                when {
                    it.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedBooks
                            )
                        }
                    }

                    it.length > 2 -> {
                        searchJob?.let { job -> if (job.isActive) job.cancel() }
                        searchBooks(it)
                    }
                }
            }.launchIn(viewModelScope)

    }

    private fun searchBooks(query: String) {
        _state.update { it.copy(isLoading = true) }

        searchJob = viewModelScope.launch {
            dataSource
                .searchBooks(query)
                .onSuccess { sarchResults ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchResults = sarchResults
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchResults = emptyList(),
                            errorMessage = error.toStringResource()
                        )
                    }
                }
        }
    }
}
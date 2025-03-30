package com.plcoding.bookpedia.book.presentation.book_detail

import com.plcoding.bookpedia.book.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val book: Book? = null,
    val isFavorite: Boolean = false,
)
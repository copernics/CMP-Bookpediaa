package com.plcoding.bookpedia.book.presentation.book_detail

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookDetailAction {
    data object OnFavoriteClick : BookDetailAction
    data object OnBackClick : BookDetailAction
    data class OnSelectedBookChanged(val book: Book) : BookDetailAction
}
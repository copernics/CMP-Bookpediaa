package com.plcoding.bookpedia.app

import kotlinx.serialization.Serializable

interface Route {
    @Serializable
    data object BookList : Route

    @Serializable
    data class BookDetails(val bookId: String) : Route

    @Serializable
    data object BookGraph : Route

}
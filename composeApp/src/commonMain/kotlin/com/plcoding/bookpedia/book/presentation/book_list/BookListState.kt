package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText
import kotlin.random.Random

data class BookListState(
    val searchQuery: String = "Kotlin",
    val searchResults:List<Book> = books,
    val favoriteBooks:List<Book> = emptyList(),
    val isLoading:Boolean = false,
    val selectedTabIndex:Int = 0,
    val errorMessage: UiText? = null,
)

val books = (1..100).map {
    Book(
        id = it.toString(),
        title = "Book#$it",
        languages = listOf("Eng"),
        numEditions = 1,
        imageUrl = "https://www.reddit.com/r/Kotlin/",
        authors = listOf("Ina Defio"),
        description = "",
        firstPublishingYear = "1911",
        averageRating = Random.nextDouble(2.0,5.0),
        numPages = Random.nextInt(100,500),
        ratingCount = Random.nextInt(10,200)
    )
}
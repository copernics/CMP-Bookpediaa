package com.plcoding.bookpedia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreen
import com.plcoding.bookpedia.book.presentation.book_list.BookListState
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import kotlin.random.Random

@Preview
@Composable
private fun BookSearchBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        BookSearchBar(
            searchingQuery = "Ko",
            onSearchQueryChange = { },
            onImeSearch = { },
        )
    }
}



@Preview
@Composable
private fun BookListScreenPreview() {
    BookListScreen(
        state = BookListState(
            selectedTabIndex = 0,
            searchQuery = "Kotlin",
            isLoading = false,
            favoriteBooks = emptyList(),
            errorMessage = null,
            searchResults = books
        ),
        onAction = { }
    )
}


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
        averageRating = Random.nextDouble(2.0, 5.0),
        numPages = Random.nextInt(100, 500),
        ratingCount = Random.nextInt(10, 200)
    )
}
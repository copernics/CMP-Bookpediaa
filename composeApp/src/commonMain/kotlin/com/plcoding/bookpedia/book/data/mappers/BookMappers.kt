package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id,
        title = title,
        imageUrl = if (coverEditionKey != null) {
            "https://covers.openlibrary.org/b/olid/${coverEditionKey}-L.jpg"
        } else {
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey ?: 0}-L.jpg"
        },
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishingYear = firstPublishYear?.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numberOfPagesMedian,
        numEditions = editionCount ?: 0,)
}
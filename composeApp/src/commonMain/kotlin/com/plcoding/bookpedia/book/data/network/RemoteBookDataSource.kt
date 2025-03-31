package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String, resultLimit: Int = 100,
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDescription(bookId: String): Result<BookWorkDto, DataError.Remote>
}
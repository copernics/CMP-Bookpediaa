package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient,
):RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String, resultLimit: Int,
    ): Result<SearchResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/search.json") {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter(
                    "fields",
                    "key,title,cover_i,author_name,first_publish_year," +
                            "ratings_average,ratings_count,number_of_pages_median," +
                            "edition_count,language,cover_edition_key"
                )
            }
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<BookWorkDto, DataError.Remote> {
        return safeCall<BookWorkDto> {
            httpClient.get("$BASE_URL/works/$bookId.json")
        }
    }
}
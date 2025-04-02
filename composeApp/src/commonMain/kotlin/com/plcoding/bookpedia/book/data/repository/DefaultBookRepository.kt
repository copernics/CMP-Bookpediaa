package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavoriteBookDao
import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.mappers.toBookEntity
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao,
) : BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource.searchBooks(query = query)
            .map { dto -> dto.results.map { it.toBook() } }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val local = favoriteBookDao.getBookById(id = bookId)
        return if (local != null) {
            Result.Success(local.description)
        } else {
            remoteBookDataSource.getBookDescription(bookId = bookId)
                .map { dto -> dto.description }
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao.getAllFavoriteBooks().map {
            it.map { bookEntity -> bookEntity.toBook() }
        }
    }

    override fun isBookFavorite(bookId: String): Flow<Boolean> {
        return favoriteBookDao.getAllFavoriteBooks().map {
            it.any { bookEntity -> bookEntity.id == bookId }
        }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> =
        safeDbCall { favoriteBookDao.upsertBook(book.toBookEntity()) }

    override suspend fun deleteFavorite(bookId: String): EmptyResult<DataError.Local> =
        safeDbCall { favoriteBookDao.deleteBookById(bookId) }

}

private inline fun <T> safeDbCall(action: () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(action())
    } catch (e: SQLiteException) {
        Result.Error(DataError.Local.SQL_ERROR)
    }
}
package com.plcoding.bookpedia.core.presentation

import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.error_disk_full
import cmp_bookpedia.composeapp.generated.resources.error_no_internet
import cmp_bookpedia.composeapp.generated.resources.error_request_timeout
import cmp_bookpedia.composeapp.generated.resources.error_serialization
import cmp_bookpedia.composeapp.generated.resources.error_server
import cmp_bookpedia.composeapp.generated.resources.error_sql_error
import cmp_bookpedia.composeapp.generated.resources.error_too_many_requests
import cmp_bookpedia.composeapp.generated.resources.error_unknown
import com.plcoding.bookpedia.core.domain.DataError

fun DataError.toStringResource(): UiText {
    val stringResourceId = (
            when (this) {
                DataError.Local.DISK_FULL -> Res.string.error_disk_full
                DataError.Local.UNKNOWN_ERROR -> Res.string.error_unknown
                DataError.Local.SQL_ERROR -> Res.string.error_sql_error
                DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
                DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
                DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
                DataError.Remote.SERVER_ERROR -> Res.string.error_server
                DataError.Remote.UNKNOWN_ERROR -> Res.string.error_unknown
                DataError.Remote.SERIALIZATION_ERROR -> Res.string.error_serialization
            }
            )
    return UiText.StringResourceId(stringResourceId)
}
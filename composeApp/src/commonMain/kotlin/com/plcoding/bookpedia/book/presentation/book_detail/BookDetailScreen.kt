@file:OptIn(ExperimentalLayoutApi::class)

package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.languages
import cmp_bookpedia.composeapp.generated.resources.no_description
import cmp_bookpedia.composeapp.generated.resources.pages
import cmp_bookpedia.composeapp.generated.resources.rating
import cmp_bookpedia.composeapp.generated.resources.synopsis
import com.plcoding.bookpedia.book.presentation.book_detail.components.BlurredImageBackground
import com.plcoding.bookpedia.book.presentation.book_detail.components.BookChip
import com.plcoding.bookpedia.book.presentation.book_detail.components.ChipSize
import com.plcoding.bookpedia.book.presentation.book_detail.components.TitleContent
import com.plcoding.bookpedia.core.presentation.PulseAnimation
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailScreen(
        modifier = Modifier,
        state = state,
        onAction = { action ->
            when (action) {
                is BookDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

//    LaunchedEffect(state.book) {
//        delay(1000)
//        snackbarHostState.showSnackbar(
//            message = state.book?.toString() ?: "",
//            withDismissAction = true,
//            actionLabel = "Dismiss"
//        )
//    }
    Box(modifier = Modifier.fillMaxSize()) {

        BlurredImageBackground(
            modifier = Modifier.fillMaxSize(),
            imageUrl = state.book?.imageUrl,
            isFavorite = state.isFavorite,
            onFavoriteClick = { onAction(BookDetailAction.OnFavoriteClick) },
            onBackClick = { onAction(BookDetailAction.OnBackClick) },
            {
                if (state.book != null) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 700.dp)
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp)
                            .verticalScroll(
                                rememberScrollState()
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = state.book.title,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        if (state.book.authors.isNotEmpty())
                            Text(
                                text = state.book.authors.joinToString(),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                32.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.book.averageRating?.let { rating ->
                                TitleContent(
                                    title = stringResource(Res.string.rating)
                                ) {
                                    BookChip {

                                        Text(
                                            text = "${round(rating * 10) / 10}",
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = stringResource(Res.string.rating),
                                            tint = SandYellow
                                        )

                                    }
                                }

                            }
                            state.book.numPages?.let { pages ->
                                TitleContent(
                                    title = stringResource(Res.string.pages)
                                ) {
                                    BookChip {
                                        Text(
                                            text = "$pages",
                                        )
                                    }
                                }

                            }
                        }
                        if (state.book.languages.isNotEmpty()) {
                            TitleContent(
                                title = stringResource(Res.string.languages),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.wrapContentSize()
                                ) {
                                    state.book.languages.forEach {
                                        BookChip(
                                            shipSize = ChipSize.SMALL,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        ) {
                                            Text(
                                                text = it.uppercase(),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Text(
                            text = stringResource(Res.string.synopsis),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 8.dp),
                        )

                        if (state.isLoading) {
                            PulseAnimation(Modifier.size(60.dp))
//                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = if (state.book.description.isNullOrEmpty())
                                    stringResource(Res.string.no_description)
                                else
                                    state.book.description,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Justify,
                                color = if (!state.book.description.isNullOrEmpty())
                                    Color.Black.copy(alpha = 0.8f) else Color.Black,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

    }
}
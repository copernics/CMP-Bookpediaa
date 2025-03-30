package com.plcoding.bookpedia.book.presentation.book_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.book_cover
import cmp_bookpedia.composeapp.generated.resources.book_error_2
import cmp_bookpedia.composeapp.generated.resources.go_back
import cmp_bookpedia.composeapp.generated.resources.mark_as_favorite
import cmp_bookpedia.composeapp.generated.resources.remove_from_favorites
import coil3.compose.rememberAsyncImagePainter
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlurredImageBackground(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    var imageResult by remember { mutableStateOf<Result<Painter>?>(null) }
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            val size = it.painter.intrinsicSize
            imageResult = if (size.height > 1 && size.width > 1) {
                Result.success(it.painter)
            } else {
                Result.failure(Exception("Invalid image size"))
            }
        },
        onError = {

            imageResult = Result.failure(it.result.throwable)
        }
    )

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(DarkBlue)
            ) {

                imageResult?.getOrNull()?.let { painter ->
                    Image(
                        painter = painter,
                        contentDescription = stringResource(Res.string.book_cover),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(
                                20.dp,
                                BlurredEdgeTreatment(RoundedCornerShape(5.dp))
                            )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .background(DesertWhite)
            ) {

            }
        }
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .statusBarsPadding()
        ) {
            Icon(
                tint = Color.White,
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(Res.string.go_back)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.fillMaxHeight(0.15f)
            )
            ElevatedCard(
                modifier = Modifier
                    .height(230.dp)
                    .aspectRatio(2 / 3f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(
                    contentColor = Color.Transparent
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 15.dp
                )
            ) {
                AnimatedContent(
                    targetState = imageResult
                ) { result ->
                    when (result) {
                        null -> {
                            CircularProgressIndicator()
                        }

                        else -> {
                            Box {
                                Image(
                                    painter = if (result.isSuccess)
                                        painter
                                    else
                                        painterResource(Res.drawable.book_error_2),
                                    contentDescription = stringResource(Res.string.book_cover),
                                    contentScale = if (result.isSuccess) {
                                        ContentScale.Crop
                                    } else {
                                        ContentScale.Fit
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Transparent),
                                )
                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(bottom = 8.dp, end = 8.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(SandYellow, Color.Transparent),
                                                radius = 70f
                                            )
                                        )
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite)
                                            Icons.Default.Favorite
                                        else
                                            Icons.Default.FavoriteBorder,
                                        tint = Color.Red,
                                        contentDescription = if (isFavorite) {
                                            stringResource(Res.string.remove_from_favorites)
                                        } else {
                                            stringResource(Res.string.mark_as_favorite)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.fillMaxHeight(0.05f)
            )
            content()
        }
    }
}
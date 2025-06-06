package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.pandoro.imageLoader
import com.tecknobit.pandoro.localUser
import org.jetbrains.compose.resources.painterResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.logo

/**
 * Used to display the profiles pictures, project icons, group logos or members profile pictures
 *
 * @param modifier The modifier to apply to the component
 * @param size The size of the thumbnail
 * @param shape The shape of the thumbnail
 * @param thumbnailData The data to display
 * @param contentDescription The description of the content displayed by the thumbnail
 * @param onClick The action to execute when the thumbnail has been clicked
 */
@Composable
fun Thumbnail(
    modifier: Modifier = Modifier,
    size: Dp = 35.dp,
    shape: Shape = CircleShape,
    thumbnailData: String?,
    contentDescription: String,
    onClick: (() -> Unit)? = null
) {
    AsyncImage(
        modifier = modifier
            .size(size)
            .clip(shape)
            .then(
                if(onClick != null) {
                    Modifier.clickable(
                        onClick = onClick
                    )
                } else
                    Modifier
            ),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(thumbnailData.prepare())
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo)
    )
}

/**
 * Method to format the data to display by the [Thumbnail] component
 *
 * @return the data formatted as nullable [String]
 */
private fun String?.prepare() : String? {
    return if (this != null && !this.startsWith(localUser.hostAddress))
        localUser.hostAddress + "/" + this
    else
        this
}
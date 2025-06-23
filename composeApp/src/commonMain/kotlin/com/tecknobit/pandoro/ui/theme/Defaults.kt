package com.tecknobit.pandoro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * `EmptyStateTitleStyle` the style to apply to the title of an [com.tecknobit.equinoxcompose.components.EmptyState]
 * component
 */
val EmptyStateTitleStyle
    @Composable get() = AppTypography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary
    )
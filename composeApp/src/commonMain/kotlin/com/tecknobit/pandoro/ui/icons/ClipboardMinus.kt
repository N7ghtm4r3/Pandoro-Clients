package com.tecknobit.pandoro.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ClipboardMinus: ImageVector
	get() {
		if (_ClipboardMinus != null) {
			return _ClipboardMinus!!
		}
		_ClipboardMinus = ImageVector.Builder(
            name = "ClipboardMinus",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(9f, 2f)
				horizontalLineTo(15f)
				arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 3f)
				verticalLineTo(5f)
				arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15f, 6f)
				horizontalLineTo(9f)
				arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 5f)
				verticalLineTo(3f)
				arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 2f)
				close()
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(16f, 4f)
				horizontalLineToRelative(2f)
				arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
				verticalLineToRelative(14f)
				arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 2f)
				horizontalLineTo(6f)
				arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, -2f)
				verticalLineTo(6f)
				arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, -2f)
				horizontalLineToRelative(2f)
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(9f, 14f)
				horizontalLineToRelative(6f)
			}
		}.build()
		return _ClipboardMinus!!
	}

private var _ClipboardMinus: ImageVector? = null

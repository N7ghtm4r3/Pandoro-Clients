package com.tecknobit.pandoro.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Copy: ImageVector
	get() {
		if (_Copy != null) {
			return _Copy!!
		}
		_Copy = ImageVector.Builder(
            name = "Copy",
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
				moveTo(10f, 8f)
				horizontalLineTo(20f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 10f)
				verticalLineTo(20f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 22f)
				horizontalLineTo(10f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 20f)
				verticalLineTo(10f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10f, 8f)
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
				moveTo(4f, 16f)
				curveToRelative(-1.1f, 0f, -2f, -0.9f, -2f, -2f)
				verticalLineTo(4f)
				curveToRelative(0f, -1.1f, 0.9f, -2f, 2f, -2f)
				horizontalLineToRelative(10f)
				curveToRelative(1.1f, 0f, 2f, 0.9f, 2f, 2f)
			}
		}.build()
		return _Copy!!
	}

private var _Copy: ImageVector? = null

package com.tecknobit.pandoro.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val FolderManaged: ImageVector
	get() {
		if (_FolderManaged != null) {
			return _FolderManaged!!
		}
		_FolderManaged = ImageVector.Builder(
            name = "FolderManaged",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(680f, 880f)
				lineToRelative(-12f, -60f)
				quadToRelative(-12f, -5f, -22.5f, -10.5f)
				reflectiveQuadTo(624f, 796f)
				lineToRelative(-58f, 18f)
				lineToRelative(-40f, -68f)
				lineToRelative(46f, -40f)
				quadToRelative(-2f, -12f, -2f, -26f)
				reflectiveQuadToRelative(2f, -26f)
				lineToRelative(-46f, -40f)
				lineToRelative(40f, -68f)
				lineToRelative(58f, 18f)
				quadToRelative(11f, -8f, 21.5f, -13.5f)
				reflectiveQuadTo(668f, 540f)
				lineToRelative(12f, -60f)
				horizontalLineToRelative(80f)
				lineToRelative(12f, 60f)
				quadToRelative(12f, 5f, 22.5f, 10.5f)
				reflectiveQuadTo(816f, 564f)
				lineToRelative(58f, -18f)
				lineToRelative(40f, 68f)
				lineToRelative(-46f, 40f)
				quadToRelative(2f, 12f, 2f, 26f)
				reflectiveQuadToRelative(-2f, 26f)
				lineToRelative(46f, 40f)
				lineToRelative(-40f, 68f)
				lineToRelative(-58f, -18f)
				quadToRelative(-11f, 8f, -21.5f, 13.5f)
				reflectiveQuadTo(772f, 820f)
				lineToRelative(-12f, 60f)
				close()
				moveToRelative(40f, -120f)
				quadToRelative(33f, 0f, 56.5f, -23.5f)
				reflectiveQuadTo(800f, 680f)
				reflectiveQuadToRelative(-23.5f, -56.5f)
				reflectiveQuadTo(720f, 600f)
				reflectiveQuadToRelative(-56.5f, 23.5f)
				reflectiveQuadTo(640f, 680f)
				reflectiveQuadToRelative(23.5f, 56.5f)
				reflectiveQuadTo(720f, 760f)
				moveToRelative(-560f, -40f)
				verticalLineToRelative(-480f)
				verticalLineToRelative(172f)
				verticalLineToRelative(-12f)
				close()
				moveToRelative(0f, 80f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(80f, 720f)
				verticalLineToRelative(-480f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(160f, 160f)
				horizontalLineToRelative(240f)
				lineToRelative(80f, 80f)
				horizontalLineToRelative(320f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(880f, 320f)
				verticalLineToRelative(131f)
				quadToRelative(-18f, -13f, -38f, -22.5f)
				reflectiveQuadTo(800f, 412f)
				verticalLineToRelative(-92f)
				horizontalLineTo(447f)
				lineToRelative(-80f, -80f)
				horizontalLineTo(160f)
				verticalLineToRelative(480f)
				horizontalLineToRelative(283f)
				quadToRelative(3f, 21f, 9.5f, 41f)
				reflectiveQuadToRelative(15.5f, 39f)
				close()
			}
		}.build()
		return _FolderManaged!!
	}

private var _FolderManaged: ImageVector? = null

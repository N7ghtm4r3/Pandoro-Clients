package com.tecknobit.pandoro.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ExportNotes: ImageVector
	get() {
		if (_Export_notes != null) {
			return _Export_notes!!
		}
		_Export_notes = ImageVector.Builder(
            name = "ExportNotes",
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
				moveTo(648f, 820f)
				lineToRelative(112f, -112f)
				verticalLineToRelative(92f)
				horizontalLineToRelative(40f)
				verticalLineToRelative(-160f)
				horizontalLineTo(640f)
				verticalLineToRelative(40f)
				horizontalLineToRelative(92f)
				lineTo(620f, 792f)
				close()
				moveToRelative(-448f, 20f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(120f, 760f)
				verticalLineToRelative(-560f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(200f, 120f)
				horizontalLineToRelative(560f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(840f, 200f)
				verticalLineToRelative(268f)
				quadToRelative(-19f, -9f, -39f, -15.5f)
				reflectiveQuadToRelative(-41f, -9.5f)
				verticalLineToRelative(-243f)
				horizontalLineTo(200f)
				verticalLineToRelative(560f)
				horizontalLineToRelative(242f)
				quadToRelative(3f, 22f, 9.5f, 42f)
				reflectiveQuadToRelative(15.5f, 38f)
				close()
				moveToRelative(0f, -120f)
				verticalLineToRelative(40f)
				verticalLineToRelative(-560f)
				verticalLineToRelative(243f)
				verticalLineToRelative(-3f)
				close()
				moveToRelative(80f, -40f)
				horizontalLineToRelative(163f)
				quadToRelative(3f, -21f, 9.5f, -41f)
				reflectiveQuadToRelative(14.5f, -39f)
				horizontalLineTo(280f)
				close()
				moveToRelative(0f, -160f)
				horizontalLineToRelative(244f)
				quadToRelative(32f, -30f, 71.5f, -50f)
				reflectiveQuadToRelative(84.5f, -27f)
				verticalLineToRelative(-3f)
				horizontalLineTo(280f)
				close()
				moveToRelative(0f, -160f)
				horizontalLineToRelative(400f)
				verticalLineToRelative(-80f)
				horizontalLineTo(280f)
				close()
				moveTo(720f, 920f)
				quadToRelative(-83f, 0f, -141.5f, -58.5f)
				reflectiveQuadTo(520f, 720f)
				reflectiveQuadToRelative(58.5f, -141.5f)
				reflectiveQuadTo(720f, 520f)
				reflectiveQuadToRelative(141.5f, 58.5f)
				reflectiveQuadTo(920f, 720f)
				reflectiveQuadTo(861.5f, 861.5f)
				reflectiveQuadTo(720f, 920f)
			}
		}.build()
		return _Export_notes!!
	}

private var _Export_notes: ImageVector? = null

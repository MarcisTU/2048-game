package org.eternal.game.kotlin2048

import android.animation.ValueAnimator
import android.graphics.Color
import android.view.MotionEvent
import android.view.animation.OvershootInterpolator

class RectUnit(private val unitManager: UnitManager) {
    var row = 1
    var col = 1
    var num = 2

    var x = 0F
    var y = 0F

    var size = 0F

    var color = Color.rgb(195, 168, 237)  // Default color for num 2

    fun setData(col: Int, row: Int, num: Int) {
        this.col = col
        this.row = row
        this.num = num
        x = GAP * col + RECT_UNIT_SIZE * (col - 0.5F)
        y = GAP * row + RECT_UNIT_SIZE * (row - 0.5F)
        enterAnimation()
    }

    // Choose appropriate color for current number
    fun setRectColor(num: Int) {
        color = when (num) {
            4 -> Color.rgb(152, 118, 204)
            8 -> Color.rgb(92, 47, 161)
            16 -> Color.rgb(53, 6, 125)
            32 -> Color.rgb(102, 20, 153)
            64 -> Color.rgb(13, 117, 191)
            128 -> Color.rgb(1, 44, 122)
            256 -> Color.rgb(148, 4, 81)
            512 -> Color.rgb(110, 2, 13)
            1024 -> Color.rgb(115, 181, 0)
            2048 -> Color.rgb(237, 146, 0)
            4096 -> Color.rgb(2, 6, 82)
            else -> Color.BLUE
        }
    }

    fun colMove(toCol: Int) {
        this.col = toCol;  // Set source col position
        // Do animation of moving rectangle
        val toX = GAP * toCol + RECT_UNIT_SIZE * (toCol - 0.5F)
        val animate = ValueAnimator.ofFloat(x, toX).apply {
            duration = 200
            interpolator = OvershootInterpolator()
            addUpdateListener {
                x = animatedValue as Float
                unitManager.refresh()
            }
        }
        animate.start()
    }

    fun rowMove(toRow: Int) {
        this.row = toRow;  // Set source row position
        // Do animation of moving rectangle
        val toY = GAP * toRow + RECT_UNIT_SIZE * (toRow - 0.5F)
        val animate = ValueAnimator.ofFloat(y, toY).apply {
            duration = 200
            interpolator = OvershootInterpolator()
            addUpdateListener {
                y = animatedValue as Float
                unitManager.refresh()
            }
        }
        animate.start()
    }

    private fun enterAnimation() {
        val animate = ValueAnimator.ofFloat(0F, RECT_UNIT_SIZE).apply {
            duration = 200
            interpolator = OvershootInterpolator()
            addUpdateListener {
                size = animatedValue as Float
                unitManager.refresh()
            }
        }
        animate.start()
    }
}

package org.eternal.game.kotlin2048

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.widget.TextView
import kotlin.math.abs

class UnitManager(view: GameView, c: Context, private val scoreView: TextView) : ViewComponent(view) {

    private val rectUnits = arrayListOf<RectUnit>()
    private var score: Int = 0
    private val ctx = c

    init {
        var x0 = 0F
        var y0 = 0F
        // Get mouse position on down action and then compute difference
        // by getting coordinates of released mouse position
        view.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    x0 = event.x
                    y0 = event.y
                }
                MotionEvent.ACTION_UP -> {
                    val dx = event.x - x0
                    val dy = event.y - y0
                    val direction = getDirection(dx, dy)
                    if (direction != Direction.ORIGIN) performMove(direction) // Tap on screen was performer instead of swipe
                }
            }
            true
        }
    }

    fun start() {
        addUnit()
    }

    fun resetGameState() {
        rectUnits.clear()
        score = 0
        scoreView.text = ctx.getString(R.string.score_text, score)

        start()
    }

    private enum class Direction {
        LEFT, UP, RIGHT, DOWN, ORIGIN
    }

    private fun checkSquare(x: Int, y: Int) : Boolean {
        val iter = rectUnits.iterator()
        iter.forEach {
            if (x == it.col && y == it.row) return true
        }
        return false
    }

    private fun getRect(x: Int, y: Int) : RectUnit? {
        val iter = rectUnits.iterator()
        iter.forEach {
            if (x == it.col && y == it.row) return it
        }
        // this line won't be executed anyway but to make compiler happy i wil add it
        // Since we are checking the square earlier for RectUnit and call this function only when true
        return null
    }

    private fun performMove(direction: Direction) {
        when(direction) {
            Direction.UP -> moveUp()/*u.rowMove(1)*/
            Direction.DOWN -> moveDown()/*u.rowMove(4)*/
            Direction.LEFT -> moveLeft()/*u.colMove(1)*/
            Direction.RIGHT -> moveRight()/*u.colMove(4)*/
            else -> Unit
        }
        // add another rect after each move
        addUnit()
    }

    private fun moveUp() {
        var startRow: Int
        var delta: Int
        var curRect: RectUnit?
        var nextRect: RectUnit?

        // Start checking from second row since first row can't move up any further
        for (y in 2..4) {
            for (x in 1..4) {
                if (checkSquare(x, y)) {
                    delta = 0
                    // obtain current rectangle
                    curRect = getRect(x, y)

                    // check rows above the current rect
                    startRow = curRect!!.row - 1
                    for (row in startRow downTo 1) {
                        if (checkSquare(x, row)) {  // Next square contains a RectUnit so we will check for moving them together
                            nextRect = getRect(x, row)
                            if (curRect.num == nextRect!!.num) {
                                delta += 1  // move current Rect into place of rectangle that has equal num
                                curRect.num *= 2  // double the score of single Rect
                                curRect.setRectColor(curRect.num)
                                rectUnits.remove(nextRect)

                                // set score
                                score += curRect.num
                                scoreView.text = ctx.getString(R.string.score_text, score)
                            }
                            break
                        }
                        delta += 1  // increase possible position current RectUnit can move to
                    }

                    // Move rect if it's possible to move
                    val destination = curRect.row - delta
                    if (destination < curRect.row) curRect.rowMove(destination)
                }
            }
        }
    }

    private fun moveDown() {
        var startRow: Int
        var delta: Int
        var curRect: RectUnit?
        var nextRect: RectUnit?

        for (y in 3 downTo 1) {
            for (x in 1..4) {
                if (checkSquare(x, y)) {
                    delta = 0
                    // obtain current rectangle
                    curRect = getRect(x, y)

                    // check rows below the current rect
                    startRow = curRect!!.row + 1
                    for (row in startRow..4) {
                        if (checkSquare(x, row)) {  // Next square contains a RectUnit so we will check for moving them together
                            nextRect = getRect(x, row)
                            if (curRect.num == nextRect!!.num) {
                                delta += 1  // move current Rect into place of rectangle that has equal num
                                curRect.num *= 2  // double the score of single Rect
                                curRect.setRectColor(curRect.num)
                                rectUnits.remove(nextRect)

                                // set score
                                score += curRect.num
                                scoreView.text = ctx.getString(R.string.score_text, score)
                            }
                            break
                        }
                        delta += 1  // increase possible position current RectUnit can move to
                    }

                    // Move rect if it's possible to move
                    val destination = curRect.row + delta
                    if (destination > curRect.row) curRect.rowMove(destination)
                }
            }
        }
    }

    private fun moveLeft() {
        var startCol: Int
        var delta: Int
        var curRect: RectUnit?
        var nextRect: RectUnit?

        for (x in 2..4) {
            for (y in 1..4) {
                if (checkSquare(x, y)) {
                    delta = 0
                    // obtain current rectangle
                    curRect = getRect(x, y)

                    // check col's to the left of current rect
                    startCol = curRect!!.col - 1
                    for (col in startCol downTo 1) {
                        if (checkSquare(col, y)) {  // Next square contains a RectUnit so we will check for moving them together
                            nextRect = getRect(col, y)
                            if (curRect.num == nextRect!!.num) {
                                delta += 1  // move current Rect into place of rectangle that has equal num
                                curRect.num *= 2  // double the score of single Rect
                                curRect.setRectColor(curRect.num)
                                rectUnits.remove(nextRect)

                                // set score
                                score += curRect.num
                                scoreView.text = ctx.getString(R.string.score_text, score)
                            }
                            break
                        }
                        delta += 1  // increase possible position current RectUnit can move to
                    }

                    // Move rect if it's possible to move
                    val destination = curRect.col - delta
                    if (destination < curRect.col) curRect.colMove(destination)
                }
            }
        }
    }

    private fun moveRight() {
        var startCol: Int
        var delta: Int
        var curRect: RectUnit?
        var nextRect: RectUnit?

        for (x in 3 downTo 1) {
            for (y in 1..4) {
                if (checkSquare(x, y)) {
                    delta = 0
                    // obtain current rectangle
                    curRect = getRect(x, y)

                    // check col's to the left of current rect
                    startCol = curRect!!.col + 1
                    for (col in startCol..4) {
                        if (checkSquare(col, y)) {  // Next square contains a RectUnit so we will check for moving them together
                            nextRect = getRect(col, y)
                            if (curRect.num == nextRect!!.num) {
                                delta += 1  // move current Rect into place of rectangle that has equal num
                                curRect.num *= 2  // double the score of single Rect
                                curRect.setRectColor(curRect.num)
                                rectUnits.remove(nextRect)

                                // set score
                                score += curRect.num
                                scoreView.text = ctx.getString(R.string.score_text, score)
                            }
                            break
                        }
                        delta += 1  // increase possible position current RectUnit can move to
                    }

                    // Move rect if it's possible to move
                    val destination = curRect.col + delta
                    if (destination > curRect.col) curRect.colMove(destination)
                }
            }
        }
    }

    private fun getDirection(dx: Float, dy: Float): Direction {
        // If mouse drag difference is too small then consider it being a click with small drag offset
        return if (abs(dx) < 50 && abs(dy) < 50) {
            Direction.ORIGIN
        } else {
            if (abs(dx) > abs(dy)) {
                if (dx > 0) Direction.RIGHT else Direction.LEFT
            } else {
                if (dy > 0) Direction.DOWN else Direction.UP
            }
        }
    }

    private fun getOneUnusedPosition(): Pair<Int, Int> {
        val array = arrayListOf<Pair<Int, Int>>()

        for (x in 1..4) {
            for (y in 1..4) {
                var exist = false
                rectUnits.takeWhile {
                    exist = (it.col == x && it.row == y)
                    !exist
                }
                if (!exist) array.add(Pair(x, y))
            }
        }

        // Board is full of rectangles (Game Over state!!!)
        if (array.size == 0) return Pair(-1, -1)

        val index = (0..(array.size - 1)).random()
        return array[index]
    }

    private fun addUnit() {
        val (col, row) = getOneUnusedPosition()
//        val num = (1..2).random() * 2
        val num = 2
        rectUnits.add(RectUnit(this@UnitManager).apply {
            setData(col, row, num)
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.rectUnits.forEach {
            paint.color = it.color
            canvas?.apply {
                // save canvas state before move it
                save()
                // move to the center of unit rect
                translate(it.x, it.y)
                // draw color block
                drawRoundRect(-it.size / 2, -it.size / 2, it.size / 2, it.size / 2, RECT_RADIUS, RECT_RADIUS, paint)
                // prepare the text
                paint.color = Color.WHITE
                paint.textSize = view.context.sp2px(20F)
                paint.textAlign = Paint.Align.CENTER
                val top = paint.fontMetrics.top
                val bottom = paint.fontMetrics.bottom
                // draw the text
                drawText(it.num.toString(), 0F, -(top + bottom) / 2, paint)
                // restore the canvas after draw
                restore()
            }
        }
    }
}

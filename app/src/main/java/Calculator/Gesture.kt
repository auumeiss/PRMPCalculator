package Calculator

import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView

class MyGestureListener(val result:TextView) : GestureDetector.SimpleOnGestureListener() {

    // Минимальная дистанция для свайпа
    private val swipeThreshold = 100
    // Минимальная скорость для свайпа
    private val swipeVelocityThreshold = 100

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val diffX = e2.x - e1!!.x // Разница по оси X
        val diffY = e2.y - e1.y // Разница по оси Y

        // Проверка, что это свайп справа налево
        if (Math.abs(diffX) > Math.abs(diffY) && // Преобладание движения по X
            Math.abs(diffX) > swipeThreshold && // Дистанция свайпа
            Math.abs(velocityX) > swipeVelocityThreshold // Скорость свайпа
        ) {
            if (diffX < 0) { // Свайп справа налево
                result.text=""
                return true
            }
        }
        return false
    }
}
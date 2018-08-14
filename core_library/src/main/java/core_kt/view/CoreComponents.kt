package core_kt.view

import android.content.Context
import android.view.View
import android.widget.SeekBar

fun createSeekBarRadius(context: Context): SeekBar {
    val seekBar = SeekBar(context)
    seekBar.max = 100
    seekBar.visibility = View.INVISIBLE
    return seekBar
}
package com.example.beatflowplayer

import org.junit.Test
import org.junit.Assert.*
import com.example.beatflowplayer.utils.convertFromDuration

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun convertFromDuration_returnsFormattedTime() {
        val result = convertFromDuration(125000)
        assertEquals("2:05", result)
    }
}
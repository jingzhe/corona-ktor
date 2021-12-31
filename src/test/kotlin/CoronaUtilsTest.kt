package com.jingzhe.corona

import com.jingzhe.corona.utils.CoronaUtils
import kotlin.test.*
import kotlin.test.assertEquals

class CoronaUtilsTest {

    @Test
    fun testNextDay() {
        val nextDay = CoronaUtils.getNextDate("2021-12-31")
        assertEquals(nextDay, "2022-01-01")
    }
}
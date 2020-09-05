package com.rshack.rstracker

import com.rshack.rstracker.model.data.Track
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SpekText : Spek({
    describe("Test1") {
        val track by memoized { Track("1", 100, 100f, 100) }

        it("should return 100") {
            assertEquals(expected = 100L, actual = track.time)
        }
    }
})

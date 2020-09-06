package com.rshack.rstracker

import androidx.lifecycle.MutableLiveData
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.model.repository.ITrackListRepository
import com.rshack.rstracker.viewmodel.ResultsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object ResultsViewModelTest : Spek({

    val repository: ITrackListRepository by memoized { mockk(relaxed = true) }
    val resultsViewModel: ResultsViewModel by memoized {
        ResultsViewModel(repository)
    }

    beforeEachTest {
        SpekInstantTaskExecutorRule.execute()

        runBlocking {
            coEvery { repository.load() } returns MutableLiveData(
                listOf(
                    mockk(),
                    mockk(),
                    mockk()
                )
            )
        }
    }

    afterEachTest { SpekInstantTaskExecutorRule.finish() }

    describe("tracks") {
        it("init with 3 items") {
            val tracks = resultsViewModel.tracks.value
            assertNotNull(tracks)
            assertEquals(3, tracks.size)
        }
    }
    describe("navigateToPhotoFragment") {
        it("init value = null") {
            assertNull(resultsViewModel.navigateToPhotoFragment.value)
        }
        it("navigate to photo") {
            val track = mockk<Track>(relaxed = true)
            resultsViewModel.displayPhotoFragment(track)
            assertEquals(track, resultsViewModel.navigateToPhotoFragment.value)
        }
        it("navigate to photo complete") {
            resultsViewModel.displayPhotoFragmentComplete()
            assertNull(resultsViewModel.navigateToPhotoFragment.value)
        }
    }

    describe("navigateToMapFragment") {
        it("init value = null") {
            assertNull(resultsViewModel.navigateToMapFragment.value)
        }
        it("navigate to map") {
            val track = mockk<Track>(relaxed = true)
            resultsViewModel.displayMapFragment(track)
            assertEquals(track, resultsViewModel.navigateToMapFragment.value)
        }
        it("navigate to map complete") {
            resultsViewModel.displayMapFragmentComplete()
            assertNull(resultsViewModel.navigateToMapFragment.value)
        }
    }
})

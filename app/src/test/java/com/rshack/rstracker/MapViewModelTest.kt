package com.rshack.rstracker

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.viewmodel.MapViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object MapViewModelTest : Spek({

    val repository: ITrackRepository by memoized { mockk(relaxed = true) }
    val firebaseAuthenticationRepository:
        IAuthenticationRepository by memoized { mockk(relaxed = true) }
    val mapViewModel: MapViewModel by memoized {
        MapViewModel(repository, firebaseAuthenticationRepository)
    }

    beforeEachTest {
        SpekInstantTaskExecutorRule.execute()
    }

    afterEachTest { SpekInstantTaskExecutorRule.finish() }

    describe("running status") {
        it("check isRunning status") {
            assertNull(mapViewModel.isRunning.value)
            mapViewModel.changeStatus()
            assertEquals(false, mapViewModel.isRunning.value)
            mapViewModel.changeStatus()
            assertEquals(true, mapViewModel.isRunning.value)
            mapViewModel.clearPoints()
            assertNull(mapViewModel.isRunning.value)
        }
    }

    describe("repository and points connection") {
        beforeEachTest {
            every { repository.getCoordinates() } returns MutableLiveData(
                listOf(
                    LatLng(55.692623, 37.735027),
                    LatLng(55.693407, 37.744329)
                )
            )
        }
        it("check points liveData") {
            val points = mapViewModel.points.value
            assertNotNull(points)
            assertEquals(2, points.size)
        }
    }

    describe("viewModel functions verify") {
        it("clearPoints") {
            mapViewModel.clearPoints()
            verify { repository.clearCoordinates() }
        }
        it("startNewTrack") {
            mapViewModel.startNewTrack(111)
            verify { repository.subscribeToUpdates(111) }
        }
        it("saveIntoFirebase") {
            mapViewModel.saveIntoFirebase(111, 111f, 111)
            verify {
                repository
                    .saveTimeAndDistanceToFirebase(111, 111f, 111)
            }
        }
        it("updateDistance") {
            mapViewModel.updateDistance()
            verify {
                repository.getPolylineLength()
            }
        }
        it("logout") {
            mapViewModel.logout()
            verify {
                firebaseAuthenticationRepository.logout()
            }
        }
        it("showTrack") {
            val track = mockk<Track>(relaxed = true)
            mapViewModel.showTrack(track)
            verify {
                repository.getCoordinates(track)
            }
        }
        it("getEmail") {
            every { firebaseAuthenticationRepository.getCurrentUserEmail() } returns "hi@hi@mail.ru"
            assertEquals("hi@hi@mail.ru", mapViewModel.getEmail())
        }
    }
})

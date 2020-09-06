package com.rshack.rstracker

import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.model.data.Urls
import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.viewmodel.MapViewModel
import com.rshack.rstracker.viewmodel.PhotosViewModel
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PhotoViewModelTest : Spek({

    val repository: ITrackRepository by memoized { mockk(relaxed = true) }
    val photoViewModel: PhotosViewModel by memoized {
        PhotosViewModel(repository)
    }

    beforeEachTest {
        SpekInstantTaskExecutorRule.execute()
    }

    afterEachTest { SpekInstantTaskExecutorRule.finish() }

    describe("click photo") {
        it("updateTrack fun should be called") {
            val urls = mockk<Urls>(relaxed = true)
            val track = mockk<Track>(relaxed = true)
            photoViewModel.onPhotoClicked(urls, track)
            verify { repository.updateTrack(track) }
        }
    }
})

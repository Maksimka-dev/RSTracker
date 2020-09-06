package com.rshack.rstracker.di

import com.rshack.rstracker.model.repository.FirebaseAuthenticationRepository
import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackListRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackListRepository
import com.rshack.rstracker.model.repository.TrackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class FirebaseAuthenticationModule {
    @Binds
    @Singleton
    abstract fun bindAuthentication(impl: FirebaseAuthenticationRepository):
        IAuthenticationRepository
}

@InstallIn(ApplicationComponent::class)
@Module
abstract class TrackModule {
    @Binds
    @Singleton
    abstract fun bindTrack(impl: TrackRepository): ITrackRepository
}

@InstallIn(ApplicationComponent::class)
@Module
abstract class TrackListModule {
    @Binds
    @Singleton
    abstract fun bindTrackList(impl: TrackListRepository): ITrackListRepository
}

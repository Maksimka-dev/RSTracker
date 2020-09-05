package com.rshack.rstracker.di

import com.rshack.rstracker.model.repository.FirebaseAuthenticationRepository
import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class FirebaseAuthenticationModule {
    @Binds
    abstract fun bindAuthentication(impl: FirebaseAuthenticationRepository):
        IAuthenticationRepository
}

@InstallIn(ActivityComponent::class)
@Module
abstract class TrackModule {
    @Binds
    abstract fun bindTrack(impl: TrackRepository): ITrackRepository
}

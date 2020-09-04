package com.rshack.rstracker.di

import android.app.Activity
import com.rshack.rstracker.databinding.ActivityMainBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
object BindingModule {
    @Provides
    @ActivityScoped
    fun provideBinding(@ActivityContext context: Activity): ActivityMainBinding {
        return ActivityMainBinding.inflate(context.layoutInflater)
    }
}

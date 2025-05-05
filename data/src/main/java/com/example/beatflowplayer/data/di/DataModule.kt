package com.example.beatflowplayer.data.di

import com.example.beatflowplayer.data.repository.AudioRepositoryImpl
import com.example.beatflowplayer.domain.repository.AudioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideAudioRepository(impl: AudioRepositoryImpl): AudioRepository = impl
}
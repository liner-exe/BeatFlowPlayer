package com.example.beatflowplayer.di

import com.example.beatflowplayer.data.local.AudioLocalDataSourceImpl
import com.example.beatflowplayer.data.repository.AudioRepositoryImpl
import com.example.beatflowplayer.domain.repository.AudioRepository
import com.example.beatflowplayer.domain.player.QueueManager
import com.example.beatflowplayer.data.player.QueueManagerImpl
import com.example.beatflowplayer.domain.AudioLocalDataSource
import com.example.beatflowplayer.player.PlayerManager
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideAudioRepository(impl: AudioRepositoryImpl): AudioRepository = impl

    @Provides
    fun provideAudioLocalDataSource(impl: AudioLocalDataSourceImpl): AudioLocalDataSource = impl

    @Provides
    @Singleton
    fun provideQueueManager(impl: QueueManagerImpl): QueueManager = impl
}
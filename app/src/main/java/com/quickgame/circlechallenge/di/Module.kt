package com.quickgame.circlechallenge.di

import com.quickgame.circlechallenge.data.datastore.DataStore
import com.quickgame.circlechallenge.data.repository.IRepository
import com.quickgame.circlechallenge.data.soundmanager.SoundManager
import com.quickgame.circlechallenge.domain.interactors.GetAvtIdUseCase
import com.quickgame.circlechallenge.domain.interactors.GetBestScoreUseCase
import com.quickgame.circlechallenge.domain.interactors.GetStatusPlaySoundUseCase
import com.quickgame.circlechallenge.domain.interactors.SaveBestScoreUseCase
import com.quickgame.circlechallenge.domain.interactors.SetAvtIdUseCase
import com.quickgame.circlechallenge.domain.interactors.SetStatusPlaySoundUseCaseMatchGame
import com.quickgame.circlechallenge.domain.repository.RepositoryImpl
import com.quickgame.circlechallenge.presentation.MainViewModel
import com.quickgame.circlechallenge.presentation.ui.circlechallenge.CircleChallengeViewModel
import com.quickgame.circlechallenge.presentation.ui.home.HomeViewModel
import com.quickgame.circlechallenge.presentation.ui.setting.SettingViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel() }
    factoryOf(::HomeViewModel)
    factoryOf(::SettingViewModel)
    factoryOf(::CircleChallengeViewModel)
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}

val dataSourceModule = module {
    single { DataStore(get()) }
}

val useCaseModule = module {
    factoryOf(::GetBestScoreUseCase)
    factoryOf(::SaveBestScoreUseCase)
    factoryOf(::GetAvtIdUseCase)
    factoryOf(::SetAvtIdUseCase)
    factoryOf(::SetStatusPlaySoundUseCaseMatchGame)
    factoryOf(::GetStatusPlaySoundUseCase)
}

val repositoryModule = module {
    single<IRepository> { RepositoryImpl(get()) }
}

val mediaPlayerModule = module {
    singleOf(::SoundManager)
}
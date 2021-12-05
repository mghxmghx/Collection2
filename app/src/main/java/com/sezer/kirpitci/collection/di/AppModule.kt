package com.sezer.kirpitci.collection.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {
    @Provides
    @Singleton
    fun providesApplication(): Application {
        return app
    }
}

package com.eighthours.flow.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.eighthours.flow.FlowApplication
import com.eighthours.flow.data.initializer.DatabaseInitializer
import com.eighthours.flow.data.room.DatabaseInitializerHook
import com.eighthours.flow.data.room.FlowDatabase
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.presenter.utility.Formatter
import com.eighthours.flow.utility.SimpleFormatStrategy
import com.jakewharton.threetenabp.AndroidThreeTen
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
        private val context: Context) {

    init {
        Logger.addLogAdapter(AndroidLogAdapter(SimpleFormatStrategy(FlowApplication.TAG)))
        AndroidThreeTen.init(context);
        Formatter.init(context)
    }

    @Provides @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides @Singleton
    fun provideRepository(): Repository {
        val initializer = DatabaseInitializer()

        // Use Room.inMemoryDatabaseBuilder(context, FlowDatabase::class.java) for test
        val db = Room.databaseBuilder(context, FlowDatabase::class.java, FlowDatabase.NAME)
                .openHelperFactory(DatabaseInitializerHook(initializer))
                .build()
        initializer.initializeIfNeeded(db)
        return db
    }
}

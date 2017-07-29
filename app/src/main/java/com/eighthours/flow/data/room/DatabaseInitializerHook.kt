package com.eighthours.flow.data.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import com.eighthours.flow.data.initializer.DatabaseInitializer

class DatabaseInitializerHook(
        private val initializer: DatabaseInitializer)
    : FrameworkSQLiteOpenHelperFactory() {

    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        val wrapped = SupportSQLiteOpenHelper.Configuration
                .builder(configuration.context)
                .name(configuration.name)
                .version(configuration.version)
                .errorHandler(configuration.errorHandler)
                .callback(CallbackWrapper(configuration.callback, initializer))
                .build()
        return super.create(wrapped)
    }
}


private class CallbackWrapper(
        private val delegate: SupportSQLiteOpenHelper.Callback,
        private val initializer: DatabaseInitializer)
    : SupportSQLiteOpenHelper.Callback() {

    override fun onConfigure(db: SupportSQLiteDatabase) {
        delegate.onConfigure(db)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        delegate.onOpen(db)
    }

    override fun onDowngrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        delegate.onDowngrade(db, oldVersion, newVersion)
    }

    override fun onCreate(db: SupportSQLiteDatabase) {
        delegate.onCreate(db)
        initializer.needInitialization = true
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        delegate.onUpgrade(db, oldVersion, newVersion)
    }
}

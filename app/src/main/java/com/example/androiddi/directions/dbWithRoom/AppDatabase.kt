package com.example.androiddi.directions.dbWithRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androiddi.directions.DirectionOperator

@Database(entities = [ DirectionOperator::class ], version=6, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    public abstract fun groupOperatorDao(): DirectionOperatorDao
}
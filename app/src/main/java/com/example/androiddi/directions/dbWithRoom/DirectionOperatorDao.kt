package com.example.androiddi.directions.dbWithRoom

import androidx.room.*
import com.example.androiddi.directions.DirectionOperator

@Dao
interface DirectionOperatorDao
{
    @Query("SELECT * FROM DirectionOperator")
    fun getAll(): List<DirectionOperator?>?

    @Query("SELECT * FROM DirectionOperator WHERE id = :id")
    fun getById(id: Int): DirectionOperator

    @Insert
    fun insert(go: DirectionOperator?)

    @Delete
    fun delete(go: DirectionOperator?)
}
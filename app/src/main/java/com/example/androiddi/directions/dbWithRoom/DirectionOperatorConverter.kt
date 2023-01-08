package com.example.androiddi.directions.dbWithRoom

import androidx.room.TypeConverter
import com.example.androiddi.directions.Direction
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DirectionOperatorConverter
{
    @TypeConverter
    fun fromGO(directions: ArrayList<Direction>): String
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return gson.toJson(directions)
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<Direction>
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return try {
            val type: Type = object : TypeToken<ArrayList<Direction>>() {}.type
            gson.fromJson(data, type)
        } catch (e: Exception) {
            ArrayList()
        }
    }
}
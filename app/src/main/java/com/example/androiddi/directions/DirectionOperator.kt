package com.example.androiddi.directions

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androiddi.directions.dbWithRoom.DirectionOperatorConverter
import java.util.*
import kotlin.collections.ArrayList

@Entity
class DirectionOperator
{
    @PrimaryKey
    private var id: Int = 1

    @TypeConverters(DirectionOperatorConverter::class)
    private var directions: ArrayList<Direction> = ArrayList()

    fun getDirections(): ArrayList<Direction>
    {
        return directions
    }

    fun setDirections(newDirections: ArrayList<Direction>)
    {
        directions = newDirections
    }

    fun setId(id: Int)
    {
        this.id = id
    }

    fun getId(): Int
    {
        return id
    }

    fun getCountries(indexGroup: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in directions[indexGroup].listOfTours)
        {
            arrayListForReturn.add(i.country)
        }
        return arrayListForReturn
    }

    fun getRates(indexGroup: Int): ArrayList<Int>
    {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        for (i in directions[indexGroup].listOfTours)
        {
            arrayListForReturn.add(i.rate)
        }
        return arrayListForReturn
    }

    fun getDurations(directionIndex: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in directions[directionIndex].listOfTours)
        {
            arrayListForReturn.add(i.duration)
        }
        return arrayListForReturn
    }

    fun getTour(directionIndex: Int, tourIndex: Int): Tour
    {
        return directions[directionIndex].listOfTours[tourIndex]
    }

    fun sortTours(directionIndex: Int, sortIndex: Int)
    {
        if (sortIndex == 0)
        {
            val tempArrayListOfTasksNames: ArrayList<String> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                tempArrayListOfTasksNames.add(i.country.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksNames.sort()
            for (i in tempArrayListOfTasksNames)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    if (i == j.country.lowercase(Locale.ROOT)
                        && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 1)
        {
            val tempArrayListOfTasksConditions: ArrayList<String> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                tempArrayListOfTasksConditions.add(i.duration.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksConditions.sort()
            for (i in tempArrayListOfTasksConditions)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    if (i == j.duration.lowercase(Locale.ROOT)
                        && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 2)
        {
            val tempArrayListOfTasksNumbers: ArrayList<Int> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                tempArrayListOfTasksNumbers.add(i.rate)
            }
            tempArrayListOfTasksNumbers.sort()
            for (i in tempArrayListOfTasksNumbers)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    if (i == j.rate && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 3)
        {
            val tempArrayListOfTasksNumOfParticipants: ArrayList<GregorianCalendar> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                val d: List<String> = i.startDate.split(".")
                tempArrayListOfTasksNumOfParticipants.add(GregorianCalendar(d[2].toInt(),
                    d[1].toInt(), d[0].toInt()))
            }
            tempArrayListOfTasksNumOfParticipants.sort()
            for (i in tempArrayListOfTasksNumOfParticipants)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    val d: List<String> = j.startDate.split(".")
                    val tempGregorianCalendar = GregorianCalendar(d[2].toInt(), d[1].toInt(),
                        d[0].toInt())
                    if (i == tempGregorianCalendar && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 4)
        {
            val tempArrayListOfTasksNumOfParticipants: ArrayList<GregorianCalendar> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                val d: List<String> = i.endDate.split(".")
                tempArrayListOfTasksNumOfParticipants.add(GregorianCalendar(d[2].toInt(),
                    d[1].toInt(), d[0].toInt()))
            }
            tempArrayListOfTasksNumOfParticipants.sort()
            for (i in tempArrayListOfTasksNumOfParticipants)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    val d: List<String> = j.endDate.split(".")
                    val tempGregorianCalendar = GregorianCalendar(d[2].toInt(), d[1].toInt(),
                        d[0].toInt())
                    if (i == tempGregorianCalendar && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 5)
        {
            val tempArrayListOfTasksMaxScore: ArrayList<Int> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                tempArrayListOfTasksMaxScore.add(i.cost)
            }
            tempArrayListOfTasksMaxScore.sort()
            for (i in tempArrayListOfTasksMaxScore)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    if (i == j.cost && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 6)
        {
            val tempArrayListOfTasksIsComplicated: ArrayList<Int> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                tempArrayListOfTasksIsComplicated.add(i.isAvailable)
            }
            tempArrayListOfTasksIsComplicated.sort()
            for (i in tempArrayListOfTasksIsComplicated)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    if (i == j.isAvailable && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }

        if (sortIndex == 7)
        {
            val tempArrayListOfTasksHints: ArrayList<String> = ArrayList()
            val tempArrayListOfTours: ArrayList<Tour> = ArrayList()
            for (i in directions[directionIndex].listOfTours)
            {
                tempArrayListOfTasksHints.add(i.comment.lowercase(Locale.ROOT))
            }
            tempArrayListOfTasksHints.sort()
            for (i in tempArrayListOfTasksHints)
            {
                for (j in directions[directionIndex].listOfTours)
                {
                    if (i == j.comment.lowercase(Locale.ROOT)
                        && !tempArrayListOfTours.contains(j))
                    {
                        tempArrayListOfTours.add(j)
                        break
                    }
                }
            }
            directions[directionIndex].listOfTours = tempArrayListOfTours
        }
    }
}
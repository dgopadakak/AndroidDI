package com.example.androiddi.forRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddi.R

class CustomRecyclerAdapterForExams(private val countries: List<String>,
                                    private val rates: List<Int>,
                                    private val durations: List<String>):
    RecyclerView.Adapter<CustomRecyclerAdapterForExams.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        val textViewNum: TextView = itemView.findViewById(R.id.textViewNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.textViewName.text = countries[position]
        holder.textViewAddress.text = rates[position].toString()
        holder.textViewNum.text = durations[position]
    }

    override fun getItemCount() = countries.size
}
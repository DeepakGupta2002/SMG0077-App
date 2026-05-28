package com.smg0077.all_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R


class BidPointsRecycleViewAdapter() :
    RecyclerView.Adapter<BidPointsRecycleViewAdapter.MyViewHolder>() {

    lateinit var context: Context
    lateinit var OpenDigit: MutableList<String>
    lateinit var Points: MutableList<String>
    lateinit var sumbitbut: Button
    lateinit var wallpoint: TextView
    lateinit var radioGroup: RadioGroup
    var radiostate: Boolean = false

    constructor(
        context: Context,
        OpenDigit: MutableList<String>,
        Points: MutableList<String>,
        sumbitbut: Button,
        wallpoint: TextView,
        radioGroup: RadioGroup,
        radiostate: Boolean
    ) : this() {
        this.context = context
        this.OpenDigit = OpenDigit
        this.Points = Points
        this.sumbitbut = sumbitbut
        this.wallpoint = wallpoint
        this.radioGroup = radioGroup
        this.radiostate = radiostate

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var opendigit: TextView
        var points: TextView
        var closeitem: TextView
        init {
            opendigit = itemView.findViewById(R.id.sing_bid_opendigit)
            points = itemView.findViewById(R.id.sing_bid_points)
            closeitem = itemView.findViewById(R.id.sing_bid_close)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_bid_points_table,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = OpenDigit[position]
        val currentPoints = Points[position]
        var castwallet: Int
        holder.opendigit.text = currentItem
        holder.points.text = currentPoints
        holder.closeitem.setOnClickListener {
            OpenDigit.removeAt(position)
            Points.removeAt(position)
            val temppoint = wallpoint.text.toString().replace("\u20B9 ", "")
            castwallet = temppoint.toInt()
            castwallet = castwallet + (currentPoints.toInt())
            wallpoint.text = "\u20B9 " + castwallet.toString()

            if (itemCount == 0) {
                sumbitbut.visibility = View.GONE
                if (radiostate) {
                    (radioGroup.getChildAt(0) as RadioButton).isEnabled = true
                    (radioGroup.getChildAt(1) as RadioButton).isEnabled = true
                } else {
                    (radioGroup.getChildAt(1) as RadioButton).isEnabled = true

                }
            }
            notifyDataSetChanged()
        }
        val countval = itemCount
        if (countval < 1) {
            sumbitbut.visibility = View.GONE
        } else {
            sumbitbut.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return OpenDigit.size
    }

    fun clear() {
        val size: Int = OpenDigit.size
        OpenDigit.clear()
        notifyItemRangeRemoved(0, size)
        notifyDataSetChanged()
    }

}


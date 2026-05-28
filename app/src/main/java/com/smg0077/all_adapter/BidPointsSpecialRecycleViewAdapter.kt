package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R


class BidPointsSpecialRecycleViewAdapter() :
    RecyclerView.Adapter<BidPointsSpecialRecycleViewAdapter.MyViewHolder>() {

    lateinit var context: Context
    lateinit var OpenDigit: MutableList<String>
    lateinit var CloseDigit: MutableList<String>
    lateinit var Points: MutableList<String>
    lateinit var sumbitbut: Button
    lateinit var wallpoint: TextView
    lateinit var radioGroup: RadioGroup

    constructor(
        context: Context,
        OpenDigit: MutableList<String>,
        CloseDigit: MutableList<String>,
        Points: MutableList<String>,
        sumbitbut: Button,
        wallpoint: TextView,
        radioGroup: RadioGroup
    ) : this() {
        this.context = context
        this.OpenDigit = OpenDigit
        this.CloseDigit = CloseDigit
        this.Points = Points
        this.sumbitbut = sumbitbut
        this.wallpoint = wallpoint
        this.radioGroup = radioGroup
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var opendigit: TextView
        var points: TextView
        var closeitem: TextView
        var closePoints: TextView
        var closelayout: RelativeLayout
        var openTV: TextView
        var closeTV: TextView

        init {
            opendigit = itemView.findViewById(R.id.sing_bid_opendigit)
            points = itemView.findViewById(R.id.sing_bid_points)
            closeitem = itemView.findViewById(R.id.sing_bid_close)
            closePoints = itemView.findViewById(R.id.sing_closedigit)
            closelayout = itemView.findViewById(R.id.closedigilayout)
            openTV = itemView.findViewById(R.id.open)
            closeTV = itemView.findViewById(R.id.closedi)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_bid_special_points_table,
                parent,
                false
            )
        )
    }

    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = OpenDigit[position]
        val currentPoints = Points[position]
        var castwallet: Int
        val currentClosepoint = CloseDigit[position]
        radioGroup.checkedRadioButtonId

        val radioOption = (radioGroup.getChildAt(0) as RadioButton).isChecked
        if (radioOption) {
            holder.closePoints.text = currentClosepoint
            holder.closelayout.visibility = View.VISIBLE
            holder.opendigit.text = currentItem
            holder.points.text = currentPoints
        } else {
            holder.openTV.text = "Open Pana"
            holder.closeTV.text = "Close Digit"
            holder.closePoints.text = currentItem
            holder.closelayout.visibility = View.VISIBLE
            holder.opendigit.text = currentClosepoint
            holder.points.text = currentPoints

        }
        holder.closeitem.setOnClickListener {
            OpenDigit.removeAt(position)
            Points.removeAt(position)
            CloseDigit.removeAt(position)
            val temppoint = wallpoint.text.toString().replace("\u20B9 ", "")
            castwallet = temppoint.toInt()
            castwallet = castwallet + (currentPoints.toInt())
            wallpoint.text = "\u20B9 " + castwallet.toString()

            if (itemCount == 0) {
                sumbitbut.visibility = View.GONE
                (radioGroup.getChildAt(0) as RadioButton).isEnabled = true
                (radioGroup.getChildAt(1) as RadioButton).isEnabled = true

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


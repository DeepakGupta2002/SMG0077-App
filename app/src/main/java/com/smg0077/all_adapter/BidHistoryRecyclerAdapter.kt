package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R
import com.smg0077.model.bidHistoryHolder


class BidHistoryRecyclerAdapter(
    private var exampleList: List<bidHistoryHolder>,
    private var showHidesession: Boolean
) :
    RecyclerView.Adapter<BidHistoryRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var game_name: TextView
        var bid_date: TextView
        var points: TextView
        var session_type: TextView
        var digitvalue: TextView

        init {
            game_name = itemView.findViewById(R.id.sing_win_title)
            bid_date = itemView.findViewById(R.id.sing_datetime)
            points = itemView.findViewById(R.id.sing_points)
            session_type = itemView.findViewById(R.id.sing_sessionstatus)
            digitvalue = itemView.findViewById(R.id.sing_digitvalue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_bid_history,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.game_name.text = "${currentItem.game_name} (${currentItem.pana})"
        holder.bid_date.text = currentItem.bid_date

        if (currentItem.pana == "Half Sangam") {
            if (currentItem.session_type == "Open") {
                holder.digitvalue.text =
                    "Open Digit: ${currentItem.openndigit}  Close Pana: ${currentItem.closedigit}"
            } else {
                holder.digitvalue.text =
                    "Open Pana: ${currentItem.closedigit}  Close Digit: ${currentItem.openndigit}"
            }
        } else if (currentItem.closedigit.equals("")) {
            if (showHidesession) {
                holder.digitvalue.text = "Open: ${currentItem.openndigit}"
            } else {
                holder.digitvalue.text = "Digit: ${currentItem.openndigit}"
            }
        } else {
            holder.digitvalue.text =
                "Open: ${currentItem.openndigit}  Close: ${currentItem.closedigit}"
        }
        holder.points.text = "\u20B9 " + currentItem.points
        if (showHidesession) {
            if (currentItem.session_type.equals("")) {
                holder.session_type.text = ""
            } else {
                holder.session_type.text = "Session: " + currentItem.session_type
            }

        } else {
            holder.session_type.text = ""
        }
    }

    override fun getItemCount(): Int {
        return exampleList.size
    }
}
package com.smg0077.all_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R
import com.smg0077.model.WinHistoryHolder


class WinHistoryRecyclerAdapter(
    private var exampleList: List<WinHistoryHolder>,
    private var showhidesession: Boolean

) : RecyclerView.Adapter<WinHistoryRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var winamount: TextView
        var transaction_type: TextView
        var transaction_note: TextView
        var amount_status: TextView
        var wining_date: TextView
        var tx_request_number: TextView

        init {
            winamount = itemView.findViewById(R.id.sing_win_points)
            transaction_type = itemView.findViewById(R.id.sing_win_sessionstatus)
            transaction_note = itemView.findViewById(R.id.sing_win_title)
            amount_status = itemView.findViewById(R.id.sing_win_title)
            wining_date = itemView.findViewById(R.id.sing_win_datetime)
            tx_request_number = itemView.findViewById(R.id.sing_win_bidid)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_win_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.transaction_note.text = currentItem.gamename + " (${currentItem.pana_type})"
        holder.winamount.text = "\u20B9 " + currentItem.winamount
        holder.tx_request_number.text = "Id: " + currentItem.tx_request_number
        holder.wining_date.text = currentItem.wining_date
        if (showhidesession) {
            holder.transaction_type.text = "Session: " + currentItem.sessiontype
        } else {
            holder.transaction_type.text = ""
        }
    }

    override fun getItemCount(): Int {
        return exampleList.size
    }
}


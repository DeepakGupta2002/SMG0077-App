package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R

import com.smg0077.model.AddpointHistoryDataholder

class AddpointHistoryAdapter(
    private var context: Context,
    private var exampleList: List<AddpointHistoryDataholder>

) : RecyclerView.Adapter<AddpointHistoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var refrenseid: TextView = itemView.findViewById(R.id.single_addpoint_refreno)
        var remark: TextView = itemView.findViewById(R.id.single_addpoint_pointnote)
        var dispalydate: TextView = itemView.findViewById(R.id.single_addpoint_date)
        var paymetstatus: TextView = itemView.findViewById(R.id.single_addpoint_statustxt)
        var amountdisplay: TextView = itemView.findViewById(R.id.single_addpoint_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_upi_payment_history,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currentItem = exampleList[position]
        holder.refrenseid.text = currentItem.txn_id
        holder.amountdisplay.text = "\u20B9 " + currentItem.amount
        holder.dispalydate.text = currentItem.insert_date
        if (currentItem.point_status == "1") {
            holder.remark.visibility = View.GONE
            holder.paymetstatus.setTextColor(context.getColor(R.color.green))
            if (currentItem.deposit_type == "1") {
                holder.paymetstatus.text = "Auto-Approved"
            } else {
                holder.paymetstatus.text = "Approved"
            }

        } else if (currentItem.point_status == "0") {
            holder.remark.visibility = View.GONE

            if (currentItem.deposit_type == "1") {
                holder.paymetstatus.text = "Auto-Approved"
            } else {
                holder.paymetstatus.setTextColor(Color.BLUE)
                holder.paymetstatus.text = "Pending"
            }

        } else if (currentItem.point_status == "2") {
            holder.paymetstatus.setTextColor(Color.RED)
            holder.paymetstatus.text = "Rejected"
            holder.remark.text = currentItem.reject_remark
            holder.remark.visibility = View.VISIBLE

        }

    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

}


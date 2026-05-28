package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R
import com.smg0077.model.Wallethistorydataholder


class WalletHistoryRecyclerAdapter(
    private var context: Context,
    private var exampleList: List<Wallethistorydataholder>

) : RecyclerView.Adapter<WalletHistoryRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var paymentto: TextView
        var trans_note: TextView
        var amount: TextView
        var insert_date: TextView
        var senderimage: ImageView

        init {
            paymentto = itemView.findViewById(R.id.sendto)
            trans_note = itemView.findViewById(R.id.amtdiscription)
            amount = itemView.findViewById(R.id.payemt_amount)
            insert_date = itemView.findViewById(R.id.datetimetxt)
            senderimage = itemView.findViewById(R.id.senderimage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.payment_design,
                parent,
                false
            )
        )
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.trans_note.text = currentItem.transaction_note
        holder.insert_date.text = currentItem.insert_date

        if (currentItem.transaction_type.equals("1")) {
            holder.amount.text = "+" + currentItem.transfer_amount
            holder.senderimage.backgroundTintList = context.getColorStateList(R.color.green)
        } else {
            holder.amount.text = "-" + currentItem.transfer_amount
            holder.senderimage.backgroundTintList = context.getColorStateList(R.color.primary)
        }
        if (currentItem.amount_status.equals("1")) {
            holder.amount.setTextColor(context.getColor(R.color.green))
        } else if (currentItem.amount_status.equals("2")) {
            holder.amount.setTextColor(Color.RED)
        } else {
            holder.amount.setTextColor(context.getColor(R.color.sky_blue))
        }

    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

}


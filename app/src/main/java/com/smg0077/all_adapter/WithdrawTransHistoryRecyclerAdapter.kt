package com.smg0077.all_adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.R

import com.smg0077.model.WithdrawTransHistoryDataHolder
import com.smg0077.web.ImageViewActivity


class WithdrawTransHistoryRecyclerAdapter(
    private var context: Context,
    private var exampleList: List<WithdrawTransHistoryDataHolder>
) : RecyclerView.Adapter<WithdrawTransHistoryRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request_amount: TextView
        var requesr_number: TextView = itemView.findViewById(R.id.withdraw_request_no)
        var request_status: TextView
        var payment_mode: TextView
        var insert_date: TextView
        var fulllayoutclick: LinearLayout
        var paymentremark: TextView

        init {
            request_amount = itemView.findViewById(R.id.withdraw_request_amt)
            request_status = itemView.findViewById(R.id.withdraw_payment_status)
            insert_date = itemView.findViewById(R.id.withdraw_payment_date)
            payment_mode = itemView.findViewById(R.id.withdraw_payment_method)
            fulllayoutclick = itemView.findViewById(R.id.fulllayoutclick)
            paymentremark = itemView.findViewById(R.id.withdraw_payment_remark)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_withdraw_history,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = exampleList[position]

        if (currentItem.request_status.equals("0")) {
            holder.requesr_number.text = currentItem.request_no
            holder.requesr_number.setTextColor(Color.BLUE)
            holder.request_status.setTextColor(Color.BLUE)
            holder.request_status.text = "Pending"
            holder.request_amount.text = "\u20B9 " + currentItem.request_amount
            holder.request_amount.setTextColor(Color.BLUE)
        } else if (currentItem.request_status.equals("1")) {
            holder.requesr_number.text = currentItem.request_no
            holder.requesr_number.setTextColor(Color.RED)
            holder.request_status.setTextColor(Color.RED)
            holder.request_amount.setTextColor(Color.RED)
            holder.request_status.text = "Failed"
            holder.request_amount.text = "\u20B9 " + currentItem.request_amount
        } else {
            holder.requesr_number.text = currentItem.request_no
            holder.requesr_number.setTextColor(context.getColor(R.color.sky_blue))
            holder.request_status.setTextColor(context.getColor(R.color.sky_blue))
            holder.request_status.text = "Accepted"
            holder.request_amount.text = "\u20B9 " + currentItem.request_amount
            holder.request_amount.setTextColor(context.getColor(R.color.sky_blue))

        }
        val pmode = currentItem.payment_mode
        if (pmode.equals("2")) {
            holder.payment_mode.text = "Payment Mode: Paytm"
        } else if (pmode.equals("3")) {
            holder.payment_mode.text = "Payment Mode: Google Pay"
        } else {
            holder.payment_mode.text = "Payment Mode: PhonePe"
        }
        if (currentItem.remark.equals("")) {
            holder.paymentremark.visibility = View.GONE
        } else {
            holder.paymentremark.text = "Remark: " + currentItem.remark
        }
        holder.insert_date.text = currentItem.insert_date

        holder.fulllayoutclick.setOnClickListener {
            if (currentItem.request_status.equals("2")) {
                if (currentItem.paymentReceipt_url.contains("jpg") || currentItem.paymentReceipt_url.contains(
                        "jpeg"
                    ) || currentItem.paymentReceipt_url.contains("png") || currentItem.paymentReceipt_url.contains(
                        "webp"
                    )
                ) {
                    val intent = Intent(context, ImageViewActivity::class.java)
                    intent.putExtra("web_url", currentItem.paymentReceipt_url)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "No Receipt Found", Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

}


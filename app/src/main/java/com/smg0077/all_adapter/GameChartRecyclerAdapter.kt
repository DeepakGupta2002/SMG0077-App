package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.web.MyTaskViewWebView
import com.smg0077.R
import com.smg0077.model.GraphUrl

class GameChartRecyclerAdapter(
    private var context: Context,
    private var exampleList: List<GraphUrl>
) :
    RecyclerView.Adapter<GameChartRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var game_name: TextView = itemView.findViewById(R.id.single_gamenameTxt)
        var parentLayout: RelativeLayout = itemView.findViewById(R.id.parentLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_item_chart,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.game_name.text = currentItem.gameName
        holder.parentLayout.setOnClickListener {
            val intent = Intent(context, MyTaskViewWebView::class.java)
            intent.putExtra("status", true)
            intent.putExtra("web_url", currentItem.webUrl)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

}


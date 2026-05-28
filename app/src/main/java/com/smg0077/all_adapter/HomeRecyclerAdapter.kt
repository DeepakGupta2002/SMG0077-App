package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.model.gamedataholder
import com.smg0077.TaskListWindow
import com.smg0077.web.MyTaskViewWebView
import com.smg0077.R
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView


class HomeRecyclerAdapter(

    private var context: Context,
    private var exampleList: List<gamedataholder>

) : RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var discriptiontxt: ShimmerTextView
        val shimmer = Shimmer()
        var opendate: TextView
        var closedate: TextView
        var statusText: ShimmerTextView
        var Gamename: ShimmerTextView
        var gameChartTV: TextView
        var gamePlayTV: TextView
        var fullwindowclik: RelativeLayout

        init {
            discriptiontxt = itemView.findViewById(R.id.recyc_scorecardtxtn)
            Gamename = itemView.findViewById(R.id.recyc_gamenametxt)
            opendate = itemView.findViewById(R.id.recyc_opentimetxt)
            closedate = itemView.findViewById(R.id.recyc_close_time_txt)
            statusText = itemView.findViewById(R.id.market_status_text)
            fullwindowclik = itemView.findViewById(R.id.homerecycle_mainlayout)
            gameChartTV = itemView.findViewById(R.id.gameChartTV)
            gamePlayTV = itemView.findViewById(R.id.gamePlayTV)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_home_single_item,
                parent,
                false
            )
        )
    }


    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val shimmer = Shimmer()
        shimmer.start(holder.statusText)

        holder.setIsRecyclable(false)
        val currentItem = exampleList[position]
        holder.discriptiontxt.text = currentItem.scorecard
        holder.Gamename.text = currentItem.gamename
        holder.opendate.text = "Open: ${currentItem.opentime}"
        holder.closedate.text = "Close: ${currentItem.closetime}"

        if (currentItem.marketstate.equals("1")) {
//            holder.statusText.text = "Market Open"
            holder.statusText.text = currentItem.marketstatus
            holder.statusText.setTextColor(context.getColor(R.color.sky_blue))
            holder.gamePlayTV.setBackgroundResource(R.drawable.ic_play_one)
            holder.shimmer.start(holder.discriptiontxt)

        } else {
            holder.gamePlayTV.setTextColor(Color.WHITE)
//            holder.statusText.text = "Market Closed"
            holder.statusText.text = currentItem.marketstatus
            holder.statusText.setTextColor(context.getColor(R.color.red))
            holder.gamePlayTV.setBackgroundResource(R.drawable.ic_close)
        }


        holder.fullwindowclik.setOnClickListener {
//            if (currentItem.betting_status == "0") {
//                if (currentItem.marketstate.equals("1")) {
//                    val intent = Intent(context, WebViewMainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
//                }else{
//                    Toast.makeText(context, "Try next day", Toast.LENGTH_SHORT).show()
//                }
//            } else {
            if (currentItem.marketstate.equals("1")) {
                val intent = Intent(context, TaskListWindow::class.java)
                intent.putExtra("game_id", currentItem.game_id)
                intent.putExtra("game_name", currentItem.gamename)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Market Closed", Toast.LENGTH_SHORT).show()
                val vibrateservice =
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrateservice.vibrate(500)
            }
//            }
        }

        holder.gameChartTV.setOnClickListener {
            val intent = Intent(context, MyTaskViewWebView::class.java)
            intent.putExtra("web_url", currentItem.weburl)
            intent.putExtra("status", true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            holder.gameChartTV.setBackgroundResource(R.drawable.ic_chart_two)
        }


    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

}


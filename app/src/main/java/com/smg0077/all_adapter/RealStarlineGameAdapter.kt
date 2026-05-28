package com.smg0077.all_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.model.RealStarlineGamedataholder
import com.smg0077.R
import com.smg0077.realtask.RealStarlineGameList
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView

class RealStarlineGameAdapter(
    private var context: Context,
    private var exampleList: List<RealStarlineGamedataholder>

) : RecyclerView.Adapter<RealStarlineGameAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var game_name: ShimmerTextView
        var marketsatus_BG: CardView
        var maincard: CardView
        var result: TextView
        var opentime: TextView
        var goimg: ImageView
        var gamestatus: ShimmerTextView
        var fullwindowlinerlayout: ConstraintLayout

        init {
            game_name = itemView.findViewById(R.id.recyc_gamenametxt)
            maincard = itemView.findViewById(R.id.maincard)
            gamestatus = itemView.findViewById(R.id.recyc_marketstatustxt)
            marketsatus_BG = itemView.findViewById(R.id.bg_marketstatustxt)
            result = itemView.findViewById(R.id.recyc_scorecardtxt)
            opentime = itemView.findViewById(R.id.recyc_OpenTimetxt)
            goimg = itemView.findViewById(R.id.recyc_goimg)
            fullwindowlinerlayout = itemView.findViewById(R.id.homerecycle_mainlayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_startline_game_recycler,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val shimmer = Shimmer()
        shimmer.start(holder.gamestatus)
        holder.setIsRecyclable(false)
        val currentItem = exampleList[position]
        holder.game_name.text = currentItem.game_name
        holder.opentime.text = currentItem.opentime
        if (currentItem.open_result.isEmpty() && currentItem.close_result.isEmpty()) {
            holder.result.text = "XXX-X"
        } else {
            holder.result.text = currentItem.open_result + currentItem.close_result
        }

        if (currentItem.msg_status.equals("1")) {
            holder.gamestatus.text = currentItem.msg
            holder.goimg.setImageResource(R.drawable.ic_play_one)
            holder.maincard.setCardBackgroundColor(context.getColor(R.color.primarydark))
        } else {
            holder.gamestatus.text = currentItem.msg
            holder.maincard.setCardBackgroundColor(context.getColor(R.color.primary))
            holder.goimg.setImageResource(R.drawable.ic_close)
        }

        holder.fullwindowlinerlayout.setOnClickListener {
            if (currentItem.msg_status.equals("1")) {
                val intent = Intent(context, RealStarlineGameList::class.java)
                intent.putExtra("game_id", currentItem.game_id)
                intent.putExtra("game_name", currentItem.game_name)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                vibrate()
            }
        }
    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

}



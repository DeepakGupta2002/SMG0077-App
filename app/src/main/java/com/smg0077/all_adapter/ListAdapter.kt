package com.smg0077.all_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.smg0077.R


class ListAdapter(
    private val context: Context,
    var GameName: Array<String>,
    var GameRate: Array<String>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            viewHolder = ViewHolder()
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.single_task_rate, null, true)

            viewHolder.gamename = view.findViewById(R.id.single_gamenameTxt)
            viewHolder.gamerate = view.findViewById(R.id.single_gamerateTxt)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.gamename?.text = GameName[position]
        viewHolder.gamerate?.text = GameRate[position]
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return GameName.size
    }

    private inner class ViewHolder {
        var gamename: TextView? = null
        var gamerate: TextView? = null
    }
}
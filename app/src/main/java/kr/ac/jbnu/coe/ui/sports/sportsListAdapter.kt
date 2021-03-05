package kr.ac.jbnu.coe.ui.sports

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.coe.R

class sportsListAdapter(val context : Context, val sportsList : ArrayList<sportsItem>, val itemClick : (sportsItem) -> Unit) : RecyclerView.Adapter<sportsListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_sportsitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return sportsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(sportsList[position], context)
    }

    inner class Holder(itemView: View?, itemClick: (sportsItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val roomName = itemView?.findViewById<TextView>(R.id.roomName)
        val date = itemView?.findViewById<TextView>(R.id.date)
        val allPeople = itemView?.findViewById<TextView>(R.id.allPeople)
        val currentPeople = itemView?.findViewById<TextView>(R.id.currentPeople)
        val status = itemView?.findViewById<TextView>(R.id.status)
        val location = itemView?.findViewById<TextView>(R.id.location)
        val event = itemView?.findViewById<TextView>(R.id.event)

        fun bind(sportsItem : sportsItem, context: Context){
            roomName?.text = sportsItem.roomName
            date?.text = sportsItem.date
            allPeople?.text = "모집 : " + sportsItem.allPeople.toString() + "명"
            currentPeople?.text = "현재 : " + sportsItem.currentPeople.toString() + "명"
            status?.text = sportsItem.status
            location?.text = sportsItem.location
            event?.text = "종목 : " + sportsItem.event

            itemView.setOnClickListener{itemClick(sportsItem)}

            if(sportsItem.status == "지원 가능"){
                status?.text = "지원 가능"
                status?.setBackgroundResource(R.drawable.background_status_ok)
            }

            if(sportsItem.status == "지원 불가"){
                status?.text = "지원 불가"
                status?.setBackgroundResource(R.drawable.background_status_fail)
            }
        }
    }
}
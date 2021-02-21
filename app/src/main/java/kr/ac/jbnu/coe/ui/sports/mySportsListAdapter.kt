package kr.ac.jbnu.coe.ui.sports

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.alliance.storeItem

class mySportsListAdapter(val context : Context, val sportsList : ArrayList<mySportsItem>) : RecyclerView.Adapter<mySportsListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_mysportsitem, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return sportsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(sportsList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val name = itemView?.findViewById<TextView>(R.id.name)
        val dept = itemView?.findViewById<TextView>(R.id.dept)
        val phone = itemView?.findViewById<TextView>(R.id.phone)

        fun bind(sportsItem : mySportsItem, context: Context){
            name?.text = sportsItem.name
            dept?.text = sportsItem.dept
            phone?.text = sportsItem.phone
        }
    }
}
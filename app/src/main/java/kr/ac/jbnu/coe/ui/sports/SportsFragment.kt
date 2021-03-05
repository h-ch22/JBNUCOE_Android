package kr.ac.jbnu.coe.ui.sports

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.ornach.nobobutton.NoboButton
import kr.ac.jbnu.coe.R

class SportsFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sports, container, false)
        val btn_check : NoboButton = root.findViewById(R.id.btn_checkStatus)
        val btn_apply : Button = root.findViewById(R.id.btn_apply)
        val btn_account : ImageButton = root.findViewById(R.id.btn_account)
        val btn_checkApply : Button = root.findViewById(R.id.btn_checkApply)

        btn_account.setOnClickListener(this)
        btn_apply.setOnClickListener(this)
        btn_check.setOnClickListener(this)
        btn_checkApply.setOnClickListener(this)

        return root
    }

    override fun onClick(v: View?) {
        if (v!=null){
            if(v.id == R.id.btn_apply){
                val intent = Intent(activity, activity_apply::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_checkStatus){
                val intent = Intent(activity, activity_sportsCheck::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_account){
                val intent = Intent(activity, activity_getMyRoom::class.java)
                intent.putExtra("type", "user")
                startActivity(intent)
            }

            if(v.id == R.id.btn_checkApply){
                val intent = Intent(activity, activity_getMyRoom::class.java)
                intent.putExtra("type", "admin")
                startActivity(intent)
            }
        }
    }
}
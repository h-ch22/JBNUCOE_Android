package kr.ac.jbnu.coe.ui.more


import com.google.firebase.storage.StorageReference
import java.io.Serializable

class feedbackItem(val feedbackTitle : String, val date : String, val type : String, val author : String, var category : String) : Serializable{

}
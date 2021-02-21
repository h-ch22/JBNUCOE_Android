package kr.ac.jbnu.coe.ui.sports

import com.google.firebase.storage.StorageReference
import java.io.Serializable

class sportsItem(val roomName : String, val date : String, val allPeople : Int, val currentPeople : Int, var status : String, val location : String, val event : String, val adminName : String) : Serializable{

}
package kr.ac.jbnu.coe.UserManagement.Models

import androidx.fragment.app.Fragment

interface ViewChangeListener {
    fun replaceView(fragment : Fragment)
}
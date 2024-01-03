package kr.ac.jbnu.coe.UserManagement.Models

data class UserDataModel(val email : String?, val dept : String?, val studentNo : String?) {
    fun getUserInfo() : UserDataModel{
        return this
    }
}
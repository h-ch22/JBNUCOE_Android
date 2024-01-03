package kr.ac.jbnu.coe.UserManagement.Controller

class UserManagementViewController(context : Context) : UserManagement(context) {
    val field_email = ObservableField<String>()
    val field_password = ObservableField<String>()
    private val dialog = DialogManager(context)

    private var email : String?
        get() = field_email.get()
        set(value) = field_email.set(value)

    private var password : String?
        get() = field_password.get()
        set(value) = field_password.set(value)

    private val context = context

    fun onButtonClick(view : View){
        when(view.id){
            R.id.btn_signIn -> {
                if(email?.isNotEmpty() == true && password?.isNotEmpty() == true){
                    signIn(email!!, password!!)
                }

                else{
                    dialog.showAlert("공백 필드", "모든 필드를 채워주세요.")
                }
            }

            R.id.btn_signUp -> (context as activity_start).replaceFragment(SignUpView.newInstance())
        }
    }

    fun changeView(){
        val intent = Intent(context, NavigationViewManager :: class.java)
        context.startActivity(intent)
    }
}
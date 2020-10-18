package io.keepcoding.eh_ho.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.SignUpModel
import io.keepcoding.eh_ho.inflate
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.inputPassword
import kotlinx.android.synthetic.main.fragment_sign_up.inputUsername
import kotlinx.android.synthetic.main.fragment_sign_up.labelCreateAccount

class SignUpFragment : Fragment() {

    var signUpInteractionListener: SignUpInteractionListener?
        = null

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SignUpInteractionListener)
            signUpInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${SignUpInteractionListener::class.java.canonicalName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_sign_up)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSignUp.setOnClickListener {
            val model = SignUpModel(
                inputUsername.text.toString(),
                inputEmail.text.toString(),
                inputPassword.text.toString()
            )

            if (isFormValid())
                signUpInteractionListener?.onSignUp(model)
            else
                showErrors()

        }

        labelCreateAccount.setOnClickListener {
            signUpInteractionListener?.onGoToSignIn()
        }
    }

    private fun showErrors(){
        var hasUserNameErrors: Boolean = false
        var hasPasswordErrors: Boolean = false

        var passwordError = ""
        var userNameError = ""

        if (inputUsername.text.isEmpty())
        {
            hasUserNameErrors = true
            userNameError = "- This field cannot be empty\n"
        }

        if (inputUsername.text.length <= 4) {
            hasUserNameErrors = true
            userNameError += "- The minimun lenght for this field is 5 characters\n"
        }

        if (inputPassword.text.isEmpty())
        {
            hasPasswordErrors = true
            passwordError = "- This field cannot be empty\n"
        }

        if (inputPassword.text.length <= 4)
        {
            hasPasswordErrors = true
            passwordError += "- The minimun lenght for this field is 5 characters\n"
        }

        if (hasUserNameErrors)
            inputUsername.error = userNameError

        if (hasPasswordErrors)
            inputPassword.error = passwordError

        if (inputPassword.text.toString() != inputConfirmPassword.text.toString())
            inputConfirmPassword.error = "The password must be the same "

        if (!inputEmail.text.matches(emailPattern.toRegex()))
            inputEmail.error = "Incorrect format. Example: example@example.com"


    }

    private fun isFormValid() = inputUsername.text.isNotEmpty() &&
            inputPassword.text.isNotEmpty() &&
            inputUsername.text.length > 4 &&
            inputPassword.text.length > 4 &&
            inputPassword.text.toString() == inputConfirmPassword.text.toString() &&
            inputEmail.text.matches(emailPattern.toRegex())


    override fun onDetach() {
        super.onDetach()
        this.signUpInteractionListener = null
    }

    interface SignUpInteractionListener {
        fun onGoToSignIn()
        fun onSignUp(signUpModel: SignUpModel)
    }
}
package com.galboss.protorype

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.galboss.protorype.databinding.FragmentRegisterBinding
import com.galboss.protorype.model.entities.UserLoggin
import com.galboss.protorype.user.responses.LoginResponses
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var user:UserLoggin?=null

    private var _binding: FragmentRegisterBinding?=null
    val binding get() =_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        val root:View= binding.root
        var bottnToLoggin = binding.registerGoLogin
        var nombreUsuarioText = binding.registerUserName
        var emailText = binding.registerEmail
        var passwordText = binding.registerPassword
        var confPass =binding.registerConfPass
        var bottnCrear = binding.registerCrear
        bottnToLoggin.setOnClickListener {
            var argument = Bundle()
            var fragment = LogginFragment()
            var ft =parentFragmentManager.beginTransaction()
            ft.replace(R.id.LogginFragContainer,fragment)
            ft.commit()
        }
        bottnCrear.setOnClickListener {
            if(!nombreUsuarioText.text.toString().isNullOrEmpty()
                &&!emailText.text.toString().isNullOrEmpty()
                &&!passwordText.text.toString().isNullOrEmpty()
                &&!confPass.text.toString().isNullOrEmpty()){
                if(passwordText.text.toString().equals(confPass.text.toString())) {
                        user = UserLoggin(
                            null, nombreUsuarioText.text.toString(),
                            emailText.text.toString(), passwordText.text.toString(),
                            false, 0, 0, 0
                        )
                    crearusuario(user!!)
                    MaterialAlertDialogBuilder(this.requireContext()).setTitle("Cuenta creada")
                        .setMessage("La cuenta ha sido creada correctamente")
                        .setPositiveButton("Ok"){dialog, which->}
                        .show()
                } else{
                    MaterialAlertDialogBuilder(this.requireContext()).setTitle("Error")
                        .setMessage("Verifique que las contraseñas coincidan")
                        .setPositiveButton("Ok"){dialog, which->}
                        .show()
                }
            }
        }

        return root
    }

    fun crearusuario(user:UserLoggin){
        lifecycleScope.launch {
            var responses = LoginResponses()
            var data = responses.crearUsuario(user)
            Log.i("Se obtuvo","${data.body()!!.toString()}")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
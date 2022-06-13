package com.galboss.protorype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.galboss.protorype.databinding.FragmentLogginBinding
import com.galboss.protorype.model.entities.User
import com.galboss.protorype.model.entities.UserLoggin
import com.galboss.protorype.user.responses.LoginResponses
import com.galboss.protorype.utils.RetrofitInstance
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLogginBinding? =null
    val binding get()= _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLogginBinding.inflate(inflater,container,false)
        val root:View=binding.root
        var bottnToRegister = binding.logginRegistrarse
        var bottnIngresar = binding.logginIngresar
        var emailText =binding.logginEmail
        var passText = binding.logginPassword
        bottnIngresar.setOnClickListener {
            if(!emailText.text.toString().isNullOrEmpty()
                &&!passText.text.toString().isNullOrEmpty()) {
                var user = UserLoggin(
                    null, null, null,
                    null, null, null, null, null
                )
                user.email = emailText.text.toString()
                user.password = passText.text.toString()
                ingresar(user)
            }
        }
        bottnToRegister.setOnClickListener(){
            var argument = Bundle()
            var fragment = RegisterFragment()
            var ft =parentFragmentManager.beginTransaction()
            ft.replace(R.id.LogginFragContainer,fragment)
            ft.commit()
        }
        viewModel.user.observe(this.viewLifecycleOwner, Observer {
            Log.i("Llego","${viewModel.user.value.toString()}")
            if(viewModel.user.value!=null){
                if(viewModel.user.value!!._id.isNullOrEmpty()){
                    MaterialAlertDialogBuilder(this.requireContext()).setTitle("Error")
                        .setMessage("El correo o contraseña son incorrectos")
                        .setPositiveButton("Ok"){dialog, which->}
                        .show()
                }else{
                    Log.i("Usuario obtenido","${viewModel.user.value!!.toString()}")
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent (this.activity,MainActivity::class.java)
                        intent.putExtra("userId",viewModel.user.value!!._id)
                        startActivity(intent)
                        activity?.finish()
                    },1500)
                }
            }

        });

        return root
    }

    fun ingresar (user:UserLoggin){
        lifecycleScope.launch {
            var responses = LoginResponses()
            var data = responses.login(user)
            viewModel.setUser(data.body()!!)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
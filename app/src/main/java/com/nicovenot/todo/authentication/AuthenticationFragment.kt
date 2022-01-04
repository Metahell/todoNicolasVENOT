package com.nicovenot.todo.authentication


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nicovenot.todo.AuthenticationActivity
import com.nicovenot.todo.form.FormActivity
import com.nicovenot.todo.MainActivity
import com.nicovenot.todo.R
import com.nicovenot.todo.databinding.FragmentAuthentificationBinding
import com.nicovenot.todo.model.Task
import com.nicovenot.todo.network.Api
import kotlinx.coroutines.MainScope

class AuthenticationFragment() : Fragment() {

    private var _binding: FragmentAuthentificationBinding? = null
    private val binding get() = _binding!!

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthentificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.co.setOnClickListener {
            findNavController().navigate(R.id.loginFrag)
        }
        binding.sub.setOnClickListener {
            findNavController().navigate(R.id.signupFrag)
        }

    }

    override fun onResume() {
        super.onResume()
        if (Api.TOKEN != "null") {
            val intent = Intent(activity, MainActivity::class.java)
            formLauncher.launch(intent)
        }
    }
}
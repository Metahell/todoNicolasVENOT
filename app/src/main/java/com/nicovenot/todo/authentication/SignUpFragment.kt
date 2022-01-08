package com.nicovenot.todo.authentication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nicovenot.todo.MainActivity
import com.nicovenot.todo.R
import com.nicovenot.todo.UserInfoActivity
import com.nicovenot.todo.databinding.FragmentSignupBinding
import com.nicovenot.todo.model.RegisterForm
import com.nicovenot.todo.model.Task
import com.nicovenot.todo.network.Api
import com.nicovenot.todo.viewModel.UserInfoViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import com.nicovenot.todo.authentication.SHARED_PREF_TOKEN_KEY

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private var viewModel = UserInfoViewModel();
    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.suIns.setOnClickListener {
            val fname = binding.suFstname.text.toString()
            val name = binding.suName.text.toString()
            val mail = binding.suMail.text.toString()
            val mdp = binding.suMdp.text.toString()
            val mdp2 = binding.suMdp2.text.toString()
            if (fname.isEmpty() || name.isEmpty() || mail.isEmpty()|| mdp.isEmpty() || mdp2.isEmpty()) {
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            } else {
                val data = RegisterForm(fname, name, mail, mdp, mdp2)
                lifecycleScope.launch() {
                    val token = viewModel.addAccount(data)
                    if (token == null) {
                        Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()
                    } else {
                        val editor = activity!!.getSharedPreferences("TOKEN_SHARE", Context.MODE_PRIVATE).edit()
                        editor.putString("TOKEN", token.token)
                        editor.apply()
                        val intent = Intent(activity, MainActivity::class.java)
                        formLauncher.launch(intent)
                    }
                }
            }
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
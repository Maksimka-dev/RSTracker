package com.rshack.rstracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.FragmentLoginBinding
import com.rshack.rstracker.utils.AuthUiState
import com.rshack.rstracker.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel.authResult.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            viewModel.clearAuthResult()
            when (it) {
                AuthUiState.Loading -> showLoadingState()
                AuthUiState.Success -> navigateMapFragment()
                AuthUiState.Error -> showErrorState()
            }
        })

        binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                val email: String = tv_email.text.toString()
                val password: String = tv_password.text.toString()
                viewModel.login(email, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseApp.getApps(requireContext()).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
        if (viewModel.getFirebaseAuth().currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
        }
    }

    private fun validateFields(): Boolean {
        if (binding.tvEmail.text.isNullOrEmpty() || binding.tvPassword.text.isNullOrEmpty()) {
            showError("Please fill in the required fields")
            return false
        }
        return true
    }

    private fun showLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
    }

    private fun navigateMapFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
        view?.clearFocus()
    }

    private fun showErrorState() {
        binding.progressBar.visibility = View.GONE
        binding.btnLogin.isEnabled = true
        showError("Wrong login or password")
    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.rshack.rstracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.FragmentRegisterBinding
import com.rshack.rstracker.utils.AuthUiState
import com.rshack.rstracker.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

const val MIN_PASSWORD_LENGTH = 6

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        viewModel.authResult.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            when (it) {
                AuthUiState.Loading -> showLoadingState()
                AuthUiState.Success -> navigateMapFragment()
                AuthUiState.Error -> showErrorState()
            }
        })

        binding.btnRegister.setOnClickListener {
            if (validateFields()) {
                val email: String = tv_email.text.toString()
                val password: String = tv_password.text.toString()
                viewModel.register(email, password)
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun validateFields(): Boolean {
        var isValid = false
        when {
            binding.tvEmail.text.isNullOrEmpty() -> showError("Email is empty")
            binding.tvPassword.text.isNullOrEmpty() -> showError("Password is empty")
            binding.tvPassword.text.length < MIN_PASSWORD_LENGTH -> showError("Password is too short (6 min)")
            else -> isValid = true
        }
        return isValid
    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorState() {
        binding.progressBar.visibility = View.GONE
        binding.btnRegister.isEnabled = true
        showError("The email address is badly formatted")
    }

    private fun navigateMapFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_mapFragment)
    }

    private fun showLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

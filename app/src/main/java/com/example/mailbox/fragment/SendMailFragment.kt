package com.example.mailbox.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mailbox.R
import com.example.mailbox.databinding.FragmentSendMailBinding
import com.example.mailbox.model.AccResponse
import com.example.mailbox.service.PreferencesProvider
import com.example.mailbox.service.RestClientAuth
import com.example.mailbox.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendMailFragment : BaseFragment() {
    private var _binding: FragmentSendMailBinding? = null
    private val binding: FragmentSendMailBinding get() = _binding!!
    private lateinit var preferenceProvider: PreferencesProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendMailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceProvider = PreferencesProvider(requireActivity())
        emailAddressShow()
    }

    private fun emailAddressShow() {
        showProgress()
        val userId = preferenceProvider.getString(SharedPrefData.USER_ID)
        RestClientAuth.authToken(preferenceProvider.getString(SharedPrefData.TOKEN)!!)
        val getAcc = RestClientAuth.get().getAccount(userId!!)
        getAcc.enqueue(object :
            Callback<AccResponse> {
            override fun onFailure(call: Call<AccResponse>, t: Throwable) {
                hideProgress()
                snackBar("Please check your connection.")
            }

            override fun onResponse(
                call: Call<AccResponse>,
                response: Response<AccResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    hideProgress()
                    getDataSuccess(response.body()!!)
                } else {
                    hideProgress()
                    snackBar("Something went wrong!!\nPlease login your account again.")
                }
            }
        })
    }

    private fun getDataSuccess(body: AccResponse) {
        val emailAddress = body.address
        preferenceProvider.putString(SharedPrefData.USER_ADDRESS, emailAddress)
        binding.tvUserAddress.text = emailAddress

        binding.sendMailBtn.onClick { if (validate()) sendToMail(emailAddress) }
    }

    private fun validate(): Boolean {
        return when {
            binding.etMailSubject.checkIsEmpty() -> {
                binding.etMailSubject.showError(getString(R.string.error_field_required))
                false
            }
            binding.etMailText.checkIsEmpty() -> {
                binding.etMailText.showError(getString(R.string.error_field_required))
                false
            }
            else -> true
        }
    }

    private fun sendToMail(emailAddress: String) {
        val subject = binding.etMailSubject.text.toString().trim()
        val textMessage = binding.etMailText.text.toString().trim()
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, textMessage)

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }
}
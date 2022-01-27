package com.example.mailbox.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mailbox.databinding.FragmentInboxBinding
import com.example.mailbox.service.PreferencesProvider

class InboxFragment : BaseFragment() {
    private var _binding: FragmentInboxBinding? = null
    private val binding: FragmentInboxBinding get() = _binding!!
    private lateinit var preferenceProvider: PreferencesProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceProvider = PreferencesProvider(requireActivity())
    }
}
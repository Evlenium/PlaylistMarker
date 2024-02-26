package com.practicum.playlistmarker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentSettingsBinding
import com.practicum.playlistmarker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonShareApplication.setOnClickListener {
                settingsViewModel.shareApp()
            }
            buttonSupport.setOnClickListener {
                settingsViewModel.openSupport()
            }
            buttonTermsOfUse.setOnClickListener {
                settingsViewModel.openTerms()
            }
            swTheme.setOnCheckedChangeListener { switcher, checked ->
                onThemeChanged(checked)
            }
            toolbarSettings.setTitleTextAppearance(
                requireContext(),
                R.style.SecondsActivityMediumTextAppearance
            )
        }
        observeText()
    }

    private fun onThemeChanged(isDark: Boolean) {
        settingsViewModel.onThemeChanged(isDark)
    }

    private fun observeText() {
        settingsViewModel.observeThemeLiveData().observe(viewLifecycleOwner) {
            binding.swTheme.isChecked = it
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
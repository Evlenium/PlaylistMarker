package com.practicum.playlistmarker.new_playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmarker.new_playlist.presentation.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding: FragmentNewPlaylistBinding
        get() = _binding!!

    private val newPlaylistViewModel by viewModel<NewPlaylistViewModel>()

    private var inputTextFromName: String? = null
    private lateinit var inputEditTextName: EditText
    private var inputTextFromDescription: String? = null
    private lateinit var inputEditTextDescription: EditText
    private var uriPicture: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMediaPicture =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.apply {
                        uriPicture = uri.toString()
                        imagePlaylist.background = null
                        imagePlaylist.setImageURI(uri)
                    }
                }
            }

        binding.imagePlaylist.setOnClickListener {
            pickMediaPicture.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.toolbarPlaylist.apply {
            setNavigationIcon(R.drawable.bt_arrow_back_mode)
            setNavigationOnClickListener {
                backPressed()
            }
            setTitleTextAppearance(
                requireContext(),
                R.style.SecondsActivityMediumTextAppearance
            )
        }

        binding.apply {
            inputEditTextName = binding.playlistNameEditText
            inputEditTextName.addTextChangedListener(
                beforeTextChanged = { s, start, count, after -> },
                onTextChanged = { s, start, before, count ->
                    if (s != null) {
                        if (s.isNotEmpty()) {
                            inputTextFromName = s.toString()
                            binding.buttonCreatePlaylist.isEnabled = true
                        } else {
                            binding.buttonCreatePlaylist.isEnabled = false
                        }
                    }
                },
                afterTextChanged = { s ->
                    inputTextFromName = s.toString()
                }
            )
            inputEditTextDescription = binding.playlistDescriptionEditText
            inputEditTextDescription.addTextChangedListener(
                beforeTextChanged = { s, start, count, after -> },
                onTextChanged = { s, start, before, count -> },
                afterTextChanged = { s ->
                    inputTextFromName = s.toString()
                }
            )
            buttonCreatePlaylist.setOnClickListener {
                if (inputTextFromName != null) {
                    newPlaylistViewModel.savePlaylist(
                        inputTextFromName!!,
                        inputTextFromDescription,
                        uriPicture
                    )
                    findNavController().popBackStack()
                    Snackbar
                        .make(view, "Плейлист $inputTextFromName создан", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner)
        {
            backPressed()
        }
    }

    private fun backPressed() {
        if (inputTextFromName.isNullOrEmpty() && inputTextFromDescription.isNullOrEmpty() && uriPicture.isNullOrEmpty()) {
            findNavController().popBackStack()
        } else {
            showDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogStyle)
            .setTitle("Завершить создание плейлиста")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, which ->
            }
            .setPositiveButton("Завершить") { dialog, which ->
                newPlaylistViewModel.savePlaylist(
                    inputTextFromName!!,
                    inputTextFromDescription,
                    uriPicture
                )
                findNavController().popBackStack()
                Snackbar
                    .make(requireView(), "Плейлист $inputTextFromName создан", Snackbar.LENGTH_LONG)
                    .show()
            }
            .show()
    }
}
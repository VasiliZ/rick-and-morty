package com.github.rtyvz.senla.tr.rick_and_morty.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.rick_and_morty.R

class ErrorLoadSingleCharacterDialogFragment : DialogFragment() {
    private var handleClickListener: HandleClickErrorLoadSingleCharacter? = null

    companion object {
        val TAG: String = ErrorLoadSingleCharacterDialogFragment::class.java.toString()
    }

    override fun onAttach(context: Context) {
        handleClickListener = (activity as HandleClickErrorLoadSingleCharacter)

        super.onAttach(context)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_retry_load))
            .setPositiveButton(getString(R.string.dialog_ok_label)) { _, _ ->
                handleClickListener?.handlePositiveActionErrorLoadSingleCharacterDialog()
            }.setNegativeButton(getString(R.string.dialog_no_label)) { dialog, _ ->
                dialog.dismiss()
                handleClickListener?.handleNegativeActionErrorLoadSingleCharacterDialog()
            }.create()
    }

    override fun onDetach() {
        handleClickListener = null

        super.onDetach()
    }
}

interface HandleClickErrorLoadSingleCharacter {
    fun handlePositiveActionErrorLoadSingleCharacterDialog()
    fun handleNegativeActionErrorLoadSingleCharacterDialog()
}
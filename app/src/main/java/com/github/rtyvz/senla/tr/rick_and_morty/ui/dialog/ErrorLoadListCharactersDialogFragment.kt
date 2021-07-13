package com.github.rtyvz.senla.tr.rick_and_morty.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.rick_and_morty.R

class ErrorLoadListCharactersDialogFragment : DialogFragment() {

    private var handleClickListener: HandleClickErrorLoadCharactersDialog? = null

    companion object {
        val TAG: String = ErrorLoadListCharactersDialogFragment::class.java.toString()
    }

    override fun onAttach(context: Context) {
        handleClickListener = (activity as HandleClickErrorLoadCharactersDialog)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_retry_load))
            .setPositiveButton(getString(R.string.dialog_ok_label)) { _, _ ->
                handleClickListener?.handlePositiveActionErrorLoadListCharactersDialog()
            }.setNegativeButton(getString(R.string.dialog_no_label)) { dialog, _ ->
                dialog.dismiss()
                handleClickListener?.handleNegativeActionErrorLoadListCharacterDialog()
            }.create()
    }

    override fun onDetach() {
        handleClickListener = null

        super.onDetach()
    }
}

interface HandleClickErrorLoadCharactersDialog {
    fun handlePositiveActionErrorLoadListCharactersDialog()
    fun handleNegativeActionErrorLoadListCharacterDialog()
}
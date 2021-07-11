package com.github.rtyvz.senla.tr.rick_and_morty.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.rick_and_morty.R

class ErrorDialogFragment(
    private val positiveAction: () -> (Unit)
) : DialogFragment() {

    companion object {
        val TAG: String = ErrorDialogFragment::class.java.toString()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_retry_load))
            .setPositiveButton(getString(R.string.dialog_ok_label)) { _, _ ->
                positiveAction()
            }.setNegativeButton(getString(R.string.dialog_no_label)) { dialog, _ ->
                dialog.dismiss()
            }.create()
    }
}
package hu.bme.aut.android.kliensalk_hf_2_android.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import hu.bme.aut.android.kliensalk_hf_2_android.R
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityAddReviewBinding

class NewReviewDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "NewSReviewDialogFragment"
    }

    private lateinit var listener: NewReviewDialogListener

    private var _binding: ActivityAddReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: FragmentActivity = activity!!
        listener = if (activity is NewReviewDialogListener) {
            activity
        } else {
            throw RuntimeException("Activity must implement the NewReviewDialogListener interface!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ActivityAddReviewBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireActivity())
            .setTitle(getText(R.string.add_review_title))
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (isValid()) {
                    listener.onShoppingItemCreated(getShoppingItem())
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    interface NewReviewDialogListener {
        fun onShoppingItemCreated(item: Review)
    }

    private fun isValid(): Boolean {
        return binding.etTitle.text.isNotEmpty()
    }

    private fun getShoppingItem(): Review {
        return Review(
            userCreatorId = arguments!!.getLong("userId", 0),
            title = binding.etTitle.text.toString()
        )
    }
}
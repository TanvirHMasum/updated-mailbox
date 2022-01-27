package com.example.mailbox.fragment

import android.annotation.SuppressLint
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.mailbox.R
import com.example.mailbox.activity.AppBaseActivity

abstract class BaseFragment : Fragment(), View.OnFocusChangeListener {
    @SuppressLint("UseCompatLoadingForDrawables", "NewApi")

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            (v as EditText).setTextColor(requireActivity().getColor(R.color.colorPrimaryDark))
            v.background = requireActivity().getDrawable(R.drawable.bg_ractangle_rounded_active)
        } else {
            (v as EditText).setTextColor(requireActivity().getColor(R.color.textColorPrimary))
            v.background = requireActivity().getDrawable(R.drawable.bg_ractangle_rounded_inactive)
        }
    }

    fun hideProgress() {
        if (activity != null)
            (requireActivity() as AppBaseActivity).showProgress(false)
    }

    fun showProgress() {
        if (activity != null)
            (requireActivity() as AppBaseActivity).showProgress(true)
    }
}
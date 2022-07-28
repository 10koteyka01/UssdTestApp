package com.demo.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.UssdResponseCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.myapplication.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            )

            if (permission != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(
                    activity as Activity,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    1
                )

//            ActivityCompat.requestPermissions(
//                activity as Activity,
//                arrayOf(Manifest.permission.CALL_PHONE),
//                1
//            )
            val manager =
                requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            manager
                .sendUssdRequest(
                    "*100#", object : UssdResponseCallback() {
                        override fun onReceiveUssdResponse(
                            telephonyManager: TelephonyManager?,
                            request: String?,
                            response: CharSequence?
                        ) {
                            super.onReceiveUssdResponse(telephonyManager, request, response)
                            binding.textviewFirst.setText(response.toString())
                        }

                        override fun onReceiveUssdResponseFailed(
                            telephonyManager: TelephonyManager?,
                            request: String?,
                            failureCode: Int
                        ) {
                            super.onReceiveUssdResponseFailed(
                                telephonyManager,
                                request,
                                failureCode
                            )
                            binding.textviewFirst.setText(failureCode.toString())
                        }
                    },
                    Handler.createAsync(Looper.getMainLooper())
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
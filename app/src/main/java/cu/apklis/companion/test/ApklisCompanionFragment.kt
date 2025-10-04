package cu.apklis.companion.test

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cu.apklis.companion.sdk.core.ApklisCompanion
import cu.apklis.companion.sdk.utils.Utils
import cu.apklis.companion.test.databinding.FragmentApkliscompanionBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ApklisCompanionFragment : Fragment() {

    private var _binding: FragmentApkliscompanionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApkliscompanionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }

    private fun initComponents() {
        binding.apply {
            validateButton.setOnClickListener {
                binding.packageIdTi.error = null
                val packageId = binding.packageId.text.toString().trim()
                var cancel = false
                var focusView: View? = null
                if (TextUtils.isEmpty(packageId)) {
                    binding.packageIdTi.error = getString(R.string.error_field_required)
                    focusView = binding.packageId
                    cancel = true
                }
                if (cancel) {
                    focusView?.requestFocus()
                } else {

                    val result = ApklisCompanion.isPurchased(requireContext(), packageId)
                    MaterialAlertDialogBuilder(requireContext())
                        .setIcon(R.drawable.info_24px)
                        .setTitle(getString(R.string.result))
                        .setMessage(
                            result.toString()
                        )
                        .setCancelable(false)
                        .setNeutralButton(getString(R.string.close)) { dialog, _ ->
                            dialog.dismiss()
                        }

                        .show()
                    Log.i("ApklisCompanionFragment", "Result: $result")
                }

            }


            launchLicenseButton.setOnClickListener {
                binding.licensePackageIdTi.error = null
                binding.publicPemKeyTi.error = null
                binding.licenceUuidTi.error = null
                val packageId = binding.licensePackageId.text.toString().trim()
                val pemKey = binding.publicPemKey.text.toString().trim()
                val licenseUuid = binding.licenceUuid.text.toString().trim()
                var cancel = false
                var focusView: View? = null
                if (TextUtils.isEmpty(packageId)) {
                    binding.licensePackageIdTi.error = getString(R.string.error_field_required)
                    focusView = binding.licensePackageId
                    cancel = true
                }
                if (TextUtils.isEmpty(pemKey)) {
                    binding.publicPemKeyTi.error = getString(R.string.error_field_required)
                    focusView = binding.publicPemKey
                    cancel = true
                }
                if (TextUtils.isEmpty(licenseUuid)) {
                    binding.licenceUuidTi.error = getString(R.string.error_field_required)
                    focusView = binding.licenceUuid
                    cancel = true
                }
                if (cancel) {
                    focusView?.requestFocus()
                } else {
                    Utils.openApklisCompanionLink(requireContext(), packageId, licenseUuid, pemKey)

                }
            }
            validateLicenseButton.setOnClickListener {
                binding.licensePackageIdTi.error = null
                val packageId = binding.licensePackageId.text.toString().trim()
                var cancel = false
                var focusView: View? = null

                if (TextUtils.isEmpty(packageId)) {
                    binding.licensePackageIdTi.error = getString(R.string.error_field_required)
                    focusView = binding.licensePackageId
                    cancel = true
                }

                if (cancel) {
                    focusView?.requestFocus()
                } else {
                    val result = ApklisCompanion.isLicensePurchased(requireContext(), packageId)
                    MaterialAlertDialogBuilder(requireContext())
                        .setIcon(R.drawable.info_24px)
                        .setTitle(getString(R.string.result))
                        .setMessage(
                            result.toString()
                        )
                        .setCancelable(false)
                        .setNeutralButton(getString(R.string.close)) { dialog, _ ->
                            dialog.dismiss()
                        }

                        .show()
                    Log.i("ApklisCompanionFragment", "Result: $result")
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
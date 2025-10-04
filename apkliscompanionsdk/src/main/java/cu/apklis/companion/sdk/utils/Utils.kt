package cu.apklis.companion.sdk.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.net.toUri
import com.orhanobut.logger.Logger

object Utils {
    private const val LICENSE_SCHEME = "apklis_license_payment_check"
    private const val APP_SCHEME = "apklis_payment_check"
    private const val PUBLIC_PEM_KEY = "publicKeyPem"
    private const val LICENSE_UUID = "licenseUuid"
    private const val APKLIS_APP_ID = "cu.uci.android.apklis"
    private const val APKLIS_COMPANION_APP_ID = "cu.apklis.companion"

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.getPackageInfoCompat(packageName, 0)
            true
        } catch (unused: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun Context.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(flags.toLong())
            )
        } else {
            packageManager.getPackageInfo(packageName, flags)
        }

    fun openApklisLink(context: Context, link: String): Boolean {

        val isApklisCompanionInstalled = isAppInstalled(context, APKLIS_COMPANION_APP_ID)
        val isApklisInstalled = isAppInstalled(context, APKLIS_APP_ID)

        return if (isApklisCompanionInstalled && openApkLisPaymentCheckLink(
                context,
                getApklisCompanionUrl(link)
            )
        )
            true
        else if (isApklisInstalled && openApkLisLink(
                context,
                getApklisUrl(link)
            )
        )
            true
        else {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Logger.e("La app Apklis no está instalada en el dispositivo")

            false
        }

    }

    fun openApklisCompanionLink(
        context: Context,
        applicationId: String,
        licenseUuid: String,
        publicKeyPem: String
    ): Boolean {

        val isApklisCompanionInstalled = isAppInstalled(context, APKLIS_COMPANION_APP_ID)

        return if (isApklisCompanionInstalled && openApkLisPaymentCheckLink(
                context,
                getApklisCompanionLicenseUrl(applicationId, licenseUuid, publicKeyPem)
            )
        )
            true
        else {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, applicationId.toUri()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Logger.e("La app ApklisCompanion no está instalada en el dispositivo")

            false
        }

    }

    private fun openApkLisPaymentCheckLink(context: Context, link: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
        val isAppInstalled = isAppInstalled(context, APKLIS_COMPANION_APP_ID)
        if (isAppInstalled) {
            intent.setPackage(APKLIS_COMPANION_APP_ID)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun openApkLisLink(context: Context, link: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
        val isAppInstalled = isAppInstalled(context, APKLIS_APP_ID)
        if (isAppInstalled) {
            intent.setPackage(APKLIS_APP_ID)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {

            false
        }
    }


    fun getApklisUrl(applicationId: String): String {
        return "https://www.apklis.cu/application/$applicationId"
    }

    fun getApklisCompanionUrl(applicationId: String): String {
        return "${APP_SCHEME}://$applicationId"
    }

    fun getApklisCompanionLicenseUrl(
        applicationId: String,
        licenseUuid: String,
        publicKeyPem: String
    ): String {
        return "${LICENSE_SCHEME}://$applicationId/?${PUBLIC_PEM_KEY}=$publicKeyPem&${LICENSE_UUID}=$licenseUuid"
    }

}
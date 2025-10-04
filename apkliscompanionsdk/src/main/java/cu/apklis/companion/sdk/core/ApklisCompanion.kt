package cu.apklis.companion.sdk.core

import android.content.Context
import android.os.Build
import androidx.core.net.toUri
import com.orhanobut.logger.Logger
import cu.apklis.companion.sdk.models.ApklisLicenseResponse
import cu.apklis.companion.sdk.models.ApklisResponse
import cu.apklis.companion.sdk.models.LicenseStatus
import cu.apklis.companion.sdk.models.PaymentStatus

object ApklisCompanion {
    private const val PROVIDER = "content://cu.apklis.companion.payment.provider/app/"
    private const val LICENSE_PROVIDER = "content://cu.apklis.companion.license.Provider/licenses/"
    private const val PAID = "paid"
    private const val UUID = "uuid"
    private const val LICENSE_UUID = "license_uuid"
    private const val STATUS = "status"
    private const val EXPIRED_IN = "expire_in"

    fun isPurchased(context: Context, applicationId: String): ApklisResponse {
        val apklisResponse = ApklisResponse()
        val providerURI = (PROVIDER + applicationId).toUri()
        try {
            val contentResolver = context.contentResolver.acquireContentProviderClient(providerURI)
            val cursor = contentResolver?.query(providerURI, null, null, null, null)
            if (cursor == null) {
                apklisResponse.status = PaymentStatus.ERROR
                apklisResponse.message =
                    Exception("La aplicación no se encontró en ApklisCompanion")
            }
            if (cursor?.moveToFirst() == true) {
                do {
                    val apklisPay = cursor.getInt(cursor.getColumnIndexOrThrow(PAID))
                    val apklisUserNAme =
                        cursor.getString(cursor.getColumnIndexOrThrow("user_name"))
                    apklisResponse.userName = apklisUserNAme

                    if (apklisPay > 0) {
                        apklisResponse.status = PaymentStatus.PAYED
                    }
                } while (cursor.moveToNext())
            } else {
                apklisResponse.status = PaymentStatus.ERROR
                apklisResponse.message =
                    Exception("La aplicación no se encontró en ApklisCompanion")
            }
            if (Build.VERSION.SDK_INT >= 24) {
                contentResolver?.close()
            } else {
                @Suppress("DEPRECATION") contentResolver?.release()
            }
            cursor?.close()

        } catch (e: Exception) {
            return ApklisResponse(status = PaymentStatus.EXCEPTION, message = e)
        }
        return apklisResponse
    }

    fun isLicensePurchased(context: Context, packageId: String): ApklisLicenseResponse {
        val apklisLicenseResponse = ApklisLicenseResponse()
        val providerURI = (LICENSE_PROVIDER + packageId).toUri()
        try {
            val contentResolver = context.contentResolver.acquireContentProviderClient(providerURI)
            val cursor = contentResolver?.query(providerURI, null, null, null, null)
            if (cursor == null) {
                apklisLicenseResponse.status = LicenseStatus.ERROR
                apklisLicenseResponse.message =
                    Exception("La licencia no se encontró en ApklisCompanion")
            }
            if (cursor?.moveToFirst() == true) {
                do {
                    val resultUuid =
                        cursor.getString(cursor.getColumnIndexOrThrow(UUID))
                    val licenseUuid =
                        cursor.getString(cursor.getColumnIndexOrThrow(LICENSE_UUID))
                    val licenseStatus = cursor.getString(cursor.getColumnIndexOrThrow(STATUS))
                    val expiredIn = cursor.getString(cursor.getColumnIndexOrThrow(EXPIRED_IN))
                    apklisLicenseResponse.licenseUuid = licenseUuid
                    apklisLicenseResponse.uuid = resultUuid
                    apklisLicenseResponse.expiredIn = expiredIn
                    apklisLicenseResponse.status=when(licenseStatus){
                        "PAYED" -> LicenseStatus.PAYED
                        "PENDING" -> LicenseStatus.PENDING
                        "CANCELLED" -> LicenseStatus.CANCELLED
                        "INVALID" -> LicenseStatus.INVALID
                        "EXPIRED" -> LicenseStatus.EXPIRED
                        else -> LicenseStatus.ERROR
                    }
                    Logger.i("apklisCheck %s", apklisLicenseResponse)

                } while (cursor.moveToNext())
            } else {
                apklisLicenseResponse.status = LicenseStatus.ERROR
                apklisLicenseResponse.message =
                    Exception("La licencia no se encontró en ApklisCompanion")
            }
            if (Build.VERSION.SDK_INT >= 24) {
                contentResolver?.close()
            } else {
                @Suppress("DEPRECATION") contentResolver?.release()
            }
            cursor?.close()

        } catch (e: Exception) {
            apklisLicenseResponse.status = LicenseStatus.ERROR
            apklisLicenseResponse.message = e

        }
        return apklisLicenseResponse
    }


}
package cu.apklis.companion.sdk.models

class ApklisResponse(
    var userName: String? = null,
    var status: PaymentStatus = PaymentStatus.NOT_PAYED,
    var message: Exception? = null
) {

    fun isValid(): Boolean {
        return PaymentStatus.PAYED == status
    }

    override fun toString(): String {
        return "User: $userName\n" +
                "Status: $status\n" +
                "Message: ${message?.message}"
    }


}

class ApklisLicenseResponse(
    var licenseUuid: String? = null,
    var uuid: String? = null,
    var expiredIn: String? = null,
    var status: LicenseStatus = LicenseStatus.PENDING,
    var message: Exception? = null
) {

    fun isValid(): Boolean {
        return LicenseStatus.PAYED == status
    }

    override fun toString(): String {
        return "License Uuid: $licenseUuid \n" +
                "Uuid: $uuid \n" +
                "Expired In: $expiredIn \n" +
                "Status: $status \n" +
                "Message: ${message?.message}"
    }

}

enum class PaymentStatus {
    NOT_PAYED, PAYED, ERROR, EXCEPTION
}

enum class LicenseStatus {
    PENDING, PAYED, EXPIRED, CANCELLED, INVALID, ERROR, SIGNATURE_INVALID, NOT_PAYED
}

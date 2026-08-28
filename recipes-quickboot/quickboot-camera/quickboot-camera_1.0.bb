SUMMARY = "Early camera boot optimization"
DESCRIPTION = "Preloads the camera driver stack during early boot"

require ../quickboot-common.inc

QCOM_QUICKBOOT_COMMON_CONFIG = "camera-modules-common.conf"
QCOM_QUICKBOOT_MACHINE_CONFIG = "camera-modules-${MACHINE}.conf"
QCOM_QUICKBOOT_OUTPUT_CONFIG = "camera-modules.conf"

SRC_URI = " \
    file://${QCOM_QUICKBOOT_COMMON_CONFIG} \
    file://${QCOM_QUICKBOOT_MACHINE_CONFIG} \
"

RDEPENDS:${PN} += "camx-dlkm"

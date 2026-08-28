SUMMARY = "Early display boot optimization"
DESCRIPTION = "Preloads the display driver stack during early boot"

require ../quickboot-common.inc

QCOM_QUICKBOOT_COMMON_CONFIG = "display-modules-common.conf"
QCOM_QUICKBOOT_MACHINE_CONFIG = "display-modules-${MACHINE}.conf"
QCOM_QUICKBOOT_OUTPUT_CONFIG = "display-modules.conf"

SRC_URI = " \
    file://${QCOM_QUICKBOOT_COMMON_CONFIG} \
    file://${QCOM_QUICKBOOT_MACHINE_CONFIG} \
"

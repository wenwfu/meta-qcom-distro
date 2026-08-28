SUMMARY = "Early audio boot optimization"
DESCRIPTION = "Preloads the audio driver stack during early boot"

require ../quickboot-common.inc

QCOM_QUICKBOOT_AUDIO_BACKEND = "${@bb.utils.contains('BBFILE_COLLECTIONS', \
    'meta-audioreach', 'audioreach', 'oss', d)}"

QCOM_QUICKBOOT_COMMON_CONFIG = "audio-modules-common.conf"
QCOM_QUICKBOOT_MACHINE_CONFIG = \
    "audio-modules-${MACHINE}-${QCOM_QUICKBOOT_AUDIO_BACKEND}.conf"
QCOM_QUICKBOOT_OUTPUT_CONFIG = "audio-modules.conf"

SRC_URI = " \
    file://${QCOM_QUICKBOOT_COMMON_CONFIG} \
    file://${QCOM_QUICKBOOT_MACHINE_CONFIG} \
"

RDEPENDS:${PN} += "${@bb.utils.contains('BBFILE_COLLECTIONS', \
    'meta-audioreach', ' audioreach-kernel', '', d)}"

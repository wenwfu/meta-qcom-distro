require recipes-quickboot/quickboot-support.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

S = "${UNPACKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "${@qcom_quickboot_compatible_machine(d)}"

QCOM_QUICKBOOT_COMMON_CONFIG ??= ""
QCOM_QUICKBOOT_MACHINE_CONFIG ??= ""
QCOM_QUICKBOOT_OUTPUT_CONFIG ??= ""

do_install() {
    install -D -m 0644 ${UNPACKDIR}/${QCOM_QUICKBOOT_COMMON_CONFIG} \
        ${D}${sysconfdir}/modules-load.d/${QCOM_QUICKBOOT_OUTPUT_CONFIG}

    cat ${UNPACKDIR}/${QCOM_QUICKBOOT_MACHINE_CONFIG} \
        >> ${D}${sysconfdir}/modules-load.d/${QCOM_QUICKBOOT_OUTPUT_CONFIG}
}

FILES:${PN} = "${sysconfdir}/modules-load.d/${QCOM_QUICKBOOT_OUTPUT_CONFIG}"

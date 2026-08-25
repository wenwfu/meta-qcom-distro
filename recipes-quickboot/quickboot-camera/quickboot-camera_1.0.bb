SUMMARY = "Early Camera Boot Optimizations"
DESCRIPTION = "Installs kernel module load list for faster camera \
driver probes during boot"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://camera-modules.conf \
"

S = "${UNPACKDIR}"

do_install() {
    # Install kernel module load list for camera pipeline. Resolved by FILESPATH:
    # files/${MACHINE}/camera-modules.conf if present, otherwise the generic
    # (empty) fallback in files/.
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0644 ${UNPACKDIR}/camera-modules.conf \
        ${D}${sysconfdir}/modules-load.d/quickboot-camera.conf
}

FILES:${PN} = " \
    ${sysconfdir}/modules-load.d/quickboot-camera.conf \
"

RDEPENDS:${PN} = "udev"

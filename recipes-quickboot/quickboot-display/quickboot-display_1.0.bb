SUMMARY = "Early Display Boot Optimizations"
DESCRIPTION = "Installs kernel module load list for faster \
display driver probes during boot"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://display-modules.conf \
"

S = "${UNPACKDIR}"

do_install() {
    # Install kernel module load list for early display pipeline. Resolved by
    # FILESPATH: files/${MACHINE}/display-modules.conf if present, otherwise the
    # generic (empty) fallback in files/.
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0644 ${UNPACKDIR}/display-modules.conf \
        ${D}${sysconfdir}/modules-load.d/quickboot-display.conf
}

FILES:${PN} = " \
    ${sysconfdir}/modules-load.d/quickboot-display.conf \
"

RDEPENDS:${PN} = "udev"

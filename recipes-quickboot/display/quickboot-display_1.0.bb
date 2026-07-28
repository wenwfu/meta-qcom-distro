SUMMARY = "Early Display Boot Optimizations"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

require ../quickboot-support.inc

SRC_URI = " \
    file://common/03-drm.rules \
    file://${MACHINE}/display-modules.conf \
"

S = "${UNPACKDIR}"

# Gated on the "quickboot" DISTRO_FEATURE: skipped entirely unless the distro
# enables it.
inherit features_check
REQUIRED_DISTRO_FEATURES = "quickboot"

COMPATIBLE_MACHINE = "${@quickboot_compatible_machine(d)}"

# Payload is enabled per machine, so package it per machine.
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile[noexec] = "1"

# The udev rule starts weston when the primary DRM card appears. Files are
# installed straight to /etc so the optimization is active on every boot.
do_install() {
    install -Dm 0644 ${UNPACKDIR}/common/03-drm.rules \
        ${D}${sysconfdir}/udev/rules.d/03-drm.rules
    install -Dm 0644 ${UNPACKDIR}/${MACHINE}/display-modules.conf \
        ${D}${sysconfdir}/modules-load.d/display-modules.conf
}

FILES:${PN} = " \
    ${sysconfdir}/udev/rules.d \
    ${sysconfdir}/modules-load.d \
"

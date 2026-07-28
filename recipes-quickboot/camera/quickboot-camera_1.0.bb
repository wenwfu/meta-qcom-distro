SUMMARY = "Early Camera Boot Optimizations"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

require ../quickboot-support.inc

SRC_URI = " \
    file://common/02-cam-server.rules \
    file://${MACHINE}/camera-modules.conf \
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

# The udev rule starts cam-server when the video0 node appears. Files are
# installed straight to /etc so the optimization is active on every boot.
do_install() {
    install -Dm 0644 ${UNPACKDIR}/common/02-cam-server.rules \
        ${D}${sysconfdir}/udev/rules.d/02-cam-server.rules
    install -Dm 0644 ${UNPACKDIR}/${MACHINE}/camera-modules.conf \
        ${D}${sysconfdir}/modules-load.d/camera-modules.conf
}

FILES:${PN} = " \
    ${sysconfdir}/udev/rules.d \
    ${sysconfdir}/modules-load.d \
"

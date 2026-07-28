SUMMARY = "Early Audio Boot Optimizations"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

require ../quickboot-support.inc

SRC_URI = " \
    file://common/01-pipewire-audio.rules \
    file://${MACHINE}/audio-modules.conf \
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

# The udev rule starts pipewire when the sound control node appears; the module
# list is installed straight to /etc so the optimization is active on every
# boot.
do_install() {
    install -Dm 0644 ${UNPACKDIR}/common/01-pipewire-audio.rules \
        ${D}${sysconfdir}/udev/rules.d/01-pipewire-audio.rules
    install -Dm 0644 ${UNPACKDIR}/${MACHINE}/audio-modules.conf \
        ${D}${sysconfdir}/modules-load.d/audio-modules.conf
}

FILES:${PN} = " \
    ${sysconfdir}/udev/rules.d \
    ${sysconfdir}/modules-load.d \
"

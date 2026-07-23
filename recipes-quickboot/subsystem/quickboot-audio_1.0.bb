SUMMARY = "Early Audio Boot Optimizations"

require ../quickboot-git.inc
require ../quickboot-support.inc

# Gated on the "quickboot" DISTRO_FEATURE: skipped entirely unless the distro
# enables it.
inherit features_check
REQUIRED_DISTRO_FEATURES = "quickboot"

RDEPENDS:${PN} += "bash quickboot-core"

COMPATIBLE_MACHINE = "${@quickboot_compatible_machine(d)}"

# Source slices in the fetched git tree: common (shared) and family payload.
# examples/audio/ (demo apps) is customer reference only and is NOT installed.
QB_COMMON = "${S}/subsystems/audio/files/common"
QB_SRC = "${S}/subsystems/audio/files/${SOC_FAMILY}"

# QuickBoot payload library: activation files are staged here and symlinked into
# /etc at runtime by quickboot-apply on "audio=on".
QB_PROFILE_DIR = "${datadir}/quickboot/profiles/${SOC_FAMILY}/audio"

QB_INSTALL_COMMON () {
    install -d ${D}${QB_PROFILE_DIR}
    install -m 0644 ${QB_COMMON}/pipewire.service             ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_COMMON}/pipewire.socket.conf         ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_COMMON}/pipewire-manager.socket.conf ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_COMMON}/01-pipewire-audio.rules      ${D}${QB_PROFILE_DIR}/
}

# qcs9100 SoC family (SA8775P / lemans; e.g. iq-9075-evk, qcs9100-ride-sx)
do_install:qcs9100() {
    QB_INSTALL_COMMON
    install -m 0644 ${QB_SRC}/manifest           ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/audio-modules.conf ${D}${QB_PROFILE_DIR}/
}

# qcm6490 SoC family (sc7280; e.g. rb3gen2-core-kit / Kodiak, qcm6490-idp)
do_install:qcm6490() {
    QB_INSTALL_COMMON
    install -m 0644 ${QB_SRC}/manifest           ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/audio-modules.conf ${D}${QB_PROFILE_DIR}/
}

FILES:${PN} += "${datadir}/quickboot/profiles"

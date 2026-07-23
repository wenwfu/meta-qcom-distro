SUMMARY = "Early Display Boot Optimizations"

require ../quickboot-git.inc
require ../quickboot-support.inc

# Gated on the "quickboot" DISTRO_FEATURE: skipped entirely unless the distro
# enables it.
inherit features_check
REQUIRED_DISTRO_FEATURES = "quickboot"

RDEPENDS:${PN} += "bash quickboot-core"

COMPATIBLE_MACHINE = "${@quickboot_compatible_machine(d)}"

QB_COMMON = "${S}/subsystems/display/files/common"
QB_SRC = "${S}/subsystems/display/files/${SOC_FAMILY}"

# QuickBoot payload library: activation files are staged here and symlinked into
# /etc at runtime by quickboot-apply on "display=on".
QB_PROFILE_DIR = "${datadir}/quickboot/profiles/${SOC_FAMILY}/display"

# qcs9100 SoC family (SA8775P / lemans; e.g. iq-9075-evk, qcs9100-ride-sx)
QB_INSTALL_COMMON () {
    install -d ${D}${QB_PROFILE_DIR}
    install -m 0644 ${QB_COMMON}/weston.service     ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_COMMON}/03-drm.rules       ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_COMMON}/weston.socket.conf ${D}${QB_PROFILE_DIR}/
}

do_install:qcs9100() {
    QB_INSTALL_COMMON
    install -m 0644 ${QB_SRC}/manifest              ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/drm-modprobe.conf     ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/display-modules.conf  ${D}${QB_PROFILE_DIR}/
}

# qcm6490 SoC family (sc7280; e.g. rb3gen2-core-kit / Kodiak, qcm6490-idp)
do_install:qcm6490() {
    QB_INSTALL_COMMON
    install -m 0644 ${QB_SRC}/manifest                 ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/display-modules.conf     ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/00-msm-softdep.conf      ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/blacklist-bootspeed.conf ${D}${QB_PROFILE_DIR}/
}

FILES:${PN} += "${datadir}/quickboot/profiles"

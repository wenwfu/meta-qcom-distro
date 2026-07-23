SUMMARY = "Early Camera Boot Optimizations"

require ../quickboot-git.inc
require ../quickboot-support.inc

# Gated on the "quickboot" DISTRO_FEATURE: skipped entirely unless the distro
# enables it.
inherit features_check
REQUIRED_DISTRO_FEATURES = "quickboot"

RDEPENDS:${PN} += "bash quickboot-core"

COMPATIBLE_MACHINE = "${@quickboot_compatible_machine(d)}"

# Source slices in the fetched git tree: common (shared) and family payload.
# examples/camera/ (demo apps) is customer reference only and is NOT installed.
QB_COMMON = "${S}/subsystems/camera/files/common"
QB_SRC = "${S}/subsystems/camera/files/${SOC_FAMILY}"

# QuickBoot payload library: activation files (units, udev rules, modules-load.d)
# are staged here and symlinked into /etc at runtime by quickboot-apply when the
# user requests "camera=on". They are NOT installed into /etc at build time.
QB_PROFILE_DIR = "${datadir}/quickboot/profiles/${SOC_FAMILY}/camera"

# qcs9100 SoC family (SA8775P / lemans; e.g. iq-9075-evk, qcs9100-ride-sx)
QB_INSTALL_COMMON () {
    install -d ${D}${QB_PROFILE_DIR}
    install -m 0644 ${QB_COMMON}/cam-server.service  ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_COMMON}/02-cam-server.rules  ${D}${QB_PROFILE_DIR}/
}

do_install:qcs9100() {
    QB_INSTALL_COMMON
    install -m 0644 ${QB_SRC}/manifest                ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/camera-modules.conf     ${D}${QB_PROFILE_DIR}/

    # First-boot setup script: staged into the payload only (0755 so the
    # symlink target stays executable); quickboot-apply links it to
    # /etc/camera/camera-sensor-prune.sh on "camera=on" (see manifest).
    install -m 0755 ${QB_SRC}/camera-sensor-prune.sh ${D}${QB_PROFILE_DIR}/
}

# qcm6490 SoC family (sc7280; e.g. rb3gen2-core-kit / Kodiak, qcm6490-idp)
do_install:qcm6490() {
    QB_INSTALL_COMMON
    install -m 0644 ${QB_SRC}/manifest                ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/camera-modules.conf     ${D}${QB_PROFILE_DIR}/

    # CamX override: staged into the payload only; quickboot-apply links it to
    # /etc/camera/camxoverridesettings.txt on "camera=on" (see manifest).
    install -m 0644 ${QB_SRC}/camxoverridesettings.txt ${D}${QB_PROFILE_DIR}/
}

FILES:${PN} += "${datadir}/quickboot/profiles"

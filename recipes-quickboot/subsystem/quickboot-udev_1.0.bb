SUMMARY = "Early udev Coldplug Boot Optimizations"

require ../quickboot-git.inc
require ../quickboot-support.inc

# Gated on the "quickboot" DISTRO_FEATURE: skipped entirely unless the distro
# enables it.
inherit features_check
REQUIRED_DISTRO_FEATURES = "quickboot"

RDEPENDS:${PN} += "quickboot-core"

# 55-qcom-firmware-noblkid.rules assumes only sda holds filesystems and
# sdb..sdz are firmware disks with nothing mounted -- verified on Kodiak;
# re-verify the UFS partition layout on iq-9075 (SA8775P) before relying on it.
COMPATIBLE_MACHINE = "${@quickboot_compatible_machine(d)}"

# All payload is shared across SoC families (files/common).
QB_SRC = "${S}/subsystems/udev/files/common"

# QuickBoot payload library: activation files are staged here and symlinked into
# /etc at runtime by quickboot-apply on "udev=on".
QB_PROFILE_DIR = "${datadir}/quickboot/profiles/${SOC_FAMILY}/udev"

do_install() {
    install -d ${D}${QB_PROFILE_DIR}
    install -m 0644 ${QB_SRC}/manifest                            ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/early-trigger.conf                  ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/systemd-udev-trigger-deferred.service ${D}${QB_PROFILE_DIR}/
    install -m 0644 ${QB_SRC}/55-qcom-firmware-noblkid.rules      ${D}${QB_PROFILE_DIR}/
}

FILES:${PN} += "${datadir}/quickboot/profiles"

SUMMARY = "QuickBoot runtime optimization core"
DESCRIPTION = "Detects the running board's SoC family, watches \
/etc/quickboot/request, and activates or reverts per-subsystem \
(camera/audio/display/udev) boot optimizations at runtime by symlinking payload \
from /usr/share/quickboot/profiles/<soc-family>/<subsystem>/ into the live /etc \
tree."

require ../quickboot-git.inc
require ../quickboot-support.inc

# Gated on the "quickboot" DISTRO_FEATURE: skipped entirely unless the distro
# enables it.
inherit systemd features_check
REQUIRED_DISTRO_FEATURES = "quickboot"

RDEPENDS:${PN} += "bash"

COMPATIBLE_MACHINE = "${@quickboot_compatible_machine(d)}"

# Records the build-time SOC_FAMILY into the rootfs, so this package's content
# is machine-specific.
PACKAGE_ARCH = "${MACHINE_ARCH}"

QB_SRC = "${S}/core/files"

# The .path watcher is what we enable at boot; it starts the oneshot .service
# on request-node changes.
SYSTEMD_SERVICE:${PN} = "quickboot-core.path"
SYSTEMD_AUTO_ENABLE = "enable"

python () {
    if not d.getVar("SOC_FAMILY"):
        bb.fatal("SOC_FAMILY is not set for MACHINE '%s'; quickboot profiles are "
                 "keyed by SoC family. Ensure the machine includes a "
                 "conf/machine/include/qcom-*.inc that sets SOC_FAMILY." % d.getVar("MACHINE"))
}

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${QB_SRC}/quickboot-apply ${D}${bindir}/

    install -d ${D}${systemd_system_unitdir}/
    install -m 0644 ${QB_SRC}/quickboot-core.path ${D}${systemd_system_unitdir}/
    install -m 0644 ${QB_SRC}/quickboot-core.service ${D}${systemd_system_unitdir}/

    # Control node + state dir live under /etc (writable on ostree). The SoC
    # family is fixed at build time from ${SOC_FAMILY}; quickboot-apply reads it
    # from /etc/quickboot/soc-family at runtime to pick the profile directory.
    install -d ${D}${sysconfdir}/quickboot/active/
    echo "${SOC_FAMILY}" > ${D}${sysconfdir}/quickboot/soc-family

    # Pre-seed the control node with every subsystem off. Users flip values to
    # "on" to enable. It is a conffile so package upgrades keep local edits.
    install -m 0644 ${QB_SRC}/request ${D}${sysconfdir}/quickboot/request
}

# Preserve the user's edited request across upgrades.
CONFFILES:${PN} = "${sysconfdir}/quickboot/request"

FILES:${PN} += " \
    ${bindir}/* \
    ${systemd_system_unitdir}/* \
    ${sysconfdir}/quickboot \
"

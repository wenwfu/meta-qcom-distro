SUMMARY = "QuickBoot runtime optimization core"
DESCRIPTION = "Installs the QuickBoot runtime service and control files used \
to apply or revert boot-time optimization profiles."

require ../quickboot-git.inc

inherit systemd features_check

REQUIRED_DISTRO_FEATURES = "quickboot"

RDEPENDS:${PN} += "bash"

COMPATIBLE_MACHINE = "(qcm6490|qcs9100)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

QB_SRC = "${S}/core/files"

SYSTEMD_SERVICE:${PN} = "quickboot-core.path"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

python () {
    if not d.getVar("SOC_FAMILY"):
        bb.fatal("SOC_FAMILY is not set for MACHINE '%s'" % d.getVar("MACHINE"))
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${QB_SRC}/quickboot-apply ${D}${bindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${QB_SRC}/quickboot-core.path ${D}${systemd_system_unitdir}/
    install -m 0644 ${QB_SRC}/quickboot-core.service ${D}${systemd_system_unitdir}/

    install -d ${D}${sysconfdir}/quickboot/active
    echo "${SOC_FAMILY}" > ${D}${sysconfdir}/quickboot/soc-family
    install -m 0644 ${QB_SRC}/request ${D}${sysconfdir}/quickboot/request
}

CONFFILES:${PN} = "${sysconfdir}/quickboot/request"

FILES:${PN} += " \
    ${bindir}/quickboot-apply \
    ${systemd_system_unitdir}/quickboot-core.path \
    ${systemd_system_unitdir}/quickboot-core.service \
    ${sysconfdir}/quickboot \
"

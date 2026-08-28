SUMMARY = "Systemd drop-in service fragment to resize root filesystem at first boot"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI += "\
    file://10-growfs-root.conf \
    file://growfs-root.preset \
"
SRC_URI:append:qcom-distro:sota = " file://20-sota-growfs-root.conf"

inherit allarch features_check systemd
REQUIRED_DISTRO_FEATURES = "systemd"

S = "${UNPACKDIR}"

do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/growfs-root.preset \
            ${D}${systemd_unitdir}/system-preset/98-growfs-root.preset
    install -Dm 0644 ${UNPACKDIR}/10-growfs-root.conf \
            ${D}${systemd_unitdir}/system/systemd-growfs-root.service.d/10-growfs-root.conf
}

# On qcom-distro SOTA (ostree) images "/" is not a growable filesystem;
# override the service via a higher-priority drop-in to grow /sysroot instead.
do_install:append:qcom-distro:sota() {
    install -Dm 0644 ${UNPACKDIR}/20-sota-growfs-root.conf \
            ${D}${systemd_unitdir}/system/systemd-growfs-root.service.d/20-sota-growfs-root.conf
}

PACKAGES = "${PN}"

FILES:${PN} += "${systemd_unitdir}"

RDEPENDS:${PN} += "e2fsprogs-resize2fs"

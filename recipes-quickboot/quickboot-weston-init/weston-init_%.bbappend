FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:qcom-distro = " \
    file://0001-quickboot-start-weston-from-drm-device.patch \
    file://03-quickboot-weston.rules \
"

# Weston is activated by the primary DRM device udev event.
SYSTEMD_AUTO_ENABLE:${PN}:qcom-distro = "disable"

do_install:append:qcom-distro() {
    install -D -m 0644 ${UNPACKDIR}/03-quickboot-weston.rules \
        ${D}${nonarch_base_libdir}/udev/rules.d/03-quickboot-weston.rules
}

FILES:${PN}:append:qcom-distro = " \
    ${nonarch_base_libdir}/udev/rules.d/03-quickboot-weston.rules \
"

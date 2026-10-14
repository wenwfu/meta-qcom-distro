SUMMARY = "QuickBoot CAMSS camera readiness test application"
DESCRIPTION = "Installs an open-source CAMSS camera preview application and an opt-in systemd service for QuickBoot camera and display readiness validation."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = " \
    file://quickboot-camera-preview-camss \
    file://quickboot-camera-preview-camss.service \
"

S = "${UNPACKDIR}"

inherit systemd features_check

# This application targets camera-enabled ARMv8 platforms. Keep it out of the
# non-ARM layer-check machines.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

REQUIRED_DISTRO_FEATURES = "wayland"

RDEPENDS:${PN} += " \
    gstreamer1.0 \
    gstreamer1.0-plugins-bad-waylandsink \
    libcamera-gst \
    weston \
"

SYSTEMD_SERVICE:${PN} = "quickboot-camera-preview-camss.service"
# Ship the test application in the image, but leave it opt-in at runtime.
SYSTEMD_AUTO_ENABLE = "disable"

do_install() {
    install -D -m 0755 ${S}/quickboot-camera-preview-camss \
        ${D}${bindir}/quickboot-camera-preview-camss
    install -D -m 0644 ${S}/quickboot-camera-preview-camss.service \
        ${D}${systemd_system_unitdir}/quickboot-camera-preview-camss.service
}

SUMMARY = "Qualcomm QuickBoot validation workload package group"
DESCRIPTION = "Package group containing QuickBoot workloads used to validate audio and camera media readiness."

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} = " \
    quickboot-audio-readiness \
"

# The camera workloads are available only on ARMv8 platforms.
RDEPENDS:${PN}:append:aarch64 = " \
    quickboot-camera-preview-camx \
    quickboot-camera-preview-libcamera \
"

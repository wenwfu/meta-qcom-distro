require qcom-multimedia-image.bb

SUMMARY = "An image built on top of multimedia image for proprietary features"

# This image is compatible only with aarch64 (ARMv8)
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

CORE_IMAGE_BASE_INSTALL += " \
    camera-service \
    camx-dlkm \
    camx-hamoa \
    camx-kodiak \
    camx-lemans \
    camx-nhx \
    camx-talos \
    gst-plugins-imsdk-prop \
    iris-video-dlkm \
    kgsl-dlkm \
    libdiag-bin \
    qcom-adreno \
    qcom-sensors-binaries \
    qwes \
"
CORE_IMAGE_BASE_INSTALL:append = " \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'meta-audioreach', ' packagegroup-audioreach', '', d)} \
"

TOOLCHAIN_HOST_TASK:append = " nativesdk-protobuf-camx-compiler"

require ../../recipes-quickboot/quickboot-support.inc

# QuickBoot early-boot optimizations, applied to the proprietary image only.
# DISTRO_FEATURES gates the feature globally, while QUICKBOOT_MACHINES keeps
# unsupported machines from pulling recipes that COMPATIBLE_MACHINE will skip
# during parsing. QUICKBOOT_SUBSYSTEMS controls which payload packages are added.
PACKAGE_INSTALL_ATTEMPTONLY:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'quickboot', \
    quickboot_package_list(d), '', d)}"

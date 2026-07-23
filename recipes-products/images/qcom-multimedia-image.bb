require qcom-console-image.bb

SUMMARY = "Basic Wayland image with Weston"

IMAGE_FEATURES += "weston"

CORE_IMAGE_BASE_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'weston-xwayland xterm', '', d)} \
    alsa-utils-alsatplg \
    alsa-utils-alsaucm \
    alsa-utils-aplay \
    ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'docker-compose', '', d)} \
    gstd \
    gstreamer1.0 \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-python \
    libcamera \
    libcamera-gst \
    libdrm-tests \
    ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'packagegroup-container', '', d)} \
    packagegroup-qcom-benchmark \
    packagegroup-qcom-test-pkgs \
    packagegroup-qcom-utilities-gpu-utils \
    pipewire \
    pipewire-alsa \
    pipewire-modules-meta \
    pipewire-pulse \
    pipewire-spa-tools \
    pipewire-tools \
    pulseaudio-pactl \
    tensorflow-lite-tools \
    thermald \
    userspace-resource-manager \
    userspace-resource-manager-extensions \
    weston \
    weston-examples \
    weston-init \
    wireplumber \
"

# IMSDK currently only used and tested on ARMv8 (aarch64) machines.
CORE_IMAGE_BASE_INSTALL:append:aarch64 = " gst-plugins-imsdk-oss"

require ../../recipes-quickboot/quickboot-support.inc

# QuickBoot early-boot optimizations. DISTRO_FEATURES gates the feature
# globally, while QUICKBOOT_SOC_FAMILIES keeps unsupported SoC families from
# pulling recipes that COMPATIBLE_MACHINE will skip during parsing.
# QUICKBOOT_SUBSYSTEMS controls optional payload packages; core is pulled in
# automatically when at least one subsystem is enabled.
PACKAGE_INSTALL_ATTEMPTONLY:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'quickboot', \
    quickboot_package_list(d), '', d)}"

# let's make sure we have a good image.
REQUIRED_DISTRO_FEATURES += "wayland"

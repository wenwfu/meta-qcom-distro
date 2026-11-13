SYSTEMD_BOOT_TIMEOUT ?= ""

setup_efi_folder:append:qcom-distro() {
    if [ -n "${SYSTEMD_BOOT_TIMEOUT}" ]; then
        install -d ${IMAGE_ROOTFS}/loader
        printf 'timeout %s\n' "${SYSTEMD_BOOT_TIMEOUT}" > ${IMAGE_ROOTFS}/loader/loader.conf
    fi
}

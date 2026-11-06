SUMMARY = "QUP firmware for the current machine's initramfs"
LICENSE = "MIT"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

inherit packagegroup

# SOC_FAMILY is in MACHINEOVERRIDES, so boards sharing a SoC use one entry.
# Add an entry here when supporting another SoC that needs QUP firmware.
RDEPENDS:${PN} = ""
RDEPENDS:${PN}:hamoa = "linux-firmware-qcom-x1e80100-qupv3fw"
RDEPENDS:${PN}:qcm6490 = "linux-firmware-qcom-qcm6490-qupv3fw"
RDEPENDS:${PN}:qcs615 = "linux-firmware-qcom-qcs615-qupv3fw"
RDEPENDS:${PN}:qcs8300 = "linux-firmware-qcom-qcs8300-qupv3fw"
RDEPENDS:${PN}:qcs9100 = "linux-firmware-qcom-sa8775p-qupv3fw"
RDEPENDS:${PN}:shikra = "linux-firmware-qcom-shikra-qupv3fw"

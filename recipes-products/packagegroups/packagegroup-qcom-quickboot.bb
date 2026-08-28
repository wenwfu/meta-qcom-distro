SUMMARY = "Qualcomm QuickBoot packagegroup"
DESCRIPTION = "Package group for supported early-boot optimizations"

require ../../recipes-quickboot/quickboot-support.inc

# The dependency list varies with MACHINE.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = "${@qcom_quickboot_package_list(d)}"

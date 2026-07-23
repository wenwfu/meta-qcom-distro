# Single source of truth for QuickBoot SoC and subsystem support.
QUICKBOOT_SOC_FAMILIES = "qcm6490 qcs9100"

# Optional QuickBoot payloads to install. Valid values are:
# camera display audio udev
#
# All subsystems are enabled by default. Override this in local.conf, distro, or
# machine config to reduce the installed payload, for example:
#   QUICKBOOT_SUBSYSTEMS = "camera display"
#   QUICKBOOT_SUBSYSTEMS:remove = "udev"
#
# quickboot-core is not listed here; it is added automatically whenever at
# least one subsystem is enabled.
QUICKBOOT_SUBSYSTEMS ?= "camera display audio udev"

def quickboot_soc_supported(d):
    soc_family = d.getVar("SOC_FAMILY") or ""
    supported = (d.getVar("QUICKBOOT_SOC_FAMILIES") or "").split()
    return soc_family in supported

def quickboot_compatible_machine(d):
    families = (d.getVar("QUICKBOOT_SOC_FAMILIES") or "").split()
    return "(" + "|".join(families) + ")" if families else "^$"

def quickboot_package_list(d):
    if not quickboot_soc_supported(d):
        return ""

    enabled = (d.getVar("QUICKBOOT_SUBSYSTEMS") or "").split()
    subsystem_pkgs = {
        "audio": "quickboot-audio",
        "camera": "quickboot-camera",
        "display": "quickboot-display",
        "udev": "quickboot-udev",
    }

    packages = [pkg for name, pkg in subsystem_pkgs.items() if name in enabled]
    if packages:
        packages.insert(0, "quickboot-core")

    return " ".join(packages)

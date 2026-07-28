# Single source of truth for QuickBoot machine and subsystem support.
QUICKBOOT_MACHINES = " \
    iq-9075-evk \
"

# QuickBoot payload packages to install. Valid values are:
# camera display audio
#
# All subsystems are enabled by default. Override this in local.conf, distro, or
# machine config to reduce the installed payload, for example:
#   QUICKBOOT_SUBSYSTEMS = "camera display"
#
# Each subsystem package installs its optimization files straight to their final
# /etc target paths, so the optimizations are baked into the image (active on
# every boot). There is no runtime enable/disable switch.
QUICKBOOT_SUBSYSTEMS ?= "camera display audio"

def quickboot_supported_machines(d):
    return (d.getVar("QUICKBOOT_MACHINES") or "").split()

def quickboot_machine_supported(d):
    machine = d.getVar("MACHINE") or ""
    return machine in quickboot_supported_machines(d)

def quickboot_compatible_machine(d):
    import re

    machines = quickboot_supported_machines(d)
    return "(^|:)(" + "|".join(re.escape(machine) for machine in machines) + ")(:|$)" if machines else "^$"

def quickboot_package_list(d):
    if not quickboot_machine_supported(d):
        return ""

    enabled = (d.getVar("QUICKBOOT_SUBSYSTEMS") or "").split()
    subsystem_pkgs = {
        "audio": "quickboot-audio",
        "camera": "quickboot-camera",
        "display": "quickboot-display",
    }

    packages = [pkg for name, pkg in subsystem_pkgs.items() if name in enabled]

    return " ".join(packages)

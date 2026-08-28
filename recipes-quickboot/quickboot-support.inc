# Shared QuickBoot machine and subsystem selection.
#
# To support another machine, add it to QCOM_QUICKBOOT_SUPPORTED_MACHINES and
# provide the corresponding <subsystem>-modules-<machine>.conf files.
QCOM_QUICKBOOT_SUPPORTED_MACHINES ?= " \
    iq-9075-evk \
"

QCOM_QUICKBOOT_AVAILABLE_SUBSYSTEMS = "audio camera display"

# Users can override this in their configuration to select a subset.
QCOM_QUICKBOOT_SUBSYSTEMS ??= "${QCOM_QUICKBOOT_AVAILABLE_SUBSYSTEMS}"

def qcom_quickboot_machine_supported(d):
    machine = d.getVar("MACHINE") or ""
    supported = (d.getVar("QCOM_QUICKBOOT_SUPPORTED_MACHINES") or "").split()
    return machine in supported

def qcom_quickboot_compatible_machine(d):
    import re

    supported = (d.getVar("QCOM_QUICKBOOT_SUPPORTED_MACHINES") or "").split()
    if not supported:
        return "^$"

    return "^(%s)$" % "|".join(re.escape(machine) for machine in supported)

def qcom_quickboot_package_list(d):
    if not qcom_quickboot_machine_supported(d):
        return ""

    available = (d.getVar("QCOM_QUICKBOOT_AVAILABLE_SUBSYSTEMS") or "").split()
    selected = (d.getVar("QCOM_QUICKBOOT_SUBSYSTEMS") or "").split()
    unknown = sorted(set(selected) - set(available))

    if unknown:
        bb.fatal("Unknown QuickBoot subsystem(s): %s. Available subsystems: %s" %
                 (" ".join(unknown), " ".join(available)))

    return " ".join("quickboot-%s" % subsystem for subsystem in selected)

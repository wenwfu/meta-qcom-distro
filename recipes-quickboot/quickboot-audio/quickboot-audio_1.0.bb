SUMMARY = "Early Audio Boot Optimizations"
DESCRIPTION = "Installs kernel module load list for faster audio \
driver probes during boot"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://audio-modules.conf \
"

# audioreach_driver is an out-of-tree kernel module provided exclusively by the
# meta-audioreach layer (recipes-kernel/audioreach-kernel). It is NOT part of
# the upstream kernel and will not exist on systems built without meta-audioreach.
#
# bb.utils.contains() checks at parse time whether 'meta-audioreach' appears in
# BBFILE_COLLECTIONS (the space-separated list of all active layer names).
#
#   - If meta-audioreach IS present  → AUDIOREACH_MODULE = "audioreach_driver"
#   - If meta-audioreach is NOT present → AUDIOREACH_MODULE = "" (empty)
AUDIOREACH_MODULE = "${@bb.utils.contains('BBFILE_COLLECTIONS', 'meta-audioreach', 'audioreach_driver', '', d)}"

S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0644 ${UNPACKDIR}/audio-modules.conf \
        ${D}${sysconfdir}/modules-load.d/quickboot-audio.conf

    # audioreach_driver is an out-of-tree module from meta-audioreach.
    # AUDIOREACH_MODULE is set to 'audioreach_driver' at parse time if
    # meta-audioreach is in BBFILE_COLLECTIONS, otherwise it is empty.
    # This appends it to the module load list only when the layer is present.
    if [ -n "${AUDIOREACH_MODULE}" ]; then
        echo "${AUDIOREACH_MODULE}" >> \
            ${D}${sysconfdir}/modules-load.d/quickboot-audio.conf
    fi
}

FILES:${PN} = " \
    ${sysconfdir}/modules-load.d/quickboot-audio.conf \
"

RDEPENDS:${PN} = "udev"

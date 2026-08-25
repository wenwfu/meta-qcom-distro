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
    # Install kernel module load list for audio pipeline. Resolved by FILESPATH:
    # files/${MACHINE}/audio-modules.conf if present, otherwise the generic
    # (empty) fallback in files/.
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0644 ${UNPACKDIR}/audio-modules.conf \
        ${D}${sysconfdir}/modules-load.d/quickboot-audio.conf

    # audioreach_driver is an out-of-tree module from meta-audioreach.
    # AUDIOREACH_MODULE is 'audioreach_driver' when meta-audioreach is in
    # BBFILE_COLLECTIONS at parse time, otherwise empty.
    if [ -n "${AUDIOREACH_MODULE}" ]; then
        echo "${AUDIOREACH_MODULE}" >> \
            ${D}${sysconfdir}/modules-load.d/quickboot-audio.conf
    fi
}

ALLOW_EMPTY:${PN} = "1"

FILES:${PN} = " \
    ${sysconfdir}/modules-load.d/quickboot-audio.conf \
"

RDEPENDS:${PN} = "udev"

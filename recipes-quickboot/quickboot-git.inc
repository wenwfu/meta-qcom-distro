# Common git source definition for the QuickBoot recipes.
#
# The QuickBoot sources live in one GitHub repo; each recipe fetches the whole
# tree at a pinned SRCREV and installs its own slice from ${S}. To bump the
# version, update SRCREV (and PV) here in one place.
#
# NOTE: this deploys recipes into meta-qcom-distro (an upstream Qualcomm layer);
# these files are a local addition and are not tracked by that layer's upstream.

HOMEPAGE = "https://github.com/wenwfu/quickboot"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

# Pinned revision on branch quickboot-runtime-manager. Bump to move forward.
SRC_URI = "git://github.com/wenwfu/quickboot.git;protocol=https;branch=quickboot-runtime-manager"
SRCREV = "a589e7a433ec3dadfba292f0e28b667f5191aa67"

PV = "1.0+git${SRCPV}"

# S is left at its bitbake.conf default (${UNPACKDIR}/${BP}). This OE release
# sets BB_GIT_DEFAULT_DESTSUFFIX = "${BP}", so the git checkout unpacks straight
# to ${S}; recipes reach ${S}/subsystems/<subsys>/files/... and ${S}/core/
# files/... . Do NOT set S = "${UNPACKDIR}/git" -- do_qa_unpack fatals on it.

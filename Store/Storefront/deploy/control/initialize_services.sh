#!/bin/sh
#
# Copyright (C) 1994, 2018, Oracle and/or its affiliates. All rights reserved.
# Oracle and Java are registered trademarks of Oracle and/or its
# affiliates. Other names may be trademarks of their respective owners.
# UNIX is a registered trademark of The Open Group.
#
# This software and related documentation are provided under a license
# agreement containing restrictions on use and disclosure and are
# protected by intellectual property laws. Except as expressly permitted
# in your license agreement or allowed by law, you may not use, copy,
# reproduce, translate, broadcast, modify, license, transmit, distribute,
# exhibit, perform, publish, or display any part, in any form, or by any
# means. Reverse engineering, disassembly, or decompilation of this
# software, unless required by law for interoperability, is prohibited.
# The information contained herein is subject to change without notice
# and is not warranted to be error-free. If you find any errors, please
# report them to us in writing.
# U.S. GOVERNMENT END USERS: Oracle programs, including any operating
# system, integrated software, any programs installed on the hardware,
# and/or documentation, delivered to U.S. Government end users are
# "commercial computer software" pursuant to the applicable Federal
# Acquisition Regulation and agency-specific supplemental regulations.
# As such, use, duplication, disclosure, modification, and adaptation
# of the programs, including any operating system, integrated software,
# any programs installed on the hardware, and/or documentation, shall be
# subject to license terms and license restrictions applicable to the
# programs. No other rights are granted to the U.S. Government.
# This software or hardware is developed for general use in a variety
# of information management applications. It is not developed or
# intended for use in any inherently dangerous applications, including
# applications that may create a risk of personal injury. If you use
# this software or hardware in dangerous applications, then you shall
# be responsible to take all appropriate fail-safe, backup, redundancy,
# and other measures to ensure its safe use. Oracle Corporation and its
# affiliates disclaim any liability for any damages caused by use of this
# software or hardware in dangerous applications.
# This software or hardware and documentation may provide access to or
# information on content, products, and services from third parties.
# Oracle Corporation and its affiliates are not responsible for and
# expressly disclaim all warranties of any kind with respect to
# third-party content, products, and services. Oracle Corporation and
# its affiliates will not be responsible for any loss, costs, or damages
# incurred due to your access to or use of third-party content, products,
# or services.

DATA_RS_NAME=@@PROJECT_NAME@@-data
DIMVALS_RS_NAME=@@PROJECT_NAME@@-dimvals
LAST_MILE_CRAWL_NAME=@@PROJECT_NAME@@-last-mile-crawl
DVAL_ID_MGR_NAME=@@PROJECT_NAME@@-dimension-value-id-manager
CAS_ROOT=@@CAS_ROOT@@
CAS_HOST=localhost
CAS_PORT=@@CAS_PORT@@
WORKING_DIR=`dirname ${0} 2>/dev/null`
. "${WORKING_DIR}/../config/script/set_environment.sh"

if [ ! -d "$CAS_ROOT" ] ; then
    echo "No CAS install folder found at $CAS_ROOT. Please install CAS."
    exit 1
fi


# Remove existing application state

if [ "$1" = "--force" ]; then
  echo "Removing existing crawl configuration for crawl $LAST_MILE_CRAWL_NAME (ignore errors if crawl doesn't exist)"
  ${CAS_ROOT}/bin/cas-cmd.sh deleteCrawl -h ${CAS_HOST} -p ${CAS_PORT} -id ${LAST_MILE_CRAWL_NAME}

  echo "Removing Record Store $DATA_RS_NAME (ignore errors if Record Store doesn't exist)"
  ${CAS_ROOT}/bin/component-manager-cmd.sh delete-component -h ${CAS_HOST} -p ${CAS_PORT} -n ${DATA_RS_NAME}

  echo "Removing Record Store $DIMVALS_RS_NAME (ignore errors if Record Store doesn't exist)" 
  ${CAS_ROOT}/bin/component-manager-cmd.sh delete-component -h ${CAS_HOST} -p ${CAS_PORT} -n ${DIMVALS_RS_NAME}

  echo "Removing Dimension Value Id Manager $DVAL_ID_MGR_NAME (ignore errors if Dimension Value Id Manager doesn't exist)"
  ${CAS_ROOT}/bin/cas-cmd.sh deleteDimensionValueIdManager -h ${CAS_HOST} -p ${CAS_PORT} -m ${DVAL_ID_MGR_NAME}
  
  echo "Removing existing application provisioning..."
  "${WORKING_DIR}/runcommand.sh" --remove-app
fi

"${WORKING_DIR}/runcommand.sh" --skip-definition AssertNotDefined
if [ $? != 0 ]; then
  exit 1
fi



# Create new application

echo "Creating Dimension Value Id Manager $DVAL_ID_MGR_NAME"
${CAS_ROOT}/bin/cas-cmd.sh createDimensionValueIdManager -h ${CAS_HOST} -p ${CAS_PORT} -m ${DVAL_ID_MGR_NAME}
if [ $? != 0 ] ; then
	echo "Failure to create Dimension Value Id Manager."
	exit 1
fi

echo "Creating Record Store $DATA_RS_NAME"
${CAS_ROOT}/bin/component-manager-cmd.sh create-component -h ${CAS_HOST} -p ${CAS_PORT} -t RecordStore -n ${DATA_RS_NAME}
if [ $? != 0 ] ; then
	echo "Failure to create Record Store."
	exit 1
fi

# Set the record store configuration
${CAS_ROOT}/bin/recordstore-cmd.sh set-configuration -h ${CAS_HOST} -p ${CAS_PORT} -a ${DATA_RS_NAME} -f ${WORKING_DIR}/../config/cas/data-recordstore-config.xml
if [ $? != 0 ] ; then
	echo "Failure to set Record Store configuration."
	exit 1
fi

echo "Creating Record Store $DIMVALS_RS_NAME"
${CAS_ROOT}/bin/component-manager-cmd.sh create-component -h ${CAS_HOST} -p ${CAS_PORT} -t RecordStore -n ${DIMVALS_RS_NAME}
if [ $? != 0 ] ; then
	echo "Failure to create Record Store."
	exit 1
fi

echo "Setting EAC provisioning and performing initial setup..."
"${WORKING_DIR}/runcommand.sh" InitialSetup

if [ $? != 0 ] ; then
	echo "Failure to initialize EAC application."
	exit 1
fi

echo "Finished updating EAC."

# Create the crawl after creating the Dimension Value Id Manager, Record Stores, and IFCR Site
echo "Creating crawl ${LAST_MILE_CRAWL_NAME}"
${CAS_ROOT}/bin/cas-cmd.sh createCrawls -h ${CAS_HOST} -p ${CAS_PORT} -f ${WORKING_DIR}/../config/cas/last-mile-crawl.xml
if [ $? != 0 ] ; then
	echo "Failure to create crawl."
	exit 1
fi

# Remove the following step if not using sample application and data.

echo "Importing content..."

# import content using public format
"${WORKING_DIR}/runcommand.sh" IFCR importApplication "${WORKING_DIR}/../config/import"
if [ $? != 0 ]; then
	echo "Failed to import content in public format."
    exit 1
fi

# import content using legacy format
"${WORKING_DIR}/runcommand.sh" IFCR legacyUpdateContent "/" "${WORKING_DIR}/../config/ifcr"
if [ $? != 0 ] ; then
	echo "Failed to import content in legacy format."
	exit 1
fi

echo "Finished importing content"


@echo off
rem
rem Copyright (C) 1994, 2018, Oracle and/or its affiliates. All rights reserved.
rem Oracle and Java are registered trademarks of Oracle and/or its
rem affiliates. Other names may be trademarks of their respective owners.
rem UNIX is a registered trademark of The Open Group.
rem
rem This software and related documentation are provided under a license
rem agreement containing restrictions on use and disclosure and are
rem protected by intellectual property laws. Except as expressly permitted
rem in your license agreement or allowed by law, you may not use, copy,
rem reproduce, translate, broadcast, modify, license, transmit, distribute,
rem exhibit, perform, publish, or display any part, in any form, or by any
rem means. Reverse engineering, disassembly, or decompilation of this
rem software, unless required by law for interoperability, is prohibited.
rem The information contained herein is subject to change without notice
rem and is not warranted to be error-free. If you find any errors, please
rem report them to us in writing.
rem U.S. GOVERNMENT END USERS: Oracle programs, including any operating
rem system, integrated software, any programs installed on the hardware,
rem and/or documentation, delivered to U.S. Government end users are
rem "commercial computer software" pursuant to the applicable Federal
rem Acquisition Regulation and agency-specific supplemental regulations.
rem As such, use, duplication, disclosure, modification, and adaptation
rem of the programs, including any operating system, integrated software,
rem any programs installed on the hardware, and/or documentation, shall be
rem subject to license terms and license restrictions applicable to the
rem programs. No other rights are granted to the U.S. Government.
rem This software or hardware is developed for general use in a variety
rem of information management applications. It is not developed or
rem intended for use in any inherently dangerous applications, including
rem applications that may create a risk of personal injury. If you use
rem this software or hardware in dangerous applications, then you shall
rem be responsible to take all appropriate fail-safe, backup, redundancy,
rem and other measures to ensure its safe use. Oracle Corporation and its
rem affiliates disclaim any liability for any damages caused by use of this
rem software or hardware in dangerous applications.
rem This software or hardware and documentation may provide access to or
rem information on content, products, and services from third parties.
rem Oracle Corporation and its affiliates are not responsible for and
rem expressly disclaim all warranties of any kind with respect to
rem third-party content, products, and services. Oracle Corporation and
rem its affiliates will not be responsible for any loss, costs, or damages
rem incurred due to your access to or use of third-party content, products,
rem or services.

setlocal
set DATA_RS_NAME=@@PROJECT_NAME@@-data
set DIMVALS_RS_NAME=@@PROJECT_NAME@@-dimvals
set LAST_MILE_CRAWL_NAME=@@PROJECT_NAME@@-last-mile-crawl
set DVAL_ID_MGR_NAME=@@PROJECT_NAME@@-dimension-value-id-manager
set CAS_ROOT=@@CAS_ROOT@@
set CAS_HOST=localhost
set CAS_PORT=@@CAS_PORT@@

if NOT EXIST %CAS_ROOT% GOTO NOCASROOT
echo Using CAS install at %CAS_ROOT%

call %~dp0..\config\script\set_environment.bat

REM Clean up existing app state

if "%1" == "--force" (
  echo Removing existing crawl configuration for crawl %LAST_MILE_CRAWL_NAME% (ignore errors if crawl doesn't exist)
  call %CAS_ROOT%\bin\cas-cmd.bat deleteCrawl -h %CAS_HOST% -p %CAS_PORT% -id %LAST_MILE_CRAWL_NAME%

  echo Removing Record Store %DATA_RS_NAME% (ignore errors if Record Store doesn't exist)
  call %CAS_ROOT%\bin\component-manager-cmd.bat delete-component -h %CAS_HOST% -p %CAS_PORT% -n %DATA_RS_NAME%

  echo Removing Record Store %DIMVALS_RS_NAME% (ignore errors if Record Store doesn't exist)
  call %CAS_ROOT%\bin\component-manager-cmd.bat delete-component -h %CAS_HOST% -p %CAS_PORT% -n %DIMVALS_RS_NAME%

  echo Removing Dimension Value Id Manager %DVAL_ID_MGR_NAME% (ignore errors if Dimension Value Id Manager doesn't exist)
  call %CAS_ROOT%\bin\cas-cmd.bat deleteDimensionValueIdManager -h %CAS_HOST% -p %CAS_PORT% -m %DVAL_ID_MGR_NAME%
  
  echo Removing existing application provisioning...
  call %~dp0runcommand.bat --remove-app
) 

call %~dp0runcommand.bat --skip-definition AssertNotDefined
if not %ERRORLEVEL%==0 (
	exit /B 1
)

REM Create new application

echo Creating Dimension Value Id Manager %DVAL_ID_MGR_NAME% 
call %CAS_ROOT%\bin\cas-cmd.bat createDimensionValueIdManager -h %CAS_HOST% -p %CAS_PORT% -m %DVAL_ID_MGR_NAME%
if not %ERRORLEVEL%==0 (
	echo Failure to create Dimension Value Id Manager.
	exit /B 1
)

echo Creating Record Store %DATA_RS_NAME%
call %CAS_ROOT%\bin\component-manager-cmd.bat create-component -h %CAS_HOST% -p %CAS_PORT% -t RecordStore -n %DATA_RS_NAME%
if not %ERRORLEVEL%==0 (
	echo Failure to create Record Store.
	exit /B 1
)

REM Set the record store configuration
call %CAS_ROOT%\bin\recordstore-cmd.bat set-configuration -h %CAS_HOST% -p %CAS_PORT% -a %DATA_RS_NAME% -f %~dp0..\config\cas\data-recordstore-config.xml
if not %ERRORLEVEL%==0 (
	echo Failure to set Record Store configuration.
	exit /B 1
)

echo Creating Record Store %DIMVALS_RS_NAME%
call %CAS_ROOT%\bin\component-manager-cmd.bat create-component -h %CAS_HOST% -p %CAS_PORT% -t RecordStore -n %DIMVALS_RS_NAME%
if not %ERRORLEVEL%==0 (
	echo Failure to create Record Store.
	exit /B 1
)

echo Setting EAC provisioning and performing initial setup...
call %~dp0runcommand.bat InitialSetup
if not %ERRORLEVEL%==0 (
	echo Failure to initialize EAC application.
	exit /B 1
)
echo Finished updating EAC.

REM Create the crawl after creating the Dimension Value Id Manager, Record Stores, and IFCR Site
echo Creating crawl %LAST_MILE_CRAWL_NAME%
call %CAS_ROOT%\bin\cas-cmd.bat createCrawls -h %CAS_HOST% -p %CAS_PORT% -f %~dp0..\config\cas\last-mile-crawl.xml
if not %ERRORLEVEL%==0 (
	echo Failure to create crawl.
	exit /B 1
)

REM Remove the following step if not using sample application and data.

echo Importing content...

REM import content using public format
call %~dp0runcommand.bat IFCR importApplication %~dp0..\config\import
if not %ERRORLEVEL%==0 (
    echo Failed to import content in public format.
    exit /b 1
)

REM import content using legacy format
call %~dp0runcommand.bat IFCR legacyUpdateContent "" %~dp0..\config\ifcr
if not %ERRORLEVEL%==0 (
	echo Failed to import content in legacy format.
	exit /B 1
)

echo Finished importing content

endlocal
exit /B 0

:NOCASROOT
echo No CAS install folder was found at '%CAS_ROOT%'. Please install CAS.
endlocal
exit /B 1


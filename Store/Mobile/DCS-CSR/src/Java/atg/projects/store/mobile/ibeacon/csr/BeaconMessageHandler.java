/*<ORACLECOPYRIGHT>
 * Copyright (C) 1994, 2018, Oracle and/or its affiliates. All rights reserved.
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 * UNIX is a registered trademark of The Open Group.
 *
 * This software and related documentation are provided under a license agreement
 * containing restrictions on use and disclosure and are protected by intellectual property laws.
 * Except as expressly permitted in your license agreement or allowed by law, you may not use, copy,
 * reproduce, translate, broadcast, modify, license, transmit, distribute, exhibit, perform, publish,
 * or display any part, in any form, or by any means. Reverse engineering, disassembly,
 * or decompilation of this software, unless required by law for interoperability, is prohibited.
 *
 * The information contained herein is subject to change without notice and is not warranted to be error-free.
 * If you find any errors, please report them to us in writing.
 *
 * U.S. GOVERNMENT RIGHTS Programs, software, databases, and related documentation and technical data delivered to U.S.
 * Government customers are "commercial computer software" or "commercial technical data" pursuant to the applicable
 * Federal Acquisition Regulation and agency-specific supplemental regulations.
 * As such, the use, duplication, disclosure, modification, and adaptation shall be subject to the restrictions and
 * license terms set forth in the applicable Government contract, and, to the extent applicable by the terms of the
 * Government contract, the additional rights set forth in FAR 52.227-19, Commercial Computer Software License
 * (December 2007). Oracle America, Inc., 500 Oracle Parkway, Redwood City, CA 94065.
 *
 * This software or hardware is developed for general use in a variety of information management applications.
 * It is not developed or intended for use in any inherently dangerous applications, including applications that
 * may create a risk of personal injury. If you use this software or hardware in dangerous applications,
 * then you shall be responsible to take all appropriate fail-safe, backup, redundancy,
 * and other measures to ensure its safe use. Oracle Corporation and its affiliates disclaim any liability for any
 * damages caused by use of this software or hardware in dangerous applications.
 *
 * This software or hardware and documentation may provide access to or information on content,
 * products, and services from third parties. Oracle Corporation and its affiliates are not responsible for and
 * expressly disclaim all warranties of any kind with respect to third-party content, products, and services.
 * Oracle Corporation and its affiliates will not be responsible for any loss, costs,
 * or damages incurred due to your access to or use of third-party content, products, or services.
 </ORACLECOPYRIGHT>*/


package atg.projects.store.mobile.ibeacon.csr;

import atg.nucleus.GenericService;
import atg.projects.store.mobile.device.csr.DeviceLocationManager;
import atg.projects.store.mobile.ibeacon.EnteredStoreMessage;
import atg.projects.store.mobile.ibeacon.ExitedStoreMessage;
import atg.projects.store.mobile.ibeacon.EnteredBeaconMessage;
import atg.projects.store.mobile.ibeacon.ExitedBeaconMessage;
import atg.projects.store.mobile.ibeacon.RequestedHelpMessage;
import atg.projects.store.mobile.ibeacon.CancelledHelpMessage;
import atg.projects.store.mobile.ibeacon.UpdatedBeaconRangeMessage;

/**
 * Processes Beacon Messages received in the message sink and passes them to the appropriate class
 *
 * @author
 * @version
 * @updated
 */
public class BeaconMessageHandler extends GenericService {

  //-------------------------------------
  /** Class version string */

  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/ibeacon/csr/BeaconMessageHandler.java#2 $$Change: 1503965 $";



  private DeviceLocationManager mDeviceLocationManager;

  /**
   * gets the DeviceLocationManager where the shopper location information is processed
   * @return the DeviceLocationManger
   */
  public DeviceLocationManager getDeviceLocationManager() {
    return mDeviceLocationManager;
  }

  /**
   * sets the DeviceLocationManager where shopper location information is processed
   * @param pDeviceLocationManager the DeviceLocationManager to set
   */
  public void setDeviceLocationManager(DeviceLocationManager pDeviceLocationManager) {
    mDeviceLocationManager = pDeviceLocationManager;
  }

  /**
   * Processes the EnteredStoreMessage
   * @param pBeaconMessage Message signifying a shopper has entered the store
   */
  public void processMessage(EnteredStoreMessage pBeaconMessage){
    getDeviceLocationManager().enteredStore(pBeaconMessage.getProfileId(),
      pBeaconMessage.getStoreId());
  }

  /**
   *  Processes the ExitedStoreMessage
   * @param pBeaconMessage Message signifying shopper has left the store
   */
  public void processMessage(ExitedStoreMessage pBeaconMessage){
    getDeviceLocationManager().exitedStore(pBeaconMessage.getProfileId(),
      pBeaconMessage.getStoreId());
  }

  /**
   * Processes the EnteredBeaconMessage
   * @param pBeaconMessage Message containing info on the beacon(s) that have entered range
   */
  public void processMessage(EnteredBeaconMessage pBeaconMessage){
    getDeviceLocationManager().enteredBeacon(pBeaconMessage.getProfileId(), pBeaconMessage.getBeaconId());
  }

  /**
   * Processes the ExitedBeaconMessage
   * @param pBeaconMessage Message containing info on the beacon(s) that are no longer in range
   */
  public void processMessage(ExitedBeaconMessage pBeaconMessage){
    getDeviceLocationManager().exitedBeacon(pBeaconMessage.getProfileId(), pBeaconMessage.getBeaconId());
  }

  /**
   * Processes the UpdatedBeaconRangeMessage
   * @param pBeaconMessage Message containing beacon range info
   */
  public void processMessage(UpdatedBeaconRangeMessage pBeaconMessage){
    getDeviceLocationManager().updatedBeaconRange(pBeaconMessage.getProfileId(),
      pBeaconMessage.getBeaconId());
  }

  /**
   * Processes the RequestedHelpMessage
   * @param pBeaconMessage Message containing the help request info
   */
  public void processMessage(RequestedHelpMessage pBeaconMessage){
    getDeviceLocationManager().requestedHelp(pBeaconMessage.getProfileId(),
      pBeaconMessage.getBeaconId(),
      pBeaconMessage.getParams());
  }

  /**
   * Processes the CancelledHelpMessage
   * @param pBeaconMessage Message containing cancel info
   */
  public void processMessage(CancelledHelpMessage pBeaconMessage){
    getDeviceLocationManager().cancelledHelpRequest(pBeaconMessage.getProfileId());
  }

}

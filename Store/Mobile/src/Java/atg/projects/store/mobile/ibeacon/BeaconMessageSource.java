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
package atg.projects.store.mobile.ibeacon;

import atg.nucleus.dms.DASMessageSource;

import java.util.Map;

/**
 * Provides methods that fire a JMS event for when state about beacon ranging needs to be updated. Listeners can
 * listen for when beacons and stores are entered and exited.
 *
 * @author  dkenyon
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/ibeacon/BeaconMessageSource.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 **/
public class BeaconMessageSource extends DASMessageSource
{
  //-------------------------------------
  // Class version string
  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/ibeacon/BeaconMessageSource.java#3 $$Change: 1536476 $";

  //-------------------------------------
  // Serialisation
  private static final long serialVersionUID = 1L;


  //-------------------------------------
  // Properties
  //-------------------------------------

  // Property: portName
  // The port name to use when firing ''beacon' events
  private String mPortName = "atg.projects.store.mobile.ibeacon";

  /**
   * Gets the port name to use when firing JMS events
   * @return The JMS port name to use
   */
  public String getPortName()
  {
    return mPortName;
  }

  /**
   * Sets the port name to use when firing JMS events
   * @param pPortName The port name to set.
   */
  public void setPortName(String pPortName)
  {
    mPortName = pPortName;
  }


  //-------------------------------------
  /**
   * Creates an <code>EnteredBeaconMessage</code> object from the given parameters and then fires a JMS message 
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the 
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   * @param pBeaconId The id of beacon that was encountered.
   * @param pBeaconName The name of the beacon that was encountered.
   */
  public void fireEnteredBeacon(String pDeviceUID, String pProfileId, String pSessionId, String pBeaconId, String pBeaconName)
  {
    fireObjectMessage(new EnteredBeaconMessage(pBeaconId, pBeaconName), pDeviceUID, pProfileId, pSessionId);
  }

  //-------------------------------------
  /**
   * Creates an <code>ExitedBeaconMessage</code> object from the given parameters and then fires a JMS message 
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the 
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   * @param pBeaconId The beacon that was encountered.
   */
  public void fireExitedBeacon(String pDeviceUID, String pProfileId, String pSessionId, String pBeaconId)
  {
    fireObjectMessage(new ExitedBeaconMessage(pBeaconId), pDeviceUID, pProfileId, pSessionId);

  }

  //-------------------------------------
  /**
   * Creates an <code>EnteredStoreMessage</code> object from the given parameters and then fires a JMS message 
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the 
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   * @param pStoreId The store that was encountered.
   */
  public void fireEnteredStore(String pDeviceUID, String pProfileId, String pSessionId, String pStoreId)
  {
    fireObjectMessage(new EnteredStoreMessage(pStoreId), pDeviceUID, pProfileId, pSessionId);
  }

  //-------------------------------------
  /**
   * Creates an <code>ExitedStoreMessage</code> object from the given parameters and then fires a JMS message 
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the 
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   * @param pStoreId The store that was encountered.
   */
  public void fireExitedStore(String pDeviceUID, String pProfileId, String pSessionId, String pStoreId)
  {
    fireObjectMessage(new ExitedStoreMessage(pStoreId), pDeviceUID, pProfileId, pSessionId);
  }

  //-------------------------------------
  /**
   * Creates an <code>RequestedHelp</code> object from the given parameters and then fires a JMS message
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   * @param pBeaconId The repository id of the last beacon seen by the device requesting help.
   * @param pBeaconName The name of the last beacon seen by the device requesting help.
   * @param pParams The parameters sent with the help request.
   */
  public void fireRequestedHelp(String pDeviceUID,
                                String pProfileId,
                                String pSessionId,
                                String pBeaconId,
                                String pBeaconName,
                                Map pParams)
  {
    fireObjectMessage(new RequestedHelpMessage(pParams, pBeaconId, pBeaconName), pDeviceUID, pProfileId, pSessionId);
  }

  //-------------------------------------
  /**
   * Creates an <code>CancelledHelp</code> object from the given parameters and then fires a JMS message
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   */
  public void fireCancelledHelp(String pDeviceUID, String pProfileId, String pSessionId)
  {
    fireObjectMessage(new CancelledHelpMessage(), pDeviceUID, pProfileId, pSessionId);
  }

  //-------------------------------------
  /**
   * Creates an <code>UpdatedRanging</code> object from the given parameters and then fires a JMS message
   * containing this, using the <code>jmsType</code property to set the JMS type of the message and the
   * <code>portName</code> property to denote the port name for the event.
   *
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pBeaconId The repository id of the last beacon seen by the device.
   * @param pBeaconName The name of the last beacon seen by the device.
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   */
  public void fireUpdatedBeaconRange(String pDeviceUID,
                                     String pBeaconId,
                                     String pBeaconName,
                                     String pProfileId,
                                     String pSessionId)
  {
    fireObjectMessage(new UpdatedBeaconRangeMessage(pBeaconId, pBeaconName), pDeviceUID, pProfileId, pSessionId);
  }

  /**
   * Fire the beacon message after adding some other properties.
   * 
   * @param pBeaconMessage  The message to fire.
   * @param pDeviceUID The unique id of the device that caused the interaction with the ibeacon
   * @param pProfileId The profile ID of the user that the impression was presented to
   * @param pSessionId  The session ID of the session where the impression was made
   */
  protected void fireObjectMessage(BeaconMessage pBeaconMessage, String pDeviceUID, String pProfileId, String pSessionId) {
    pBeaconMessage.setDeviceUID(pDeviceUID);
    pBeaconMessage.setProfileId(pProfileId);
    pBeaconMessage.setSessionId(pSessionId);
    pBeaconMessage.setTimestamp(System.currentTimeMillis());
    super.fireObjectMessage(pBeaconMessage, getPortName(), pBeaconMessage.getMessageType());
  }
}

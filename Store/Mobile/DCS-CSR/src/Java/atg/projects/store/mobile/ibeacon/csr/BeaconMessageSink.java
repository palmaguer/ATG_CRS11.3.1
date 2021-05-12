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

import atg.dms.patchbay.MessageSink;
import atg.nucleus.GenericService;
import atg.projects.store.mobile.ibeacon.BeaconMessage;
import atg.projects.store.mobile.ibeacon.EnteredBeaconMessage;
import atg.projects.store.mobile.ibeacon.EnteredStoreMessage;
import atg.projects.store.mobile.ibeacon.ExitedStoreMessage;
import atg.projects.store.mobile.ibeacon.ExitedBeaconMessage;
import atg.projects.store.mobile.ibeacon.RequestedHelpMessage;
import atg.projects.store.mobile.ibeacon.UpdatedBeaconRangeMessage;
import atg.projects.store.mobile.ibeacon.CancelledHelpMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * Message Sink that receives Beacon messages and requests further processing of these
 *
 * @author
 * @version
 * @updated
 */
public class BeaconMessageSink extends GenericService implements MessageSink {

  //-------------------------------------
  /** Class version string */

  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/ibeacon/csr/BeaconMessageSink.java#2 $$Change: 1503965 $";



  protected BeaconMessageHandler mBeaconMessageHandler;

  /**
   * Gets the BeaconMessageHandler that processes Beacon Messages
   * @return the BeaconMessageHandler
   */
  public BeaconMessageHandler getBeaconMessageHandler() {
    return mBeaconMessageHandler;
  }

  /**
   * sets the message handler for the Beacon messages
   * @param pBeaconMessageHandler the BeaconMessageHandler to set
   */
  public void setBeaconMessageHandler(BeaconMessageHandler pBeaconMessageHandler) {
    mBeaconMessageHandler = pBeaconMessageHandler;
  }

  /**
   * Invoked when a new message is received
   * @param pPortName
   * @param pMessage
   * @throws JMSException
   */
  public void receiveMessage(String pPortName, Message pMessage)
    throws JMSException {

    //Get the type of the message
    String messageType = pMessage.getJMSType();
    if(isLoggingDebug()){
      logDebug("Type of message receieved " + messageType);
    }

    ObjectMessage oMessage = (ObjectMessage) pMessage;

    //Check the message is a beacon message
    if(oMessage.getObject() instanceof BeaconMessage){

      //cast as a BeaconMessage
      BeaconMessage receivedBeaconMessage = (BeaconMessage)oMessage.getObject();

      //Process the message
      processMessage(receivedBeaconMessage);
    }
  }

  /**
   * Processes BeaconMessages received by the message sink and hands them the the message handler for processing
   * @param pBeaconMessage BeaconMessage received and to be processed
   */
  private void processMessage(BeaconMessage pBeaconMessage){
    if(pBeaconMessage instanceof EnteredStoreMessage){
      getBeaconMessageHandler().processMessage((EnteredStoreMessage)pBeaconMessage);
    }
    else if(pBeaconMessage instanceof ExitedStoreMessage){
      getBeaconMessageHandler().processMessage((ExitedStoreMessage)pBeaconMessage);
    }
    else if(pBeaconMessage instanceof EnteredBeaconMessage){
      getBeaconMessageHandler().processMessage((EnteredBeaconMessage)pBeaconMessage);
    }
    else if(pBeaconMessage instanceof ExitedBeaconMessage){
      getBeaconMessageHandler().processMessage((ExitedBeaconMessage)pBeaconMessage);
    }
    else if(pBeaconMessage instanceof UpdatedBeaconRangeMessage){
      getBeaconMessageHandler().processMessage((UpdatedBeaconRangeMessage)pBeaconMessage);
    }
    else if(pBeaconMessage instanceof RequestedHelpMessage){
      getBeaconMessageHandler().processMessage((RequestedHelpMessage)pBeaconMessage);
    }
    else if(pBeaconMessage instanceof CancelledHelpMessage){
      getBeaconMessageHandler().processMessage((CancelledHelpMessage)pBeaconMessage);
    }
  }

}

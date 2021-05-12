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

package atg.commerce.fulfillment;

import atg.commerce.CommerceException;
import atg.service.pipeline.PipelineResult;
import atg.service.pipeline.RunProcessException;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.Properties;

/**
 * <p>The InStoreSaleFulfillerModificationHandler deals with the
 * ModifyOrder and ModifyOrderNotifications messages received by the
 * InStoreSaleFulfiller. The InStoreSaleFulfiller contains a
 * ModificationHandler property, which is set by default to the
 * InStoreSaleFulfillerModificationHandler. This class is similar to the
 * OrderFulfillerModificationHandler.</p>
 *
 * <p>To change the handling behavior of ModifyOrder and ModifyOrderNotification
 * messages, extend the InStoreSaleFulfillerModificationHandler class and change
 * the ModificationHandler property InStoreSaleFulfiller to point to the new class.</p>
 *
 * <p>The default implementation deals with the following ModifyOrder modification:
 *        Remove the shipping group from the order:
 *                       The fulfillers can remove shipping groups if they have
 *                       not been shipped. Determining whether a shipping group
 *                       has been shipped can be difficult because of the asynchronous
 *                       nature of shipping items. Consulting the states may not be
 *                       enough to determine if the group has been shipped.
 *                       DCS 5.0 consults the state to make sure that it isn't in a
 *                       NO_PENDING_ACTION or REMOVED state. This is sufficient because
 *                       out-of-the box there is no integration with a real warehouse
 *                       so shipment is indicated by changing a set of states in the order
 *                       repository. Some vendors might decide to create business rules
 *                       that limit the time in which cancellations can occur because it
 *                       is difficult to determine the exact shipping time for a shipping group.
 *        Ship the shipping group.
 *                       The InStoreSaleFulfiller can be notified that a shipping group has
 *                       shipped through a ModifyOrder message (which is originally sent
 *                       to the OrderFulfiller, then forwarded to the InStoreSaleFulfiller).
 *                       The InStoreSaleFulfiller gets a GenericUpdate modification through
 *                       the ModifyOrder message, checks the current state of the shipping
 *                       group to ensure that it is PENDING_SHIPMENT.  If everything is
 *                       fine, it sets the state to NO_PENDING_ACTION and notifies the
 *                       rest of the system of the change with a ModifyOrderNotification
 *                       message.
 *
 * @author pmacrory
 * @version: $Id: //product/DAS/main/Java/atg
 * @updated: $DateTime: 14:53
 */
public class InStoreSaleFulfillerModificationHandler  implements ModificationHandler {

  //-------------------------------------
  /** Class version string */
  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/Fulfillment/src/atg/commerce/fulfillment/InStoreSaleFulfillerModificationHandler.java#2 $$Change: 1503965 $";


  //---------------------------------------------------------------------------
  // property:InStoreSaleFulfiller
  //---------------------------------------------------------------------------

  private InStoreSaleFulfiller mInStoreSaleFulfiller;

  public void setInStoreSaleFulfiller(InStoreSaleFulfiller pInStoreSaleFulfiller) {
    mInStoreSaleFulfiller = pInStoreSaleFulfiller;
  }

  /**
   * The fulfiller object that this class handles modifications for
   **/
  public InStoreSaleFulfiller getInStoreSaleFulfiller() {
    return mInStoreSaleFulfiller;
  }

  /**
   * The map of the chains to run to execute pipeline
   **/
  private Properties mChainToRunMap;

  public Properties getChainToRunMap() {
    return mChainToRunMap;
  }

  public void setChainToRunMap(Properties pChainToRunMap) {
    mChainToRunMap = pChainToRunMap;
  }


  //-------------------------------------
  /**
   * <p> This method is called to handle all messages of type
   * ModifyOrder.  Should developers wish to change the behavior of
   * the InStoreSaleFulfiller class on handling a ModifyOrder message
   * this method should be overridden. This method calls various
   * methods to respond to the ModifyOrder requests. Modifications on
   * orders are ignored since the InStoreSaleFulfiller does not have the
   * authority to edit orders.
   * <ul>
   *   <li>performShippingGroupModification - called if the request's
   *       target type is Modification.TARGET_SHIPPING_GROUP</li>
   *   <li>performItemModification - called if the request's
   *       target type is Modification.TARGET_ITEM</li>
   *   <li>performRelationshipModification - called if the request's
   *       target type is Modification.TARGET_RELATIONSHIP</li>
   * </ul>
   *
   * @param pPortName The port from which this message was received.
   * @param pMessage The message that was received.
   * @exception javax.jms.JMSException
   * @see ModifyOrder
   * @see #performShippingGroupModification
   * @see #performItemModification
   * @see #performRelationshipModification
   * @see IdTargetModification#getTargetType
   * @see Modification
   **/
  public void handleModifyOrder(String pPortName, ObjectMessage pMessage) throws JMSException
  {

    InStoreSaleFulfiller of = getInStoreSaleFulfiller();

    if (of.isLoggingDebug())
      of.logDebug("Handling a modifyOrder message in InStoreSaleFulfillerModificationHandler");

    // the input params to the chain
    HashMap map = new HashMap(10);
    map.put(PipelineConstants.MESSAGE, pMessage);
    map.put(PipelineConstants.ORDERFULFILLER, of);

    try {
      String chainToRun = (String)getChainToRunMap().get("modifyOrderChain");
      // execute the submitOrder pipeline chain
      PipelineResult results =
          of.getFulfillmentPipelineManager().runProcess(chainToRun, map);
    }
    catch (RunProcessException e) {
      Throwable p = e.getSourceException();
      if(of.isLoggingError())
        of.logError(p);
      // check the thrown exceptions
      if (p instanceof CommerceException) {
        try {
          of.getTransactionManager().getTransaction().setRollbackOnly();
        }
        catch (javax.transaction.SystemException se) {
          // Hopefully this will never happen.
          if(of.isLoggingError())
            of.logError(se);
        }
      }
      if (p instanceof JMSException) {
        throw (JMSException)p;
      }
    }
  }



  //-------------------------------------
  /**
   * <p> This method is called to handle all messages of type
   * ModifyOrderNotification.  Should developers wish to
   * change the behavior of the InStoreSaleFulfiller class on handling a
   * ModifyOrderNotification message this method should be
   * overridden. Currently only one modification type is handled by
   * this method. If it is an IdTargetModification then handleIdTargetModification
   * is called.
   * <ul>
   *   <li>handleShippingGroupUpdateModification - called if the notification is
   *       of type ShippingGroupUpdate
   * </ul>
   *
   * @param pPortName The port from which this message was received.
   * @param pMessage The message that was received.
   * @exception javax.jms.JMSException
   * @see ModifyOrderNotification
   * @see #handleShippingGroupUpdateModification
   * @see #handleIdTargetModification
   * @see ShippingGroupUpdate
   **/
  public void handleModifyOrderNotification(String pPortName, ObjectMessage pMessage) throws JMSException
  {

    InStoreSaleFulfiller of = getInStoreSaleFulfiller();

    if (of.isLoggingDebug())
      of.logDebug("Handling a modifyOrderNotification message in InStoreSaleFulfillerModificationHandler");

    // the input params to the chain
    HashMap map = new HashMap(10);
    map.put(PipelineConstants.MESSAGE, pMessage);
    map.put(PipelineConstants.ORDERFULFILLER, of);

    try {
      String chainToRun = (String) getChainToRunMap().get("modifyOrderNotificationChain");
      // execute the submitOrder pipeline chain
      PipelineResult results =
          of.getFulfillmentPipelineManager().runProcess(chainToRun, map);
    }
    catch (RunProcessException e) {
      Throwable p = e.getSourceException();
      if(of.isLoggingError())
        of.logError(p);
      // check the thrown exceptions
      if (p instanceof CommerceException) {
        try {
          of.getTransactionManager().getTransaction().setRollbackOnly();
        }
        catch (javax.transaction.SystemException se) {
          // Hopefully this will never happen.
          if(of.isLoggingError())
            of.logError(se);
        }
      }
      if (p instanceof JMSException) {
        throw (JMSException)p;
      }
    }
  }

}
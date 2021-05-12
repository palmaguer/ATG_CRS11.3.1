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

package atg.commerce.order.purchase;

import atg.commerce.CommerceException;
import atg.commerce.order.InStorePickupShippingGroup;
import atg.commerce.order.InStoreSaleShippingGroup;
import atg.commerce.order.ShippingGroup;
import atg.nucleus.GenericService;
import atg.servlet.DynamoHttpServletRequest;
import atg.userprofiling.Profile;

import java.util.Iterator;
import java.util.Set;

/**
 * This class initializes an InStorePickupShippingGroup.
 *
 * @author pmacrory
 * @version: $Id: //product/DAS/main/Java/atg
 * @updated: $DateTime: 15:44
 */
public class InStoreSaleShippingGroupInitializer extends GenericService implements ShippingGroupInitializer, ShippingGroupMatcher
{

  //-------------------------------------
  // Class version string
  //-------------------------------------

  public static final String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/commerce/order/purchase/InStoreSaleShippingGroupInitializer.java#2 $$Change: 1503965 $";

  //--------------------------------------------------
  // Constants
  //--------------------------------------------------

  static final String MY_RESOURCE_NAME = "atg.commerce.order.purchase.PurchaseProcessResources";
  static final String USER_RESOURCE_NAME = "atg.commerce.order.purchase.UserMessages";

  /** Resource Bundle **/
  private static java.util.ResourceBundle sResourceBundle = atg.core.i18n.LayeredResourceBundle.getBundle(MY_RESOURCE_NAME, atg.service.dynamo.LangLicense.getLicensedDefault());

  /**
   * {@inheritDoc}
   */
  public void initializeShippingGroups( Profile pProfile,
                                        ShippingGroupMapContainer pShippingGroupMapContainer,
                                        DynamoHttpServletRequest pRequest )
      throws ShippingGroupInitializationException {
    //No groups to be initialized
  }

  /**
   * {@inheritDoc}
   */
  public String getNewShippingGroupName( ShippingGroup pShippingGroup ) {
    if(pShippingGroup instanceof InStoreSaleShippingGroup) {
      return ((InStoreSaleShippingGroup)pShippingGroup).getLocationId();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String matchShippingGroup( ShippingGroup pShippingGroup,
                                    ShippingGroupMapContainer pShippingGroupMapContainer ) {
    if (!(pShippingGroup instanceof InStoreSaleShippingGroup)) return null;

    Set shippingGroupNames = pShippingGroupMapContainer.getShippingGroupNames();
    if (shippingGroupNames == null) return null;

    Iterator nameIter = shippingGroupNames.iterator();
    String shippingGroupName = null;
    boolean found = false;
    while (nameIter.hasNext() && !found) {
      shippingGroupName = (String) nameIter.next();
      ShippingGroup shippingGroup = pShippingGroupMapContainer.getShippingGroup(shippingGroupName);

      if (shippingGroup instanceof InStorePickupShippingGroup) {
        try {
          found = ((InStorePickupShippingGroup) shippingGroup).doPropertiesMatch((InStoreSaleShippingGroup)pShippingGroup);
        } catch (CommerceException ce) {
          if(isLoggingError()){
            logError(ce);
          }
        }
      }
    }

    if (found) return shippingGroupName;
    else return null;
  }
}

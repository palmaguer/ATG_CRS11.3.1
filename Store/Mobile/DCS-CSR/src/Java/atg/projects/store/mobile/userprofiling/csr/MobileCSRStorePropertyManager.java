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

package atg.projects.store.mobile.userprofiling.csr;

import atg.projects.store.mobile.userprofiling.MobileStorePropertyManager;

/**
 * Component that manages the property names in this module
 *
 * @author pmacrory
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/userprofiling/csr/MobileCSRStorePropertyManager.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class MobileCSRStorePropertyManager extends MobileStorePropertyManager {

  /** Class version string. */
  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/userprofiling/csr/MobileCSRStorePropertyManager.java#3 $$Change: 1536476 $";

  public static final String RESOURCE_BUNDLE_NAME = "atg.projects.store.userprofiling.UserProfileResources";

  private String mInStoreUsersItemDescriptorName = "inStoreDeviceUser";

  private String mUserPropertyName = "profileId";

  private String mStorePropertyName = "store";

  private String mBeaconPropertyName = "beacon";

  private String mLastSeenPropertyName = "lastSeen";

  private String mHelpRequestedPropertyName = "helpRequested";

  private String mHelpRequestTimePropertyName = "helpRequestTimestamp";

  private String mUserDisplayNamePropertyName = "userDisplayName";

  /**
   * @return The InStoreUsers item descriptor name.
   */
  public String getInStoreUsersItemDescriptorName() {
    return mInStoreUsersItemDescriptorName;
  }

  /**
   * @param pInStoreUsersItemDescriptorNam The InStoreUser item descriptor name.
   */
  public void setInStoreUsersItemDescriptorName(String pInStoreUsersItemDescriptorNam) {
    mInStoreUsersItemDescriptorName = pInStoreUsersItemDescriptorNam;
  }


  public String getUserPropertyName() {
    return mUserPropertyName;
  }

  public void setUserPropertyName(String mUserPropertyName) {
    this.mUserPropertyName = mUserPropertyName;
  }

  public String getStorePropertyName() {
    return mStorePropertyName;
  }

  public void setStorePropertyName(String mStorePropertyName) {
    this.mStorePropertyName = mStorePropertyName;
  }

  public String getBeaconPropertyName() {
    return mBeaconPropertyName;
  }

  public void setBeaconPropertyName(String mBeaconPropertyName) {
    this.mBeaconPropertyName = mBeaconPropertyName;
  }

  public String getLastSeenPropertyName() {
    return mLastSeenPropertyName;
  }

  public void setLastSeenPropertyName(String mLastSeenPropertyName) {
    this.mLastSeenPropertyName = mLastSeenPropertyName;
  }

  public String getHelpRequestedPropertyName() {
    return mHelpRequestedPropertyName;
  }

  public void setHelpRequestedPropertyName(String mHelpRequestedPropertyName) {
    this.mHelpRequestedPropertyName = mHelpRequestedPropertyName;
  }

  public String getHelpRequestTimePropertyName() {
    return mHelpRequestTimePropertyName;
  }

  public void setHelpRequestTimePropertyName(String mHelpRequestTimePropertyName) {
    this.mHelpRequestTimePropertyName = mHelpRequestTimePropertyName;
  }

  public String getUserDisplayNamePropertyName() {
    return mUserDisplayNamePropertyName;
  }

  public void setUserDisplayNamePropertyName(String mUserDisplayNamePropertyName) {
    this.mUserDisplayNamePropertyName = mUserDisplayNamePropertyName;
  }

  public static java.util.ResourceBundle sResourceBundle =
    atg.core.i18n.LayeredResourceBundle.getBundle(RESOURCE_BUNDLE_NAME,
      atg.service.dynamo.LangLicense.getLicensedDefault());

}

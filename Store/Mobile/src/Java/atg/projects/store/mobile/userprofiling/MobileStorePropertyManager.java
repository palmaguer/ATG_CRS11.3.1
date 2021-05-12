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

package atg.projects.store.mobile.userprofiling;

import atg.projects.store.profile.StorePropertyManager;

public class MobileStorePropertyManager extends StorePropertyManager {
  
  /** Class version string. */
  public static final String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/userprofiling/MobileStorePropertyManager.java#2 $$Change: 1503965 $";

  protected String mMobileDevicesPropertyName = "mobileDevices";

  /**
   * @return The mobileDevices property name.
   */
  public String getmobileDevicesPropertyName() {
    return mMobileDevicesPropertyName;
  }

  /**
   * @param pMobileDevicesPropertyName The MobileDevices property name.
   */
  public void setMobileDevicesPropertyName(String pMobileDevicesPropertyName) {
    mMobileDevicesPropertyName = pMobileDevicesPropertyName;
  }
  
  protected String mMobileDeviceItemDescriptorName = "mobileDevice";

  /**
   * @return The mobileDevice item descriptor name.
   */
  public String getMobileDeviceItemDescriptorName() {
    return mMobileDeviceItemDescriptorName;
  }

  /**
   * @param pMobileDeviceItemDescriptorName The MobileDevice item descriptor name.
   */
  public void setMobileDeviceItemDescriptorName(String pMobileDeviceItemDescriptorName) {
    mMobileDeviceItemDescriptorName = pMobileDeviceItemDescriptorName;
  }
  
  protected String mUniqueIdPropertyName = "uniqueId";

  /**
   * @return The name of property name of mobiledevice 'uniqueId'.
   */
  public String getUniqueIdPropertyName() {
    return mUniqueIdPropertyName;
  }
  /**
   * @param pUniqueIdPropertyName The name of property name of mobiledevice 'uniqueId'.
   */
  public void setUniqueIdPropertyName(String pUniqueIdPropertyName) {
    mUniqueIdPropertyName = pUniqueIdPropertyName;
  }
  
  protected String mProfilePropertyName = "profile";
  
  /**
   * @return the mProfilePropetyName
   */
  public String getProfilePropertyName() {
    return mProfilePropertyName;
  }

  /**
   * @param mProfilePropertyName the mProfilePropetyName to set
   */
  public void setProfilePropetyName(String pProfilePropertyName) {
    mProfilePropertyName = pProfilePropertyName;
  }

  protected String mPushTokenPropertyName = "pushToken";

  /**
   * @return The name of property name of mobiledevice 'pushToken'.
   */
  public String getPushTokenPropertyName() {
    return mPushTokenPropertyName;
  }
  /**
   * @param pPushTokenPropertyName The name of property name of mobiledevice 'pushToken'.
   */
  public void setPushTokenPropertyName(String pPushTokenPropertyName) {
    mPushTokenPropertyName = pPushTokenPropertyName;
  }

  protected String mOsPropertyName = "os";

  /**
   * @return The name of property name of mobiledevice 'os'.
   */
  public String getOsPropertyName() {
    return mOsPropertyName;
  }
  /**
   * @param pOsPropertyName The name of property name of mobiledevice 'os'.
   */
  public void setOsPropertyName(String pOsPropertyName) {
    mOsPropertyName = pOsPropertyName;
  }
  
  protected String mOsVersionPropertyName = "osVersion";

  /**
   * @return The name of property name of mobiledevice 'osVersion'.
   */
  public String getOsVersionPropertyName() {
    return mOsVersionPropertyName;
  }
  /**
   * @param pOsVersionPropertyName The name of property name of mobiledevice 'osVersion'.
   */
  public void setOsVersionPropertyName(String pOsVersionPropertyName) {
    mOsVersionPropertyName = pOsVersionPropertyName;
  }
  
  protected String mAppPropertyName = "app";

  /**
   * @return The name of property name of mobiledevice 'app'.
   */
  public String getAppPropertyName() {
    return mAppPropertyName;
  }
  /**
   * @param pAppPropertyName The name of property name of mobiledevice 'app'.
   */
  public void setAppPropertyName(String pAppPropertyName) {
    mAppPropertyName = pAppPropertyName;
  }
  
  protected String mAppVersionPropertyName = "appVersion";

  /**
   * @return The name of property name of mobiledevice 'appVersion'.
   */
  public String getAppVersionPropertyName() {
    return mAppVersionPropertyName;
  }
  /**
   * @param pAppVersionPropertyName The name of property name of mobiledevice 'AppVersion'.
   */
  public void setAppVersionPropertyName(String pAppVersionPropertyName) {
    mAppVersionPropertyName = pAppVersionPropertyName;
  }
  
  protected String mRegisteredOnPropertyName = "registeredOn";

  /**
   * @return The name of property name of mobiledevice 'RegisteredOn'.
   */
  public String getRegisteredOnPropertyName() {
    return mRegisteredOnPropertyName;
  }
  /**
   * @param pRegisteredOnPropertyName The name of property name of mobiledevice 'RegisteredOn'.
   */
  public void setRegisteredOnPropertyName(String pRegisteredOnPropertyName) {
    mRegisteredOnPropertyName = pRegisteredOnPropertyName;
  }
  
  protected String mSeenOnPropertyName = "seenOn";

  /**
   * @return The name of property name of mobiledevice 'SeenOn'.
   */
  public String getSeenOnPropertyName() {
    return mSeenOnPropertyName;
  }
  /**
   * @param pSeenOnPropertyName The name of property name of mobiledevice 'SeenOn'.
   */
  public void setSeenOnPropertyName(String pSeenOnPropertyName) {
    mSeenOnPropertyName = pSeenOnPropertyName;
  }
}

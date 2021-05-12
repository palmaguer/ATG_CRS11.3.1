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

package atg.projects.store.mobile.push;

import java.util.Map;

import javax.servlet.Servlet;

import atg.nucleus.GenericService;
import atg.nucleus.ServiceMap;
import atg.projects.store.mobile.MobileStoreConfiguration;
import atg.projects.store.mobile.device.DeviceManager;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

/**
 * Class handles pushing notifications to notification service.  Can choose between apple push notification service
 * or another device service from the pushServiceMap
 *
 * @author gdoneil
 * @version $Change: 1536476 $$DateTime: 2018/04/13 08:11:14 $$Author: releng $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */

public class PushNotificationManager extends GenericService {
  public static String CLASS_VERSION =
      "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/push/PushNotificationManager.java#3 $$Change: 1536476 $";
  
  // --------------------------------------------------
  // Properties 
  // --------------------------------------------------

  // --------------------------------------------------
  // Property pushServiceMap
  private ServiceMap<PushNotificationService> mPushServiceMap = new ServiceMap<>();
  
  /**
   * Gets the map of device type (android/apple/windows) to push manager
   * @return the mPushServiceMap
   */
  public ServiceMap<PushNotificationService> getPushServiceMap() {
    return mPushServiceMap;
  }

  /**
   * Sets the push manager map
   * @param mPushServiceMap the mPushServiceMap to set
   */
  public void setPushServiceMap(ServiceMap<PushNotificationService> pPushServiceMap) {
    mPushServiceMap = pPushServiceMap;
  }
  
  // ---------------------------------------------------------------------------
  // property: deviceManager
  private DeviceManager mDeviceManager;
  
  /**
   * @param pDeviceManager the device manager to set
   */
  public void setDeviceManager(DeviceManager pDeviceManager) {
    mDeviceManager = pDeviceManager;
  }

  /**
   * @return the device manager
   */
  public DeviceManager getDeviceManager() {
    return mDeviceManager;
  }

  // --------------------------------------------------
  // Protected methods
  // -------------------------------------------------- 
  /* (non-Javadoc)
   * @see atg.nucleus.GenericService#createAdminServlet()
   */
  @Override
  protected Servlet createAdminServlet() {
    return new PushNotificationManagerAdminServlet(this, getNucleus());
  }
  
  // --------------------------------------------------
  // Public methods
  // --------------------------------------------------
  /**
   * Sends notification to the profile's device
   * @param pDeviceUniqueId the profile that the notification should be sent to
   * @param pBody the notification text
   * @param pLocKey the localizeable key (instead of body)
   * @param pLink the notification's link or null
   * @param pExtra any extra data to include in the notification. Null if no extra data
   * @throws PushNotificationException
   */
  public void sendNotification(String pDeviceUniqueId, String pBody, String pLocKey,
                               String pLink, Map<String, Object> pExtra) 
      throws PushNotificationException
  {
    RepositoryItem device = null;
    try {
      // Get device from repository.  If null, log error and throw illegal argument
      // exception
      device = getDeviceManager().getDeviceByUniqueId(pDeviceUniqueId);
      if (device == null) {
        // If device is null, log error and return
        vlogError(MobileStoreConfiguration.sResourceBundle, "noDeviceForUniqueId", pDeviceUniqueId);
        vlogError(MobileStoreConfiguration.sResourceBundle, "pusnNotificationNotSent", pDeviceUniqueId);
        return;
      }
    } catch (RepositoryException e) {
      // Propagate the RepositoryException
      throw new PushNotificationException(e);
    }

    // Get push service using the device OS
    String deviceOS = (String) device.getPropertyValue(getDeviceManager()
                                                       .getPropertyManager()
                                                       .getOsPropertyName());
    if (getPushServiceMap().containsKey(deviceOS)) {
      PushNotificationService devicePushManager = (PushNotificationService) this
                                                  .getPushServiceMap().get(deviceOS);
      devicePushManager.sendNotification(pDeviceUniqueId, pBody, pLocKey, pLink,
                                         pExtra);
    } else {
      vlogError(MobileStoreConfiguration.sResourceBundle, "noPushManager",
                pDeviceUniqueId, deviceOS);
    }
  }
}
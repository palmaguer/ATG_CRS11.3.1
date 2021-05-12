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
package atg.projects.store.mobile.device;

import java.sql.Timestamp;
import java.util.Calendar;

import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.projects.store.mobile.MobileStoreConfiguration;
import atg.projects.store.mobile.userprofiling.MobileStorePropertyManager;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.servlet.ServletUtil;

/**
 * Handles creating and updating mobileDevice item descriptor in the profile repository
 * 
 * @author gdoneil
 * @version $Change: 1536476 $$DateTime: 2018/04/13 08:11:14 $$Author: releng $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class DeviceManager extends GenericService {
  public static String CLASS_VERSION =
      "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/device/DeviceManager.java#3 $$Change: 1536476 $";

  //-------------------------------------
  // Properties
  //-------------------------------------
  private static RepositoryItem[] emptyRepositoryItemList;

  //--------- Property: profileRepository-----------
  protected MutableRepository mProfileRepository;

  /**
   * Returns the profile repository where devices are stored
   * @return the profileRepository
   */
  public MutableRepository getProfileRepository() {
    return mProfileRepository;
  }

  /**
   * Sets the profile repository where devices are stored
   * @param pProfileRepository the profileRepository to set
   */
  public void setProfileRepository(MutableRepository pProfileRepository) {
    mProfileRepository = pProfileRepository;
  }

  //-------------------------------------
  //--------- Property: propertyManager -
  protected MobileStorePropertyManager mPropertyManager;

  /**
   * @return the mPropertyManager
   */
  public MobileStorePropertyManager getPropertyManager() {
    return mPropertyManager;
  }

  /**
   * @param pPropertyManager the mPropertyManager to set
   */
  public void setPropertyManager(MobileStorePropertyManager pPropertyManager) {
    mPropertyManager = pPropertyManager;
  }

  //-------------------------------------
  // Query Methods
  //-------------------------------------
  /**
   * Returns the device with the provided unique id
   * @param pUniqueId the unique id of the device (not the repository id)
   * @return Returns the device with the provided unique id
   * @throws RepositoryException
   */
  public RepositoryItem getDeviceByUniqueId(String pUniqueId) throws RepositoryException {
    if (pUniqueId == null) {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "getDeviceNullIdError");
      return null;
    }
    RepositoryView view = getProfileRepository()
                          .getView(getPropertyManager()
                                  .getMobileDeviceItemDescriptorName());

    QueryBuilder qb = view.getQueryBuilder();

    // create query for unique id
    QueryExpression uuidParameterExpression = qb.createConstantQueryExpression(pUniqueId);
    QueryExpression uuidPropertyExpression = 
        qb.createPropertyQueryExpression(getPropertyManager().getUniqueIdPropertyName());
    Query uuidQuery = qb.createComparisonQuery(uuidPropertyExpression,
      uuidParameterExpression,
      QueryBuilder.EQUALS);

    // Execute query
    RepositoryItem[] results = view.executeQuery(uuidQuery);
    
    if (results == null || results.length == 0) {
      return null;
    }
    return results[0];
  }
  
  /**
   * Returns the device with the provided profile id
   * @param pProfileId the profile linked to the device (not the repository id)
   * @return Returns the device linked to the profil eid
   * @throws RepositoryException
   */
  public RepositoryItem[] getDevicesByUser(String pProfileId) throws RepositoryException {
    RepositoryView view = getProfileRepository()
                          .getView(getPropertyManager()
                                   .getMobileDeviceItemDescriptorName());

    QueryBuilder qb = view.getQueryBuilder();

    // create query for unique id
    QueryExpression profileParameterExpression = qb.createConstantQueryExpression(pProfileId);
    QueryExpression profilePropertyExpression = 
        qb.createPropertyQueryExpression(getPropertyManager().getProfilePropertyName());
    Query profileQuery = qb.createComparisonQuery(profilePropertyExpression,
                                                  profileParameterExpression,
                                                  QueryBuilder.EQUALS);

    // Execute query
    RepositoryItem[] results = view.executeQuery(profileQuery);
    
    if (results == null || results.length == 0) {
      return new RepositoryItem[0];
    }
    return results;
  }
  
  /**
   * Returns the devices with the push token provided
   * @param pPushToken the push token of the device mapped to the parameter
   * @return Returns the device with the provided push token. If there are no
   *     devices with the given push token, it returns null
   * @throws RepositoryException
   */
  public RepositoryItem getDeviceByPushToken(String pPushToken) throws RepositoryException {
    RepositoryView view = getProfileRepository().getView(getPropertyManager()
                                   .getMobileDeviceItemDescriptorName());

    QueryBuilder qb = view.getQueryBuilder();

    // create query for unique id
    QueryExpression pushTokenParameterExpression = qb.createConstantQueryExpression(pPushToken);
    QueryExpression pushTokenPropertyExpression = 
        qb.createPropertyQueryExpression(getPropertyManager().getPushTokenPropertyName());
    Query profileQuery = qb.createComparisonQuery(pushTokenPropertyExpression,
                                                  pushTokenParameterExpression,
                                                  QueryBuilder.EQUALS);

    // Execute query
    RepositoryItem[] results = view.executeQuery(profileQuery);
    
    if (results == null || results.length == 0) {
      return null;
    }
    if (results.length != 1) {
      vlogError(MobileStoreConfiguration.sResourceBundle, "multipleDevicesWithSamePushToken");
    }
    return results[0];
  }
  

    
  //-------------------------------------
  // Private Methods
  //-------------------------------------

  /**
   * Sets the properties of pDevice. Does not update repository after
   * @param pDevice
   * @param pUniqueId
   * @param pPushToken
   * @param pUserId
   * @param pOS
   * @param pOSVersion
   * @param pApp
   * @param pAppVersion
   * @param pRegisteredOn if null, won't update the registered on time
   * @param pSeenOn
   */
  private void setDeviceValues(MutableRepositoryItem pDevice, String pUniqueId, 
                               String pPushToken,             String pUserId, 
                               String pOS,                    String pOSVersion,
                               String pApp,                   String pAppVersion,
                               Timestamp pRegisteredOn,       Timestamp pSeenOn)
  {
    MobileStorePropertyManager propertyManager = getPropertyManager();

    // Set properties
    pDevice.setPropertyValue(propertyManager.getUniqueIdPropertyName(),     pUniqueId);
    pDevice.setPropertyValue(propertyManager.getOsPropertyName(),           pOS);
    pDevice.setPropertyValue(propertyManager.getOsVersionPropertyName(),    pOSVersion);
    pDevice.setPropertyValue(propertyManager.getAppPropertyName(),          pApp);
    pDevice.setPropertyValue(propertyManager.getAppVersionPropertyName(),   pAppVersion);
    pDevice.setPropertyValue(propertyManager.getPushTokenPropertyName(),    pPushToken);
    pDevice.setPropertyValue(propertyManager.getProfilePropertyName(),      pUserId);
    pDevice.setPropertyValue(propertyManager.getSeenOnPropertyName(),       pSeenOn);
    if (pRegisteredOn != null) {
      pDevice.setPropertyValue(propertyManager.getRegisteredOnPropertyName(), pRegisteredOn);
    }
    
  }
  
  //-------------------------------------
  // Repository-related Methods
  //-------------------------------------
  /**
   * Creates a device and stores it in the profileRepository. Returns the device
   * created.  If the device already exists, updates the device with the
   * parameters provided
   * 
   * @param pUniqueId the device's unique id
   * @param pPushToken the token used for pushing notifications to the device
   * @param pOS the operating system of the device - iOS, Android, Blackberry, etc
   * @param pOSVersion the OS version
   * @param pApp the application installed. e.g. iua, asa
   * @param pAppVersion the version of the application installe
   * @return the created or updated device
   * @throws RepositoryException
   */
  public MutableRepositoryItem createDevice(String pUniqueId,  String pPushToken,
                                            String pOS,        String pOSVersion, 
                                            String pApp,       String pAppVersion) 
      throws RepositoryException
  { 
    MobileStorePropertyManager propertyManager = getPropertyManager();

    MutableRepositoryItem device = null;
    // Check if a device with pUnique id or with the push token already exists in
    // the repository
    if (getDeviceByUniqueId(pUniqueId) == null 
        && (pPushToken == null || getDeviceByPushToken(pPushToken) == null))
    {
      // If not, create the device
      MutableRepository repository = getProfileRepository();
      device = repository.createItem(propertyManager.getMobileDeviceItemDescriptorName());
      
      // Fetch user id if not transient
      RepositoryItem user = ServletUtil.getCurrentUserProfile();
      String userId = null;
      if (!user.isTransient()) {
        userId = user.getRepositoryId();
      }
      
      // Get time
      Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
      // Set device values
      setDeviceValues(device, pUniqueId, pPushToken, userId, pOS, pOSVersion,
          pApp, pAppVersion, time, time);
      // Add to repository
      repository.addItem(device);
    } else {
      vlogError(MobileStoreConfiguration.sResourceBundle, "cannotCreateExistingDeviceError");
    }
    return device;
  }

  /**
   * Updates the device with unique id pUniqueId to have the provided parameters.
   * If the device doesn't exist, creates one. Returns the updated or created device
   * @param pDevice the device to update.  Must not be null
   * @param pUniqueId the device's unique id
   * @param pPushToken the token used for pushing notifications to the device
   * @param pOS the operating system of the device - iOS, Android, Blackberry, etc
   * @param pOSVersion the OS version
   * @param pApp the application installed. e.g. iua, asa
   * @param pAppVersion the version of the application installed
   * @return the device that has been updated or created
   * @throws RepositoryException
   */
  public MutableRepositoryItem updateDevice(RepositoryItem pDevice, 
                                            String pUniqueId, String pPushToken,
                                            String pOS,       String pOSVersion, 
                                            String pApp,      String pAppVersion)
      throws RepositoryException
  {
    if (pDevice == null) {
      vlogError(MobileStoreConfiguration.sResourceBundle, "updateNullDeviceError");
      return null;
    }

    // Get device for update
    MutableRepository repository = getProfileRepository();
    MutableRepositoryItem device = repository
        .getItemForUpdate(pDevice.getRepositoryId(), 
                          pDevice.getItemDescriptor().getItemDescriptorName());

    // Get current user id.  If it is transient, get old user id associated with
    // the device
    RepositoryItem user = ServletUtil.getCurrentUserProfile();
    String userId;
    if (user.isTransient()) {
      userId = (String)device.getPropertyValue(getPropertyManager().getProfilePropertyName());
    } else {
      userId = user.getRepositoryId();
    }

    Timestamp seenOnTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
    setDeviceValues(device,     pUniqueId, pPushToken,  userId, pOS, 
                    pOSVersion, pApp,      pAppVersion, null,   seenOnTime);
    repository.updateItem(device);
    return device;
  }
  
  /**
   * Creates or Updates the device with unique id pUniqueId to have the provided parameters.
   * If the device doesn't exist, creates one. Returns the updated or created device
   * @param pUniqueId the device's unique id
   * @param pPushToken the token used for pushing notifications to the device
   * @param pOS the operating system of the device - iOS, Android, Blackberry, etc
   * @param pOSVersion the OS version
   * @param pApp the application installed. e.g. iua, asa
   * @param pAppVersion the version of the application installed
   * @return the device that has been updated or created
   * @throws RepositoryException
   */
  public MutableRepositoryItem createOrUpdateDevice(String pUniqueId,  String pPushToken,
                                                    String pOS,        String pOSVersion, 
                                                    String pApp,       String pAppVersion) 
      throws RepositoryException 
  {
    // Get device from repository by unique id
    MutableRepositoryItem device = (MutableRepositoryItem)getDeviceByUniqueId(pUniqueId);
    if (device == null) {
      device = (MutableRepositoryItem)getDeviceByPushToken(pPushToken);
    }

    // If device not in the repository, create one
    if  (device == null) { 
      return createDevice(pUniqueId, pPushToken, pOS, pOSVersion, pApp, pAppVersion);
    } else {
      return updateDevice(device, pUniqueId, pPushToken, pOS, pOSVersion, pApp, pAppVersion);
    }
  }
  
  /**
   * Deletes a device from the profileRepository. 
   * 
   * @param pUniqueId the device's unique id
   * @throws RepositoryException
   */
  public void deleteDevice(String pUniqueId) throws RepositoryException { 
    if (!StringUtils.isBlank(pUniqueId)) {
      RepositoryItem device = getDeviceByUniqueId(pUniqueId);
      if (device != null) {
        getProfileRepository().removeItem(device.getRepositoryId(), 
            getPropertyManager().getMobileDeviceItemDescriptorName());
      } else {
        vlogError(MobileStoreConfiguration.sResourceBundle, "noDeviceForUniqueId",
            pUniqueId);
      }
    } else {
      vlogError(MobileStoreConfiguration.sResourceBundle, "cannotDeleteNullDevice");
    }
  }
  
  /**
   * Sets the device token of the device with unique id pUniqueId. Returns the
   * device if the device already exists in the repository or null if the device
   * can't be found
   * @param pUniqueId the unique id of the device that should have its push token set
   * @param pPushToken the push token to set
   * @return the Device if the device already exists in the repository or null
   * if the device cant be found
   * @throws RepositoryException
   */
  public MutableRepositoryItem setDevicePushToken(String pUniqueId, String pPushToken)
      throws RepositoryException 
  {
    MutableRepositoryItem device = (MutableRepositoryItem)getDeviceByUniqueId(pUniqueId);
    // If device doesn't exist, log error and return null
    if (device == null) {
      vlogError(MobileStoreConfiguration.sResourceBundle, "noDeviceForUniqueId", pUniqueId);
      vlogError(MobileStoreConfiguration.sResourceBundle, "pushTokenSetError", pUniqueId);
      return null;
    }

    // Get device for update
    MutableRepository repository = getProfileRepository();
    device = repository
             .getItemForUpdate(device.getRepositoryId(),
                               device.getItemDescriptor().getItemDescriptorName());
    
    // Get the property manager
    MobileStorePropertyManager propertyManager = getPropertyManager();
    // Set the push token value to the provided push token
    device.setPropertyValue(propertyManager.getPushTokenPropertyName(),  pPushToken);

    // Update the seen on time
    Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
    device.setPropertyValue(propertyManager.getSeenOnPropertyName(),       time);

    // Update the item in the repository
    repository.updateItem(device);

    return device;
  }

  /**
   * Updates the last seen timestamp to the current time for the given device and commits it to the repository.
   * @param pDeviceUID  The device's unique id.
   * @throws RepositoryException  If Repository throws an exception.
   * @throws IllegalArgumentException If the device UID argument is blank
   */
  public void updateLastSeenTimestamp(String pDeviceUID) throws RepositoryException {
    if(StringUtils.isBlank(pDeviceUID)) {
      vlogError(MobileStoreConfiguration.sResourceBundle, "emptyParameterError", pDeviceUID);
      return;
    }

    RepositoryItem deviceItem = getDeviceByUniqueId(pDeviceUID);
    if(deviceItem != null) {
      MutableRepositoryItem deviceItemToUpdate =
        getProfileRepository().getItemForUpdate(deviceItem.getRepositoryId(), getPropertyManager().getMobileDeviceItemDescriptorName());
      Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
      deviceItemToUpdate.setPropertyValue(getPropertyManager().getSeenOnPropertyName(), time);
      getProfileRepository().updateItem(deviceItemToUpdate);
    }
    else {
      vlogError(
        MobileStoreConfiguration.sResourceBundle, "itemNotFoundError",
        getPropertyManager().getMobileDeviceItemDescriptorName(), pDeviceUID
      );
    }
  }

  //-------------------------------------
  // Public Utility Methods
  //-------------------------------------

  /**
   * Returns the push token associated with the device with the provided unique
   * id. Returns null if the device does not exist or if there is no token
   * @param pUniqueId the device's unique id
   * @return the push token associated with the device with the provided unique
   * id. Returns null if the device does not exist or if there is no token
   */
  public String getPushTokenForUniqueId(String pUniqueId) {
    RepositoryItem device;
    try {
      device = getDeviceByUniqueId(pUniqueId);
    } catch (RepositoryException e) {
      return null;
    }
    if (device == null) {
      return null;
    }
    return (String)device.getPropertyValue(getPropertyManager()
                                          .getPushTokenPropertyName());
  }
  
  /**
   * Returns the operating system string associated with the provided unique id.
   * Returns null if the device does not exist
   * @param pUniqueId the device's unique id
   * @return the operating system string associated with the provided unique id.
   * Returns null if the device does not exist
   * @throws RepositoryException
   */
  public String getOsForUniqueId(String pUniqueId) {
    RepositoryItem device;
    try {
      device = getDeviceByUniqueId(pUniqueId);
    } catch (RepositoryException e) {
      return null;
    }
    if (device == null) {
      return null;
    }
    return (String)device.getPropertyValue(getPropertyManager()
                                           .getOsPropertyName());
  }

}

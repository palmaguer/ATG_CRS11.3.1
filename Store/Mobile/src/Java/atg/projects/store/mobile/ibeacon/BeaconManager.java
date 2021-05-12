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

import java.util.*;

import javax.servlet.http.HttpSession;

import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.projects.store.mobile.MobileStoreConfiguration;
import atg.projects.store.mobile.device.DeviceManager;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.ServletUtil;

/**
 * Component for interacting with iBeacons. Client code can call these methods to let the server know the state
 * of beacon ranging on the client and in turn, these methods will (if configured) send DMS events to inform any
 * listeners about ranging state.
 *
 * @see BeaconMessageSource
 *
 * @author Doug Kenyon
 * @version $Change: 1536476 $$DateTime: 2018/04/13 08:11:14 $$Author: releng $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class BeaconManager extends GenericService {

  public static String CLASS_VERSION =
    "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/ibeacon/BeaconManager.java#3 $$Change: 1536476 $";

  // static member to represent empty result set
  public final static RepositoryItem[] EMPTY_RESULT = new RepositoryItem[0];

  //-------------------------------------
  // property: locationRepository
  //
  // The repository where the beacon and related location items are stored.
  private Repository mLocationRepository;

  /**
   * Gets the location repository.
   * @return The location repository.
   */
  public Repository getLocationRepository() {
    return mLocationRepository;
  }

  /**
   * Sets the location repository.
   * @param pLocationRepository The location repository.
   */
  public void setLocationRepository(Repository pLocationRepository) {
    mLocationRepository = pLocationRepository;
  }

  //-------------------------------------
  // property: deviceManager
  //

  // The device manager component
  private DeviceManager mDeviceManager;

  /**
   * Gets the device manager component.
   * @return The device manager component.
   */
  public DeviceManager getDeviceManager() {
    return mDeviceManager;
  }

  /**
   * Sets the device manager component.
   * @param pDeviceManager The device manager component.
   */
  public void setDeviceManager(DeviceManager pDeviceManager) {
    mDeviceManager = pDeviceManager;
  }

  //-------------------------------------
  // item descriptor and property configuration
  //
  private String mBeaconItemDescriptorName = "ibeacon";
  private String mAlertItemDescriptorName = "ibeaconAlert";
  private String mStoreItemDescriptorName = "store";

  /**
   * Gets the name of the beacon item-descriptor.
   * @return  The beacon item-descriptor name.
   */
  public String getBeaconItemDescriptorName() {
    return mBeaconItemDescriptorName;
  }

  /**
   * Sets the name of the beacon item-descriptor.
   * @param mBeaconItemDescriptorName The name of the beacon item-descriptor.
   */
  public void setBeaconItemDescriptorName(String mBeaconItemDescriptorName) {
    this.mBeaconItemDescriptorName = mBeaconItemDescriptorName;
  }

  /**
   * Gets the name of the alert item-descriptor.
   * @return  The alert item-descriptor name.
   */
  public String getAlertItemDescriptorName() {
    return mAlertItemDescriptorName;
  }

  /**
   * Sets the name of the alert item-descriptor.
   * @param mAlertItemDescriptorName The name of the alert item-descriptor.
   */
  public void setAlertItemDescriptorName(String mAlertItemDescriptorName) {
    this.mAlertItemDescriptorName = mAlertItemDescriptorName;
  }

  /**
   * Gets the name of the store item-descriptor.
   * @return  The store item-descriptor name.
   */
  public String getStoreItemDescriptorName() {
    return mStoreItemDescriptorName;
  }

  /**
   * Sets the name of the store item-descriptor.
   * @param pStoreItemDescriptorName The name of the store item-descriptor.
   */
  public void setStoreItemDescriptorName(String pStoreItemDescriptorName) {
    mStoreItemDescriptorName = pStoreItemDescriptorName;
  }

  private String mNamePropertyName="name";
  private String mUuidPropertyName="UUID";
  private String mMajorPropertyName="major";
  private String mMinorPropertyName="minor";
  private String mStorePropertyName="store";
  private String mEnterAlertsPropertyName="enterAlerts";
  private String mExitAlertsPropertyName="exitAlerts";

  /**
   * Gets the name of the Name property.
   * @return  The name of the Name property.
   */
  public String getNamePropertyName() {
    return mNamePropertyName;
  }

  /**
   * Sets the name of the Name property.
   * @param pNamePropertyName The name of the Name property.
   */
  public void setNamePropertyName(String pNamePropertyName) {
    mNamePropertyName = pNamePropertyName;
  }
  
  /**
   * Gets the name of the UUID property.
   * @return  The name of the UUID property.
   */
  public String getUuidPropertyName() {
    return mUuidPropertyName;
  }

  /**
   * Sets the name of the UUID property.
   * @param pUuidPropertyName The name of the UUID property.
   */
  public void setUuidPropertyName(String pUuidPropertyName) {
    mUuidPropertyName = pUuidPropertyName;
  }

  /**
   * Gets the name of the major property.
   * @return  The name of the major property.
   */
  public String getMajorPropertyName() {
    return mMajorPropertyName;
  }

  /**
   * Sets the name of the major property.
   * @param pMajorPropertyName The name of the major property.
   */
  public void setMajorPropertyName(String pMajorPropertyName) {
    mMajorPropertyName = pMajorPropertyName;
  }

  /**
   * Gets the name of the minor property.
   * @return  The name of the minor property.
   */
  public String getMinorPropertyName() {
    return mMinorPropertyName;
  }

  /**
   * Sets the name of the minor property.
   * @param pMinorPropertyName The name of the minor property.
   */
  public void setMinorPropertyName(String pMinorPropertyName) {
    mMinorPropertyName = pMinorPropertyName;
  }

  /**
   * Gets the name of the store property.
   * @return  The name of the store property.
   */
  public String getStorePropertyName() {
    return mStorePropertyName;
  }

  /**
   * Sets the name of the store property.
   * @param pStorePropertyName The name of the store property.
   */
  public void setStorePropertyName(String pStorePropertyName) {
    mStorePropertyName = pStorePropertyName;
  }

  /**
   * Gets the name of the enter alerts property.
   * @return  The name of the enter alerts property.
   */
  public String getEnterAlertsPropertyName() {
    return mEnterAlertsPropertyName;
  }

  /**
   * Sets the name of the enter alerts property.
   * @param pEnterAlertsPropertyName The name of the enter alerts property.
   */
  public void setEnterAlertsPropertyName(String pEnterAlertsPropertyName) {
    mEnterAlertsPropertyName = pEnterAlertsPropertyName;
  }

  /**
   * Gets the name of the exit alerts property.
   * @return  The name of the exit alerts property.
   */
  public String getExitAlertsPropertyName() {
    return mExitAlertsPropertyName;
  }

  /**
   * Sets the name of the exit alerts property.
   * @param pExitAlertsPropertyName The name of the exit alerts property.
   */
  public void setExitAlertsPropertyName(String pExitAlertsPropertyName) {
    mExitAlertsPropertyName = pExitAlertsPropertyName;
  }

  private String mTextPropertyName = "text";
  private String mTextKeyPropertyName = "textKey";
  private String mLinkUrl = "linkUrl";
  private String mIsPushNotificationPropertyName = "isPushNotification";

  /**
   * Gets the name of the text property.
   * @return  The name of the text property.
   */  
  public String getTextPropertyName() {
    return mTextPropertyName;
  }

  /**
   * Sets the name of the text property.
   * @param pTextPropertyName The name of the text property.
   */  
  public void setTextPropertyName(String pTextPropertyName) {
    mTextPropertyName = pTextPropertyName;
  }

  /**
   * Gets the name of the text key property.
   * @return  The name of the text key property.
   */
  public String getTextKeyPropertyName() {
    return mTextKeyPropertyName;
  }

  /**
   * Sets the name of the text key property.
   * @param pTextKeyPropertyName The name of the text key property.
   */
  public void setTextKeyPropertyName(String pTextKeyPropertyName) {
    mTextKeyPropertyName = pTextKeyPropertyName;
  }

  /**
   * Gets the name of the is push notification property.
   * @return  The name of the is push notification property.
   */
  public String getIsPushNotificationPropertyName() {
    return mIsPushNotificationPropertyName;
  }

  /**
   * Sets the name of the is push notification property.
   * @param pIsPushNotificationPropertyName The name of the is push notification property.
   */
  public void setIsPushNotificationPropertyName(String pIsPushNotificationPropertyName) {
    mIsPushNotificationPropertyName = pIsPushNotificationPropertyName;
  }  

  public String getLinkUrl() {
    return mLinkUrl;
  }

  public void setLinkUrl(String pLinkUrl) {
    mLinkUrl = pLinkUrl;
  }


  // ---------------------------------------------------------------------------
  // property: list of UUIDs used
  // --------------------------------------------------------------------------- 
  String[] UUIDs;

  /**
   * Gets the company UUID used for all in-store beacons
   * @return the company UUID used for all in-store beacons
   */
  public String[] getUUIDs() {
    return UUIDs;
  }

  /**
   * Sets the company UUID used for all in-store beacons
   * @param pCompanyUuid the company UUID used for all in-store beacons
   */
  public void setUUIDs(String[] pCompanyUuid) {
    this.UUIDs = pCompanyUuid;
  }

  // ---------------------------------------------------------------------------
  // property:messageSource
  // ---------------------------------------------------------------------------
  private BeaconMessageSource mMessageSource;

  /**
   * Gets the message source for sending scenario event messages.
   * @return  The message source.
   */
  public BeaconMessageSource getMessageSource() {
    return mMessageSource;
  }

  /**
   * Sets the message source for sending scenario event messages.
   * @param pMessageSource  The message source.
   */
  public void setMessageSource(BeaconMessageSource pMessageSource) {
    mMessageSource = pMessageSource;
  }


/**
  * Query Methods
  */

  /**
   * Gets the repository view for the beacon item-descriptor
   * @return  The RepositoryView item.
   * @throws RepositoryException
   */
  public RepositoryView getBeaconRepositoryView() throws RepositoryException {
    return getLocationRepository().getView(getBeaconItemDescriptorName());
  }

  /**
   * Gets a beacon item given the id.
   * @param pRepositoryId The repository id of the item to find.
   * @return  The beacon repository item or null if it's not found.
   * @throws RepositoryException
   */
  public RepositoryItem getBeaconById(String pRepositoryId) throws RepositoryException {
    return getLocationRepository().getItem(pRepositoryId, getBeaconItemDescriptorName());
  }

  /**
   * Gets one or more beacons given some information about the beacon. The UUID is required but the major and minor
   * fields are optional.
   * @param pUUID The UUID of the beacon
   * @param pMajor  The major value of the beacon
   * @param pMinor  The minor value of the beacon
   * @return  One or more beacons that match the parameters. If none are found an empty array is returned.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the UUID parameter is empty or null.
   */
  public RepositoryItem[] getBeacons(String pUUID, String pMajor, String pMinor)
    throws RepositoryException
  {
    if(StringUtils.isEmpty(pUUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pUUID")
      );
    }

    // do this as an array list and create an array from it at the end because the createAndQuery method
    // will not accept any null values in the given array.
    ArrayList<Query> queries = new ArrayList<Query>(3);
    int numQueries = 0;

    QueryBuilder qb = getBeaconRepositoryView().getQueryBuilder();

    // beacon.uuid = pUUID
    QueryExpression uuidParameterExpression = qb.createConstantQueryExpression(pUUID);
    QueryExpression uuidPropertyExpression = qb.createPropertyQueryExpression(getUuidPropertyName());
    queries.add(qb.createComparisonQuery(uuidPropertyExpression, uuidParameterExpression, QueryBuilder.EQUALS));

    // beacon.major = pMajor
    if(StringUtils.isNotBlank(pMajor)) {
      QueryExpression parameterExpression = qb.createConstantQueryExpression(pMajor);
      QueryExpression propertyExpression = qb.createPropertyQueryExpression(getMajorPropertyName());
      queries.add(qb.createComparisonQuery(parameterExpression, propertyExpression, QueryBuilder.EQUALS));
    }

    // beacon.minor = pMinor
    if(StringUtils.isNotBlank(pMinor)) {
      QueryExpression parameterExpression = qb.createConstantQueryExpression(pMinor);
      QueryExpression propertyExpression = qb.createPropertyQueryExpression(getMinorPropertyName());
      queries.add(qb.createComparisonQuery(parameterExpression, propertyExpression, QueryBuilder.EQUALS));
    }

    // Put all the clauses together as an and query and execute it
    Query[] queryArray = queries.toArray(new Query[numQueries]); // can only be sized to exact number of clauses
    RepositoryItem[] results = getBeaconRepositoryView().executeQuery(qb.createAndQuery(queryArray));
    return results == null ? EMPTY_RESULT : results; // be friendly
  }

  /**
   * Convenience method to get the store the given beacon belongs to. The UUID is required but the major and minor
   * fields are optional.
   * @param pUUID The UUID of the beacon
   * @param pMajor  The major value of the beacon
   * @param pMinor  The minor value of the beacon
   * @return  The store item for the first beacon that matches the query. If none is found null is returned.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the UUID parameter is empty or null.
   */
  public RepositoryItem getStoreForBeacon(String pUUID, String pMajor, String pMinor) throws RepositoryException {
    if(StringUtils.isEmpty(pUUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pUUID")
      );
    }

    RepositoryItem[] beacons = getBeacons(pUUID, pMajor, pMinor);
    if(beacons.length == 0) {
      vlogError(MobileStoreConfiguration.sResourceBundle, "noBeaconItemsFound", pUUID, pMajor, pMinor);
      return null;
    }

    Object storePropertyValue = beacons[0].getPropertyValue(getStorePropertyName());
    return (RepositoryItem)storePropertyValue;
  }

  /**
   * Enter the range of the given beacons as defined by the query parameters. This will notify any listeners.
   * @param pDeviceUID The unique id of the device that interacted with the ibeacon.
   * @param pUUID The UUID of the beacon
   * @param pMajor  The major value of the beacon
   * @param pMinor  The minor value of the beacon
   * @return  One or more beacons that match the parameters. If none are found an empty array is returned.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the given deviceUID or UUID is empty or null.
   */
  public RepositoryItem[] enterBeaconRange(String pDeviceUID, String pUUID, String pMajor, String pMinor)
    throws RepositoryException{
    List<Beacon> beaconList = new ArrayList<Beacon>(1);
    beaconList.add(new Beacon(pUUID, pMajor, pMinor));
    return enterBeaconRange(pDeviceUID, beaconList);
  }

  /**
   * Enter the range of the given list of beacons for the given device.
   * 
   * @param pDeviceUID  The unique id of the device.
   * @param pBeacons  The List of beacons either as Beacon object or a Map of UUID/major/minor values.
   * @return An array of ibeacon items returned by the underlying queries or an empty array if none are found.
   * @throws RepositoryException If a repository error occurs when updating the timestamp for the device.
   * @throws IllegalArgumentException If the given deviceUID is empty/null or the given list of beacons is empty/null.
   */
  public RepositoryItem[] enterBeaconRange(String pDeviceUID, List pBeacons) throws RepositoryException {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    if(pBeacons == null || pBeacons.isEmpty()) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pBeacons")
      );
    }

    RepositoryItem[] beaconItems = getBeaconItemsFromList(pBeacons);
    if(getMessageSource() != null) {
      for (RepositoryItem beaconItem : beaconItems) {
        String beaconName = (String)beaconItem.getPropertyValue(getNamePropertyName());
        getMessageSource().fireEnteredBeacon(
          pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId(), beaconItem.getRepositoryId(), beaconName
        );
      }
    }
    else {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "EnterBeaconRange");
    }

    updateDeviceLastSeen(pDeviceUID);

    return beaconItems;
  }

  /**
   * Exit the range of the given beacons as defined by the query parameters. This will notify any listeners.
   * @param pDeviceUID The unique id of the device that interacted with the ibeacon.
   * @param pUUID The UUID of the beacon
   * @param pMajor  The major value of the beacon
   * @param pMinor  The minor value of the beacon
   * @return  One or more beacons that match the parameters. If none are found an empty array is returned.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the deviceUID or UUID parameter is empty or null.
   */
  public RepositoryItem[] exitBeaconRange(String pDeviceUID, String pUUID, String pMajor, String pMinor)
    throws RepositoryException
  {
    List<Beacon> beaconList = new ArrayList<Beacon>(1);
    beaconList.add(new Beacon(pUUID, pMajor, pMinor));
    return exitBeaconRange(pDeviceUID, beaconList);
  }

  /**
   * Exit the range of the given list of beacons for the given device.
   * @param pDeviceUID  The unique id of the device.
   * @param pBeacons  The List of beacons either as Beacon object or a Map of UUID/major/minor values.
   * @return  An array of ibeacon items returned by the underlying queries or an empty array if none are found.
   * @throws RepositoryException
   * @throws IllegalArgumentException If either of the given parameters or empty or null.
   */
  public RepositoryItem[] exitBeaconRange(String pDeviceUID, List pBeacons) throws RepositoryException {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    if(pBeacons == null || pBeacons.isEmpty()) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pBeacons")
      );
    }

    RepositoryItem[] beaconItems = getBeaconItemsFromList(pBeacons);
    if(getMessageSource() != null) {
      for (RepositoryItem beaconItem : beaconItems) {
        getMessageSource().fireExitedBeacon(
          pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId(), beaconItem.getRepositoryId()
        );
      }
    }
    else {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "ExitBeaconRange");
    }

    updateDeviceLastSeen(pDeviceUID);

    return beaconItems;
  }
  
  /**
   * Enter the store.
   * @param pDeviceUID The unique id of the device that enter the store.
   * @param pStoreId  The repository id of the store.
   * @throws IllegalArgumentException If either of the given parameters or empty or null.
   */
  public RepositoryItem enterStore(String pDeviceUID, String pStoreId) throws RepositoryException {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    if(StringUtils.isEmpty(pStoreId)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pStoreId")
      );
    }

    RepositoryItem store = getLocationRepository().getItem(pStoreId, getStoreItemDescriptorName());
    if (getMessageSource() != null) {
      if(store != null) {
        getMessageSource().fireEnteredStore(pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId(), store.getRepositoryId());
      }
      else {
        vlogError(MobileStoreConfiguration.sResourceBundle, "noStoreItemFound", pStoreId, "EnteredStore");
      }
    }
    else {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "EnteredStore");
    }

    return store;
  }

  /**
   * Exit the store.
   * @param pDeviceUID The unique id of the device that exit the store.
   * @param pStoreId  The repository id of the store.
   * @throws IllegalArgumentException If either of the given parameters or empty or null.
   */
  public RepositoryItem exitStore(String pDeviceUID, String pStoreId) throws RepositoryException {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    if(StringUtils.isEmpty(pStoreId)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pStoreId")
      );
    }

    RepositoryItem store = getLocationRepository().getItem(pStoreId, getStoreItemDescriptorName());
    if (getMessageSource() != null) {
      if(store != null) {
        getMessageSource().fireExitedStore(pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId(), store.getRepositoryId());
      }
      else {
        vlogError(MobileStoreConfiguration.sResourceBundle, "noStoreItemFound", pStoreId, "ExitedStore");
      }
    }
    else {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "ExitStore");
    }

    return store;
  }

  /**
   * Initiate a help request on a device.
   * @param pDeviceUID The unique id of the device that initiated the request.
   * @param pDeviceUID The unique id of the device that initiated the request.
   * @param pUUID The UUID of the nearest beacon
   * @param pMajor  The major value of the nearest beacon
   * @param pMinor  The minor value of the nearest beacon
   * @param pExtraParams  Extra form parameters submitted by the device (e.g. firstName/lastName)
   * @return  An array of ibeacon items returned by the underlying queries or an empty array if none are found.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the given deviceUID or UUID is empty or null.
   */
  public RepositoryItem[] requestHelp(String pDeviceUID, String pUUID, String pMajor, String pMinor, Map pExtraParams)
    throws RepositoryException
  {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    RepositoryItem[] beaconItems = getBeacons(pUUID, pMajor, pMinor);
    if(getMessageSource() != null) {
      // even if somehow there are no beacons, still send the message with safe parameters
      RepositoryItem beacon = beaconItems.length > 0 ? beaconItems[0] : null;
      String beaconId = beacon != null ? beacon.getRepositoryId() : null ;
      String beaconName = beacon != null ? (String)beacon.getPropertyValue(getNamePropertyName()) : "";

      getMessageSource().fireRequestedHelp(
        pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId(), beaconId, beaconName, pExtraParams);
    }
    else {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "RequestedHelp");
    }

    updateDeviceLastSeen(pDeviceUID);

    return beaconItems;
  }

  /**
   * User cancelled help request.
   * @param pDeviceUID The unique id of the device that exit the store.
   * @throws RepositoryException If a repository error occurs when updating the timestamp for the device.
   * @throws IllegalArgumentException If the given deviceUID is empty or null.
   */
  public void cancelHelp(String pDeviceUID) throws RepositoryException {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    if (getMessageSource() != null) {
      getMessageSource().fireCancelledHelp(pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId());
    }
    else {
      vlogWarning(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "CancelledHelp");
    }

    updateDeviceLastSeen(pDeviceUID);

  }

  /**
   * Update the range of the given beacons as defined by the query parameters. This will notify any listeners.
   * @param pDeviceUID The unique id of the device that interacted with the ibeacon.
   * @param pUUID The UUID of the beacon
   * @param pMajor  The major value of the beacon
   * @param pMinor  The minor value of the beacon
   * @return  One or more beacons that match the parameters. If none are found an empty array is returned.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the given deviceUID or UUID is empty or null.
   */
  public RepositoryItem[] updateBeaconRange(String pDeviceUID, String pUUID, String pMajor, String pMinor)
    throws RepositoryException
  {
    List<Beacon> beaconList = new ArrayList<Beacon>(1);
    beaconList.add(new Beacon(pUUID, pMajor, pMinor));
    return updateBeaconRange(pDeviceUID, beaconList);
  }

  /**
   * Update the range of the given list of beacons for the given device.
   * @param pDeviceUID  The unique id of the device.
   * @param pBeacons  The List of beacons either as Beacon object or a Map of UUID/major/minor values.
   * @return  An array of ibeacon items returned by the underlying queries or an empty array if none are found.
   * @throws RepositoryException
   * @throws IllegalArgumentException If the given deviceUID or list of beacon items is empty or null.
   */
  public RepositoryItem[] updateBeaconRange(String pDeviceUID, List pBeacons)
    throws RepositoryException
  {
    if(StringUtils.isEmpty(pDeviceUID)) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pDeviceUID")
      );
    }

    if(pBeacons == null) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pBeacons")
      );
    }

    // no-op if no beacons are being ranged
    // this is the state where all beacon ranges are exited and remain exited.
    if(pBeacons.isEmpty()) {
      return EMPTY_RESULT;
    }

    RepositoryItem[] beaconItems = getBeaconItemsFromList(pBeacons);
    if(getMessageSource() != null) {
      for (RepositoryItem beaconItem : beaconItems) {
        String beaconName = (String)beaconItem.getPropertyValue(getNamePropertyName());
        getMessageSource().fireUpdatedBeaconRange(
          pDeviceUID, getCurrentUserProfileId(), getCurrentSessionId(), beaconItem.getRepositoryId(), beaconName
        );
      }
    }
    else {
      vlogInfo(MobileStoreConfiguration.sResourceBundle, "noMessageSource", "UpdatedBeaconRange");
    }

    updateDeviceLastSeen(pDeviceUID);

    return beaconItems;
  }
  
  /**
   * Gets the repository id of the current user profile. If there is no current user profile null is returned.
   * @return  The repositoryId of the current user profile.
   */
  protected String getCurrentUserProfileId() {
    RepositoryItem profile = ServletUtil.getCurrentUserProfile();
    return profile == null ? null : profile.getRepositoryId();
  }

  /**
   * Gets the id of the current session. If there is no current request or session then null is returned.
   * @return  The id of the current session.
   */
  protected String getCurrentSessionId() {
    DynamoHttpServletRequest currentRequest = ServletUtil.getCurrentRequest();
    if(currentRequest != null) {
      HttpSession currentSession = currentRequest.getSession();
      if(currentSession != null) {
        return currentRequest.getSession().getId();
      }
    }
    return null;
  }

  /**
   * Updates the last seen timestamp on the given device.
   * @param pDeviceUID  The uid of the device to update.
   */
  protected void updateDeviceLastSeen(String pDeviceUID) throws RepositoryException {
    if(getDeviceManager() != null) {
      getDeviceManager().updateLastSeenTimestamp(pDeviceUID);
    }
    else {
      vlogWarning("No DeviceManager is configured. Unable to update lastSeen timestamp for device {0}", pDeviceUID);
    }
  }

  /**
   * Given the list of either beacon object or maps representing beacon objects, returning the corresponding list
   * of beacon repository items.
   * @param pBeaconList The non-null, non-empty list of objects representing a beacon.
   * @return  A list of beacon repository items or an empty list if none are found.
   * @throws RepositoryException If an repository exception occurs querying the repository
   * @throws IllegalArgumentException If the parameter passed is empty or null or the list contains something other than
   * a Map or Beacon object.
   */
  protected RepositoryItem[] getBeaconItemsFromList(List pBeaconList) throws RepositoryException {
    if(pBeaconList == null || pBeaconList.isEmpty()) {
      throw new IllegalArgumentException(
        ResourceUtils.getMsgResource(
          "emptyParameterError",
          MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
          "pBeaconList")
      );
    }

    List<RepositoryItem> retVal = new ArrayList<RepositoryItem>();
    for (Object beacon : pBeaconList) {
      Beacon beaconObj;
      if(beacon instanceof Map) {
        beaconObj = new Beacon((Map)beacon);
      }
      else if(beacon instanceof Beacon) {
        beaconObj = (Beacon)beacon;
      }
      else {
        throw new IllegalArgumentException(
          ResourceUtils.getMsgResource(
            "invalidCollectionItemType",
            MobileStoreConfiguration.RESOURCE_BUNDLE_NAME, MobileStoreConfiguration.sResourceBundle,
            "pBeaconList", beacon.getClass().getName())
        );
      }

      RepositoryItem[] beaconItems = getBeacons(beaconObj.getUUID(), beaconObj.getMajor(), beaconObj.getMinor());
      if(beaconItems.length > 0) {
        retVal.addAll(Arrays.asList(beaconItems));
      }
      else {
        vlogError(MobileStoreConfiguration.sResourceBundle, "noBeaconItemsFoundObj", beaconObj);
      }
    }

    // convert the result set to an array
    return retVal.toArray(new RepositoryItem[retVal.size()]);
  }
}

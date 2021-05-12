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


package atg.projects.store.mobile.device.csr;

import atg.nucleus.GenericService;
import atg.projects.store.mobile.userprofiling.csr.MobileCSRStorePropertyManager;
import atg.repository.MutableRepository;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.repository.MutableRepositoryItem;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Component that handles creating and updating the inStoreUsers item descriptor in the profile repository
 *
 * @author pmacrory
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/device/csr/DeviceLocationManager.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class DeviceLocationManager extends GenericService {

  /** Class version string */
  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/device/csr/DeviceLocationManager.java#3 $$Change: 1536476 $";

  private String mBeaconItemDescriptorName;
  private String mStoreItemDescriptorName;
  private String mStorePropertyName;
  private int mQueryTimeInMinutes;
  private MutableRepository mProfileRepository;
  private Repository mLocationRepository;
  private MobileCSRStorePropertyManager mPropertyManager;
  private String mCancelHelpRequestQuery;
  private String mInStoreQuery;
  private String mHelpRequestQuery;
  private String mFindUserAtStoreQuery;
  private String mHelpRequestCountQuery;

  /**
   *gets the name of the iBeacon item descriptor
   * @return
   */
  public String getBeaconItemDescriptorName() {
    return mBeaconItemDescriptorName;
  }

  /**
   * sets the name of the iBeacon item descriptor
   * @param pBeaconItemDescriptorName
   */
  public void setBeaconItemDescriptorName(String pBeaconItemDescriptorName) {
    mBeaconItemDescriptorName = pBeaconItemDescriptorName;
  }

  /**
   * gets the name of the Store item descriptor
   * @return
   */
  public String getStoreItemDescriptorName() {
    return mStoreItemDescriptorName;
  }

  /**
   * sets the name of the Store item descriptor
   * @param pStoreItemDescriptorName
   */
  public void setStoreItemDescriptorName(String pStoreItemDescriptorName) {
    mStoreItemDescriptorName = pStoreItemDescriptorName;
  }

  /**
   * gets the property name of the store on the iBeacon
   * @return
   */
  public String getStorePropertyName() {
    return mStorePropertyName;
  }

  /**
   * sets the property name of the store on the iBeacon
   * @param pStorePropertyName
   */
  public void setStorePropertyName(String pStorePropertyName) {
    mStorePropertyName = pStorePropertyName;
  }

  /**
   * gets the query RQL for finding the help request to cancel
   * @return
   */
  public String getCancelHelpRequestQuery() {
    return mCancelHelpRequestQuery;
  }

  /**
   * sets the query RQL for finding the help request to cancel
   * @param pCancelHelpRequestQuery
   */
  public void setCancelHelpRequestQuery(String pCancelHelpRequestQuery) {
    mCancelHelpRequestQuery = pCancelHelpRequestQuery;
  }

  /**
   * gets the query RQL for finding the users currently in-store
   * @return
   */
  public String getInStoreQuery() {
    return mInStoreQuery;
  }

  /**
   * sets the query RQL for finding the users currently in-store
   * @param pInStoreQuery
   */
  public void setInStoreQuery(String pInStoreQuery) {
    mInStoreQuery = pInStoreQuery;
  }

  /**
   * gets the query RQL for finding the users requesting help
   * @return
   */
  public String getHelpRequestQuery() {
    return mHelpRequestQuery;
  }


  /**
   * gets the query RQL for finding a user in a store
   * @return
   */
  public String getFindUserAtStoreQuery() {
    return mFindUserAtStoreQuery;
  }

  /**
   * sets the query RQL for finding a user in a store
   * @param pFindUserAtStoreQuery
   */
  public void setFindUserAtStoreQuery(String pFindUserAtStoreQuery) {
    this.mFindUserAtStoreQuery = pFindUserAtStoreQuery;
  }

  /**
   * sets the query RQL for finding the users requesting help
   * @param pHelpRequestQuery
   */
  public void setHelpRequestQuery(String pHelpRequestQuery) {
    mHelpRequestQuery = pHelpRequestQuery;
  }

  /**
   * gets the query for the number of users who have requested help
   * @return
   */
  public String getHelpRequestCountQuery() {
    return mHelpRequestCountQuery;
  }

  /**
   * sets the query for the number of users who have requested help
   * @param pHelpRequestCountQuery
   */
  public void setHelpRequestCountQuery(String pHelpRequestCountQuery) {
    this.mHelpRequestCountQuery = pHelpRequestCountQuery;
  }

  /**
   * gets the duration of time in minutes that a shopper is deemed to be in-store if unseen for this duration
   * @return duration in minutes
   */
  public int getQueryTimeInMinutes() {
    return mQueryTimeInMinutes;
  }

  /**
   * sets the duration of time in minutes that a shopper is deemed to be in-store if unseen for this duration
   * @param pQueryTimeInMinutes duration in minutes
   */
  public void setQueryTimeInMinutes(int pQueryTimeInMinutes) {
    mQueryTimeInMinutes = pQueryTimeInMinutes;
  }

  /**
   * Returns the profile repository where users are stored
   * @return the profileRepository
   */
  public MutableRepository getProfileRepository() {
    return mProfileRepository;
  }

  /**
   * Sets the profile repository where users are stored
   * @param pProfileRepository the profileRepository to set
   */
  public void setProfileRepository(MutableRepository pProfileRepository) {
    mProfileRepository = pProfileRepository;
  }

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

  /**
   * Gets the property manager
   * @return the property manager
   */
  public MobileCSRStorePropertyManager getPropertyManager() {
    return mPropertyManager;
  }

  /**
   * sets the property manager
   * @param pPropertyManager the property manager to set
   */
  public void setPropertyManager(MobileCSRStorePropertyManager pPropertyManager) {
    mPropertyManager = pPropertyManager;
  }

  /**
   * returns the current time in milliseconds
   * @return time in milliseconds
   */
  protected Timestamp currentTime(){
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * The duration of time as a TimeStamp that a shopper is deemed to be still in-store, if inactive for this duration
   * @return TimeStamp indication the time limit of the query
   */
  private Timestamp queryTimeLimit(){
    //Query time window
    return new Timestamp(System.currentTimeMillis() - (1000 * 60 * getQueryTimeInMinutes()));
  }

  /**
   * Retrieves the store repository item that represents the store with specified id
   * @param pStoreId the repository id for the store
   * @return the store repository item
   * @throws RepositoryException
   */
  protected RepositoryItem storeFromStoreId(String pStoreId) throws RepositoryException {
    return getLocationRepository().getItem(pStoreId, getStoreItemDescriptorName());
  }

  /**
   * Retrieves the store repository item that is associated with the specified beacon
   * @param pBeaconId the id of the beacon
   * @return the store repository item
   * @throws RepositoryException
   */
  protected RepositoryItem storeFromBeaconId(String pBeaconId) throws RepositoryException {
    RepositoryItem beacon = getLocationRepository().getItem(pBeaconId, getBeaconItemDescriptorName());
    return (RepositoryItem)beacon.getPropertyValue(getStorePropertyName());
  }

  /**
   * Retrieves the store repository item id for the specified beacon
   * @param pBeacon the beacon repository item
   * @return the store repository id
   */
  protected String storeIdFromBeacon(RepositoryItem pBeacon){
    RepositoryItem store = (RepositoryItem) pBeacon.getPropertyValue(getStorePropertyName());
    return store.getRepositoryId();
  }

  /**
   * Retrieves the Repository item for the specified beacon repository id
   * @param pBeaconId the repository id of the beacon
   * @return the Repository item for the beacon
   * @throws RepositoryException
   */
  protected RepositoryItem beaconFromBeaconId(String pBeaconId) throws RepositoryException {
    return getLocationRepository().getItem(pBeaconId, getBeaconItemDescriptorName());
  }

  /**
   * Creates a new Repository item
   * @return the new RepositoryItem
   * @throws RepositoryException
   */
  protected MutableRepositoryItem createItem() throws RepositoryException {
    MutableRepository inStoreDeviceRepository = getProfileRepository();
    return inStoreDeviceRepository.createItem(getPropertyManager().getInStoreUsersItemDescriptorName());
  }

  /**
   * Retrieves from the repository, the item for the specified user and store
   * @param pProfileId the id of the user
   * @param pStoreId the id of the store
   * @return the RepositoryItem
   * @throws RepositoryException
   */
  protected MutableRepositoryItem existingItem(String pProfileId, String pStoreId) throws RepositoryException {
    //Query the repository
    MutableRepository inStoreDeviceRepository = getProfileRepository();

    RepositoryView view = inStoreDeviceRepository.getView(getPropertyManager().getInStoreUsersItemDescriptorName());
    RqlStatement statement =
      RqlStatement.parseRqlStatement(getFindUserAtStoreQuery());
    Object[] params = new Object[]{pProfileId, pStoreId};

    RepositoryItem[] foundItems = statement.executeQuery(view, params);
    if (foundItems != null && foundItems.length > 0){
      String id = foundItems[0].getRepositoryId();
      return inStoreDeviceRepository.getItemForUpdate(id,
        getPropertyManager().getInStoreUsersItemDescriptorName());
    }
    return null;
  }

  /**
   * Performs updates on the repository when a shopper enters the store
   * @param pProfileId the id of the shopper who entered the store
   * @param pStoreId the id of the store that the shopper entered
   */
  public void enteredStore(String pProfileId, String pStoreId){
    //get the repository
    MutableRepository inStoreDeviceRepository = getProfileRepository();

    try {
      MutableRepositoryItem existingItem = existingItem(pProfileId, pStoreId);
      if(existingItem != null){

        existingItem.setPropertyValue(getPropertyManager().getBeaconPropertyName(), null);
        existingItem.setPropertyValue(getPropertyManager().getLastSeenPropertyName(), currentTime());

        inStoreDeviceRepository.updateItem(existingItem);
      }
      else {
        MutableRepositoryItem inStoreDevice = createItem();

        //set properties
        inStoreDevice.setPropertyValue(getPropertyManager().getUserPropertyName(), pProfileId);
        inStoreDevice.setPropertyValue(getPropertyManager().getStorePropertyName(), pStoreId);
        inStoreDevice.setPropertyValue(getPropertyManager().getLastSeenPropertyName(), currentTime());

        inStoreDeviceRepository.addItem(inStoreDevice);
      }
    } catch (RepositoryException e) {
      if(isLoggingError()){
        logError(e);
      }
    }
  }

  /**
   * Performs updates on the repository when a shopper leaves the store
   * @param pProfileId the id of the shopper who left the store
   * @param pStoreId the id of the store that the shopper exited
   */
  public void exitedStore(String pProfileId, String pStoreId){
    //Delete record
    try {
      RepositoryItem record = existingItem(pProfileId, pStoreId);

      if(record != null) {
        MutableRepository inStoreDeviceRepository = getProfileRepository();

        inStoreDeviceRepository.removeItem(record.getRepositoryId(),
          getPropertyManager().getInStoreUsersItemDescriptorName());
      }

    } catch (RepositoryException e) {
      if(isLoggingError()){
        logError(e);
      }
    }

  }

  /**
   * Performs updates on the repository when a shopper encounters a new beacon
   * @param pProfileId the id of the shopper
   * @param pBeaconId the id of the beacon
   */
  public void enteredBeacon(String pProfileId, String pBeaconId){
    updatedBeaconRange(pProfileId, pBeaconId);
  }

  /**
   * Performs updates on the repository when a shopper moves within the store and the beacon ranges values are updated
   * @param pProfileId the id of the shopper
   * @param pBeaconId the id of the beacon
   */
  public void updatedBeaconRange(String pProfileId, String pBeaconId){
    //get the beacon info
    RepositoryItem beacon;
    try {
      beacon = beaconFromBeaconId(pBeaconId);
      String storeId = storeFromBeaconId(pBeaconId).getRepositoryId();

      if(beacon != null) {
        MutableRepository inStoreDeviceRepository = getProfileRepository();

        MutableRepositoryItem item = existingItem(pProfileId, storeIdFromBeacon(beacon));

        if(item != null){
          item.setPropertyValue(getPropertyManager().getBeaconPropertyName(), pBeaconId);
          item.setPropertyValue(getPropertyManager().getLastSeenPropertyName(), currentTime());

          //update the repository
          inStoreDeviceRepository.updateItem(item);
        }
        else{
          //add
          item = createItem();

          item.setPropertyValue(getPropertyManager().getUserPropertyName(), pProfileId);
          item.setPropertyValue(getPropertyManager().getStorePropertyName(), storeId);
          item.setPropertyValue(getPropertyManager().getBeaconPropertyName(), pBeaconId);
          item.setPropertyValue(getPropertyManager().getLastSeenPropertyName(), currentTime());

          //add to the repository
          inStoreDeviceRepository.addItem(item);
        }
      }

    } catch (RepositoryException e) {
      if(isLoggingError()){
        logError(e);
      }
    }
  }

  /**
   * Called when a beacon has left the range of a user
   * @param pProfileId the user id of the user who has left the beacon
   * @param pBeaconId the id of the beacon that is no longer in range
   */
  public void exitedBeacon(String pProfileId, String pBeaconId) {
    updatedBeaconRange(pProfileId, pBeaconId);
  }

  /**
   * Requests help for a user at a beacon
   * @param pProfileId the id of the user who wants to request help
   * @param pBeaconId the id of the beacon they are closest to, at the point of the request
   * @param pExtraParams a map of additional parameters, expected to contain username key
   */
  public void requestedHelp(String pProfileId, String pBeaconId, Map pExtraParams) {
    MutableRepository inStoreDeviceRepository = getProfileRepository();

    //query for the store
    RepositoryItem store;
    try {
      store = storeFromBeaconId(pBeaconId);

      MutableRepositoryItem editedItem;
      MutableRepositoryItem existingItem = existingItem(pProfileId, store.getRepositoryId());

      if(existingItem == null){
        editedItem = createItem();
        editedItem.setPropertyValue(getPropertyManager().getUserPropertyName(), pProfileId);
        editedItem.setPropertyValue(getPropertyManager().getStorePropertyName(), store.getRepositoryId());
      }
      else editedItem = existingItem;

      Timestamp time = currentTime();
      editedItem.setPropertyValue(getPropertyManager().getBeaconPropertyName(), pBeaconId);
      editedItem.setPropertyValue(getPropertyManager().getLastSeenPropertyName(), time);

      editedItem.setPropertyValue(getPropertyManager().getHelpRequestTimePropertyName(), time);
      editedItem.setPropertyValue(getPropertyManager().getHelpRequestedPropertyName(), true);

      if(pExtraParams.containsKey("username")) {
        String chosenUserName = (String) pExtraParams.get("username");

        editedItem.setPropertyValue(getPropertyManager().getUserDisplayNamePropertyName(), chosenUserName);
      }

      if(existingItem == null) inStoreDeviceRepository.addItem(editedItem);
      else inStoreDeviceRepository.updateItem(editedItem);


    } catch (RepositoryException e) {
      if(isLoggingError()){
        logError(e);
      }
    }
  }

  /**
   * Cancels a help request for the specified user
   * @param pProfileId the id of the user who wants to cancel a help request
   */
  public void cancelledHelpRequest(String pProfileId){
    //remove the cancelled flag
    MutableRepository inStoreDeviceRepository = getProfileRepository();
    try {
      RepositoryView view = inStoreDeviceRepository.getView(getPropertyManager().getInStoreUsersItemDescriptorName());
      RqlStatement statement =
        RqlStatement.parseRqlStatement(getCancelHelpRequestQuery());
      Object[] params = new Object[]{pProfileId, true};

      RepositoryItem[] foundItems = statement.executeQuery(view, params);

      if(foundItems.length > 0){

        MutableRepositoryItem editableItem = inStoreDeviceRepository.getItemForUpdate(foundItems[0].getRepositoryId(),
          getPropertyManager().getInStoreUsersItemDescriptorName());

        clearHelpRequestFromExistingItem(editableItem, inStoreDeviceRepository);
      }
    }
    catch (RepositoryException e) {
      if(isLoggingError()){
        logError(e);
      }
    }
  }

  /**
   * Finds and returns the in-store shoppers for the specified store
   * @param pStoreId the repsository id for the store
   * @return An array of in-store user repository items
   * @throws RepositoryException
   */
  public RepositoryItem[] usersInStore(String pStoreId) throws RepositoryException {
    RepositoryView view = getProfileRepository().getView(getPropertyManager().getInStoreUsersItemDescriptorName());

    RqlStatement statement = RqlStatement.parseRqlStatement(getInStoreQuery());

    //current time minus twenty minutes (default, can be changed in the properties file)
    Timestamp timestamp = queryTimeLimit();
    Object[] params = new Object[] { timestamp , pStoreId };

    return statement.executeQuery(view, params);
  }

  /**
   * Finds and returns the list of in-store shoppers who have requested help in the specified store
   * @param pStoreId the repsository id for the store
   * @return An array of in-store user repository items
   * @throws RepositoryException
   */
  public RepositoryItem[] usersRequiringHelp(String pStoreId) throws RepositoryException{
    RepositoryView view = getProfileRepository().getView(getPropertyManager().getInStoreUsersItemDescriptorName());

    RqlStatement statement =
      RqlStatement.parseRqlStatement(getHelpRequestQuery());

    Timestamp timestamp = queryTimeLimit();
    Object[] params = new Object[] { timestamp, pStoreId, true };

    return statement.executeQuery(view, params);
  }

  /**
   * Finds the number of users who have requested help
   * @param pStoreId the is of the store
   * @return number of users who have requested help
   * @throws RepositoryException
   */
  public int usersRequiringHelpCount(String pStoreId) throws RepositoryException{
    RepositoryView view = getProfileRepository().getView(getPropertyManager().getInStoreUsersItemDescriptorName());

    RqlStatement statement =
      RqlStatement.parseRqlStatement(getHelpRequestCountQuery());

    Timestamp timestamp = queryTimeLimit();
    Object[] params = new Object[] { timestamp, pStoreId, true };

    RepositoryItem[] items = statement.executeQuery(view, params);
    return items == null ? 0 : items.length;
  }

  /**
   * Deletes the help request for the specified user
   * @param pStoreId the repository id for the store
   * @param pUserId the id of the user (repositoryId if not an anon user)
   * @throws RepositoryException
   */
  public void deleteHelpRequest(String pStoreId, String pUserId) throws RepositoryException {
    MutableRepository inStoreDeviceRepository = getProfileRepository();

    MutableRepositoryItem existingItem = existingItem(pUserId, pStoreId);

    clearHelpRequestFromExistingItem(existingItem, inStoreDeviceRepository);
  }

  /**
   * Removes the help requested flag from the user location record in the repository
   * @param pEditableItem the repository item to edit
   * @param pRepository the in store device repository
   * @throws RepositoryException
   */
  private void clearHelpRequestFromExistingItem(MutableRepositoryItem pEditableItem,
                                                MutableRepository pRepository) throws RepositoryException {
    pEditableItem.setPropertyValue(getPropertyManager().getHelpRequestedPropertyName(), false);
    pEditableItem.setPropertyValue(getPropertyManager().getHelpRequestTimePropertyName(), null);
    pEditableItem.setPropertyValue(getPropertyManager().getUserDisplayNamePropertyName(), null);

    pRepository.updateItem(pEditableItem);
  }
}

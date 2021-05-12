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

import atg.projects.store.mobile.userprofiling.csr.MobileCSRStorePropertyManager;
import atg.repository.MutableRepository;
import atg.repository.RepositoryView;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.rql.RqlStatement;
import atg.service.scheduler.SchedulableService;
import atg.service.scheduler.ScheduledJob;
import atg.service.scheduler.Scheduler;

import java.sql.Timestamp;

/**
 * This service checks for devices that are no longer in store
 * and removes the record signifying that they are in store.
 *
 * Author: Shahla Almasri Hafez
 * @version $Change: 1536476 $$DateTime: 2018/04/13 08:11:14 $$Author: releng $
 **/
public class RemoveExpiredInStoreDevicesJob extends SchedulableService {

  /** Class version string */
  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/projects/store/mobile/device/csr/RemoveExpiredInStoreDevicesJob.java#3 $$Change: 1536476 $";

  private MutableRepository mProfileRepository;
  private MobileCSRStorePropertyManager mPropertyManager;
  private int mExpiredDeviceQueryTimeInHours;
  private String mExpiredDevicesQuery;


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
   *get the time that needs to have passed for a device session to be deemed expired (in hours)
   * @return
   */
  public int getExpiredDeviceQueryTimeInHours() {
    return mExpiredDeviceQueryTimeInHours;
  }

  /**
   *sets the time that needs to have passed for a device session to be deemed expired (in hours)
   * @param pExpiredDeviceQueryTimeInHours
   */
  public void setExpiredDeviceQueryTimeInHours(int pExpiredDeviceQueryTimeInHours) {
    mExpiredDeviceQueryTimeInHours = pExpiredDeviceQueryTimeInHours;
  }

  /**
   *gets the RQL query for finding the device sessions that have expired
   * @return
   */
  public String getExpiredDevicesQuery() {
    return mExpiredDevicesQuery;
  }

  /**
   *sets the RQL query for finding the device sessions that have expired
   * @param mExpiredDevicesQuery
   */
  public void setExpiredDevicesQuery(String mExpiredDevicesQuery) {
    this.mExpiredDevicesQuery = mExpiredDevicesQuery;
  }

  private Timestamp queryTimeLimit(){
    //Query time window
    return new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * getExpiredDeviceQueryTimeInHours()));
  }

  /**
   * Performs the scheduled task to remove any expired devices
   * @param pScheduler the scheduler managing this job
   * @param pJob the scheduled job associated with this service
   */
  public void performScheduledTask(Scheduler pScheduler, ScheduledJob pJob) {
    MutableRepository inStoreDeviceRepository = getProfileRepository();
    try {
      //Get repository
      RepositoryView view = inStoreDeviceRepository.getView(getPropertyManager().getInStoreUsersItemDescriptorName());
      RqlStatement statement = RqlStatement.parseRqlStatement(getExpiredDevicesQuery());

      //set the query params
      Object[] params = new Object[] { queryTimeLimit() };
      RepositoryItem[] foundItems = statement.executeQuery(view, params);

      //if there are records, cycle through them and delete
      if(foundItems != null && foundItems.length > 0) {

        for (RepositoryItem foundItem : foundItems) {
          MutableRepositoryItem editableItem = inStoreDeviceRepository.getItemForUpdate(foundItem.getRepositoryId(),
          getPropertyManager().getInStoreUsersItemDescriptorName());

          //Delete the record
          inStoreDeviceRepository.removeItem(editableItem.getRepositoryId(), getPropertyManager().getInStoreUsersItemDescriptorName());

        }
      }
    }
    catch (RepositoryException e) {
      if(isLoggingError()){
        logError(e);
      }
    }


  }


}

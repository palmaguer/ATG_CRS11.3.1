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

package atg.projects.store.inventory;

import java.util.Date;

import atg.commerce.inventory.InventoryException;
import atg.commerce.inventory.LocationInventoryManager;
import atg.projects.store.catalog.StoreCatalogProperties;
import atg.repository.MutableRepository;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

/**
 * An interface for CRS additions to the LocationInventoryManager.
 * 
 * @author ATG
 *
 */
public interface StoreInventoryManagerInterface extends LocationInventoryManager {

  public static final String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/src/atg/projects/store/inventory/StoreInventoryManagerInterface.java#2 $$Change: 1503965 $";
  
  /**
   * The method logs InventoryException. MissingInventoryException is logged 
   * only if <code>logMissingInventoryExceptionsAsError</code> property is configured
   * to true.
   * @param pException The InventoryException to log.
   * @return true if exception has been logged as error.
   */
  public boolean logInventoryException(InventoryException pException);
  
  /**
   * Returns true if SKU is in stock.
   * 
   * @param pSkuId SKU ID.
   * 
   * @return true if SKU is in stock.
   * 
   * @throws InventoryException if there was an error while attempting to return the inventory information.
   */
  public boolean isItemInStock(String pSkuId) throws InventoryException;

  public boolean isItemInStock(String pSkuId, String pLocationId)
      throws InventoryException;

  /**
   * Derive the availabilityStatus based on the properties of the product and the item.
   * Always derives the status, does not check the current value of availabilityStatus.
   *
   * Pre-orderability is checked first.  Pre-orderability can be set based just on product
   * properties or on a combination of product properties and inventory levels. The end
   * date for pre-orderability can be set to a fixed date or use stockLevel to determine when
   * it ends.
   *
   * If an item is not pre-orderable, then the inventory levels are checked to see if it
   * is available, back-orderable or unavailable.
   *
   * @param  pProduct The product we are deriving the status for.
   * @param  pSkuId The SKU we are deriving the status for.
   *
   * @return The derived status.
   * 
   * @throws InventoryException if there was an error while attempting to return the inventory information.
   */
  public int queryAvailabilityStatus(RepositoryItem pProduct, String pSkuId)
      throws InventoryException;

  public int queryAvailabilityStatus(RepositoryItem pProduct, String pSkuId,
      String pLocationId) throws InventoryException;

  /**
   * Check to see if backInStoreNotifyItem already exists for this combination
   * of CatalogRefId and email.
   * 
   * @param pRepository - Repository where to check if item exists.
   * @param pCatalogRefId - repository id.
   * @param pEmail - string that represents email.
   * @param pProductId - product id.
   * 
   * @return true if item exists.
   * 
   * @throws RepositoryException if there was an error while creating repository item.
   */
  public boolean isBackInStockItemExists(MutableRepository pRepository,
      String pCatalogRefId, String pEmail, String pProductId)
      throws RepositoryException;

  public boolean isBackInStockItemExists(MutableRepository pRepository,
      String pCatalogRefId, String pEmail, String pProductId, String pLocationId)
      throws RepositoryException;

  /**
   * Creates the required item in the repository.
   * 
   * @param pRepository - Repository where to create item.
   * @param pCatalogRefId - repository id.
   * @param pEmail - string that represents email.
   * @param pProductId - product id.
   * @param pLocale - local.
   * @param pSiteId - site id.
   * 
   * @throws RepositoryException if there was an error while creating repository item.
   */
  public void createBackInStockNotifyItem(MutableRepository pRepository, 
                                             String pCatalogRefId, 
                                             String pEmail, 
                                             String pProductId,
                                             String pLocale, 
                                             String pSiteId) throws RepositoryException;
      
  /**
   * Get the availabilityDate from the inventory data for a SKU item.
   *
   * @param pSkuId - The SKU we are getting the availability date for.
   * 
   * @return The availabilityDate which may be null.
   * 
   * @throws InventoryException if there was an error while attempting to return the inventory information.
   */
  public Date getBackorderAvailabilityDate(String pSkuId) throws InventoryException;

  /**
   * Get the availabilityDate for a product.
   *
   * @param pProduct - The product we are getting the availability date for.
   * 
   * @return The availabilityDate which may be null.
   */
  public Date getPreorderAvailabilityDate(RepositoryItem pProduct);
  
  /**
   * @return catalog properties.
   */
  public StoreCatalogProperties getCatalogProperties();
  
  
  /* --------------------------------------------------------------------------- */
  /* Methods below are from RepositoryInventoryManager and used by CRS           */
  /* --------------------------------------------------------------------------- */
  
  /**
   * Returns property CatalogRefRepository
   **/
  public Repository getCatalogRefRepository();
  
  /**
   * The integer value for the availabilityStatus of DERIVED
   **/
  public int getAvailabilityStatusDerivedValue();

  /**
   * The integer value for the availabilityStatus of IN_STOCK
   **/
  public int getAvailabilityStatusInStockValue();

  /**
   * The integer value for the availabilityStatus of BACKORDERABLE
   **/
  public int getAvailabilityStatusBackorderableValue();

  /**
   * The integer value for the availabilityStatus of PREORDERABLE
   **/
  public int getAvailabilityStatusPreorderableValue();

  /**
   * The integer value for the availabilityStatus of OUT_OF_STOCK
   **/
  public int getAvailabilityStatusOutOfStockValue();

  /**
   * The integer value for the availabilityStatus of DISCONTINUED
   **/
  public int getAvailabilityStatusDiscontinuedValue();

}
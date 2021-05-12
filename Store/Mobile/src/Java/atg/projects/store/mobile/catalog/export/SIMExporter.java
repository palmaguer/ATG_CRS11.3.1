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

package atg.projects.store.mobile.catalog.export;

import atg.commerce.inventory.InventoryException;
import atg.commerce.inventory.LocationInventoryManager;
import atg.core.jdbc.JDBCUtils;
import atg.nucleus.GenericService;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemDescriptor;
import atg.repository.RepositoryItemGroup;
import atg.repository.RepositoryUtils;
import atg.repository.RepositoryView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * This component exports ATG Commerce Product, SKU, Store and
 * Store+SKU Price information to the Oracle Store Inventory
 * Management (SIM) application schema.
 *
 * Created: 2014/06/30
 * @author Peter Eddy
 */
public class SIMExporter extends GenericService
{
  public static String CLASS_VERSION =
    "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/src/atg/projects/store/mobile/catalog/export/SIMExporter.java#2 $$Change: 1503965 $";
  
  private static String
    ITEM_HIERARCHY_DEPT_NAME = "CRS Department",
    ORGANIZATION_UNIT_ID = "CRS",
    SUPPLIER_ID = "MME",
    ORACLE_COMMERCE_BRAND = "Oracle Commerce Reference";
  
  private static int
    // Values used in the SIM ITEM.ITEM_LEVEL colum to define the
    // Product/SKU hierarchy. Products are at level 1, SKUs are at
    // level 2. The SIM schema's ITEM.TRANSACTION_LEVEL colum values
    // are set to SIM_SKU_LEVEL (2), meaning the SKU level items are
    // items in the hieararchy that can be transacted (sold).
    SIM_PRODUCT_LEVEL = 1,
    SIM_SKU_LEVEL = 2;
  
  private int
    mFakeStoreItemStockLevel = -1;
  
  private Repository
    mProductCatalogRepository,
    mLocationRepository;

  private Map mCountryNamesToLocales = new HashMap<String,String>();

  // If some conversion between the Oracle Commerce IDs and the SIM IDs needs to take place,
  // this map stores the converted-to-original ID mapping.  This map is reason to batch imports
  // if the data is particularly large.
  private Map<String, String> mConvertedSkuIdsToOriginalSkuIds = new HashMap<String, String>();
  
  private DataSource mSimDataSource;

  private Connection mConnection;

  private LocationInventoryManager mLocationInventoryManager;
  
  public void setProductCatalogRepository(Repository pRepository) {
    mProductCatalogRepository=pRepository;
  }

  public Repository getProductCatalogRepository() {
    return mProductCatalogRepository;
  }

  public void setLocationRepository(Repository pRepository) {
    mLocationRepository=pRepository;
  }

  public Repository getLocationRepository() { return mLocationRepository; }

  /**
   * If < 0, create stock level for store items using ATG inventory
   * data. If true, create fake stock levels all set to the value of
   * this property and without using ATG inventory data.
   */
  public void setFakeStoreItemStockLevel(int pFakeLevel) {
    mFakeStoreItemStockLevel = pFakeLevel;
  }

  public int getFakeStoreItemStockLevel() {
    return mFakeStoreItemStockLevel;
  }
  
  public boolean isCreateFakeItemStock() {
    return getFakeStoreItemStockLevel() >= 0;
  }
  
  public void setLocationInventoryManager(LocationInventoryManager pManager) {
    mLocationInventoryManager = pManager;
  }
  
  public LocationInventoryManager getLocationInventoryManager() {
    return mLocationInventoryManager;
  }
  
  public void setSimDataSource(DataSource pDataSource) {
    mSimDataSource=pDataSource;
  }
  
  public DataSource getSimDataSource() { return mSimDataSource; }

  // Should SKU Ids always be exported as uppercase?
  private boolean mForcingUppercaseIds = false;

  /**
   * Returns true if SKU Ids should be forced into uppercase when being exported.
   * @return true if forcing uppercase Ids
   */
  public boolean isForcingUppercaseIds() {
    return mForcingUppercaseIds;
  }

  /**
   * If set to true, SKU ids exported to SIM will be upper-cased, even if they are not upper case in the repository.  This
   * may be necessary if integrating with another system that can't handle lowercase Ids.
   * @param pForcingUppercaseIds if SKU ids should be converted to uppercase.
   */
  public void setForcingUppercaseIds(boolean pForcingUppercaseIds) {
    mForcingUppercaseIds = pForcingUppercaseIds;
  }

  /**
   * Map of country names in the CRS data to the locale values used in
   * SIM. For example, Deutschland in CRS is mapped to GR for SIM.
   *
   * @param pMap a potentially empty but non-null map of country to locale values
   */
  public void setCountryNamesToLocales(Map pMap) { mCountryNamesToLocales=pMap; }

  /**
   * Map of country names in the CRS data to the locale values used in
   * SIM. For example, Deutschland in CRS is mapped to GR for SIM.
   *
   * @return a potentially empty but non-null map of country to locale values
   */
  public Map<String,String> getCountryNamesToLocales() {
    return mCountryNamesToLocales;
  }
  
  protected void setConnection(Connection pConn) { mConnection=pConn; }
  protected Connection getConnection() { return mConnection; }
  
  protected Statement createStatement() throws Exception {
    return getConnection().createStatement();
  }

  protected RepositoryItem [] getItemsOfType(Repository pRepository,
    String pItemDescriptorName) throws RepositoryException
  {
    RepositoryView view =
      pRepository.getItemDescriptor(pItemDescriptorName).getRepositoryView();
    return view.executeQuery(view.getQueryBuilder().createUnconstrainedQuery());
  }

  /**
   * Export CRS data to the SIM schema.
   */
  public void executeExport() throws Exception
  {
    setConnection(getSimDataSource().getConnection());

    try {
      populateInitialData();
      exportProductsAndSKUs();
      RepositoryItem [] stores = exportStores();
      createStoreItems();
      createBuddyStores();
      if (isCreateFakeItemStock()) {
        createFakeStoreItemStock(getFakeStoreItemStockLevel());
      }
      else {
        createStoreItemsStock(stores);
      }
      createSupplierItems();
      createItemPrices();
      createStoreSequenceArea();
      mConvertedSkuIdsToOriginalSkuIds.clear();
      vlogInfo("SIM Export Complete");
    }
    finally {
      try {
        getConnection().close();
      }
      catch (SQLException se) {
        logError(se);
      }
      finally {
        setConnection(null);
      }
    }
  }

  /**
   * Delete data previously exported to the SIM schema, attempting to
   * remove only that data that this component would have added.
   */
  public void deleteExportedData() throws Exception
  {
    String crsStoreSubquery =
      "(SELECT id FROM store WHERE organization_unit_id = '" +
      ORGANIZATION_UNIT_ID + "')";
    
    setConnection(getSimDataSource().getConnection());

    try {
      executeStatement("DELETE FROM STORE_SEQUENCE_AREA " + 
                       "WHERE store_id IN " + crsStoreSubquery);
      executeStatement("DELETE FROM ITEM_PRICE " +
                       "WHERE store_id IN " + crsStoreSubquery);
      executeStatement("DELETE FROM SUPPLIER_ITEM_COUNTRY " +
                       "WHERE supplier_id = '" + SUPPLIER_ID + "'");
      executeStatement("DELETE FROM SUPPLIER_ITEM " +
                       "WHERE supplier_id = '" + SUPPLIER_ID + "'");
      executeStatement("DELETE FROM STORE_ITEM_STOCK " +
                       "WHERE item_id IN " +
                       "(SELECT ITEM_ID FROM ITEM WHERE brand = " +
                       "'" + ORACLE_COMMERCE_BRAND + "')");
      executeStatement("DELETE FROM STORE_GROUP " +
                       "WHERE store_id IN " + crsStoreSubquery);
      executeStatement("DELETE FROM STORE_ITEM " +
                       "WHERE store_id IN " + crsStoreSubquery);
      executeStatement("DELETE FROM ADDRESS " +
                       "WHERE entity_type = 'ST' " +
                       "AND entity_id IN " + crsStoreSubquery);
      executeStatement("DELETE FROM STORE " +
                       "WHERE organization_unit_id = '" + ORGANIZATION_UNIT_ID + "'");
      executeStatement("DELETE FROM ITEM WHERE BRAND = " +
                       "'" + ORACLE_COMMERCE_BRAND + "'");
      executeStatement("DELETE FROM SUPPLIER " +
                       "WHERE ID = '" + SUPPLIER_ID + "'");
      executeStatement("DELETE FROM ITEM_HIERARCHY " +
                       "WHERE DEPARTMENT_NAME = '" + ITEM_HIERARCHY_DEPT_NAME + "'");
    }
    finally {
      try {
        getConnection().close();
      }
      catch (SQLException se) {
        logError(se);
      }
      finally {
        setConnection(null);
      }
    }
  }
  
  /**
   * Empty the tables that this component populates.
   */
  public void truncateTables() throws Exception
  {
    try {
      setConnection(getSimDataSource().getConnection());

      executeStatement("DELETE FROM STORE_SEQUENCE_AREA");
      executeStatement("DELETE FROM ADDRESS");
      executeStatement("DELETE FROM STORE_ITEM_STOCK");
      executeStatement("DELETE FROM STORE_ITEM");
      executeStatement("DELETE FROM STORE_GROUP");
      executeStatement("DELETE FROM STORE");
      executeStatement("DELETE FROM SUPPLIER_ITEM_COUNTRY");
      executeStatement("DELETE FROM SUPPLIER_ITEM");
      executeStatement("DELETE FROM ITEM_PRICE");
      executeStatement("DELETE FROM ITEM");
      executeStatement("DELETE FROM SUPPLIER");
      executeStatement("DELETE FROM ITEM_HIERARCHY");
    }
    finally {
      try {
        getConnection().close();
      }
      catch (SQLException se) {
        logError(se);
      }
      finally {
        setConnection(null);
      }
    }
  }
  
  protected void populateInitialData() throws Exception
  {
    vlogInfo("Populating initial data");
    
    executeStatement(
      "INSERT INTO ITEM_HIERARCHY (ID, DEPARTMENT_ID, DEPARTMENT_NAME, " +
      "  CLASS_ID, CLASS_NAME, SUBCLASS_ID, SUBCLASS_NAME, STATUS) " +
      "VALUES (ITEM_HIERARCHY_SEQ.NEXTVAL, 1234, '" + ITEM_HIERARCHY_DEPT_NAME +
      "', 1, 'CRS Class', 1,'CRS Subclass','A')");
    executeStatement(
      "INSERT INTO SUPPLIER (ID, NAME, CURRENCY_CODE, STATUS, " +
      "  DELIVERY_DISCREPANCY_TYPE, AUTHORIZATION_REQUIRED, RETURN_ALLOWED, " +
      "  PO_CREATE_ALLOWED) " +
      "VALUES('" + SUPPLIER_ID + "', 'M & M Enterprises', 'USD', 'A', 0, 'N', 'Y', 'Y')");
  }
  
  protected int executeStatement(String pSQL) throws Exception
  {
    try (Statement stmt = createStatement()) {
      return stmt.executeUpdate(pSQL);
    }
  }

  protected RepositoryItem [] exportProductsAndSKUs() throws Exception
  {
    vlogInfo("Exporting Products and SKUs");

    RepositoryItem [] products =
      getItemsOfType(getProductCatalogRepository(), "product");

    for (RepositoryItem product : products) {
      exportItem(product, null);
      for (RepositoryItem sku : getRelatedItems(product,"childSKUs")) {
        exportItem(sku, product);
      }
    }
    
    return products;
  }

  /**
   * Export the repository item to a SIM ITEM row.
   *
   * @param pItem May be either a repository item type of Product or
   * SKU. If pParentItem is non-null, this is a SKU type and
   * pParentItem is the SKU's product.
   * @param pParentItem A Product repository item that is the parent
   * of the SKU or null.
   */
  protected void exportItem(RepositoryItem pItem, RepositoryItem pParentItem)
    throws Exception
  {
    try (PreparedStatement stmt = getConnection().prepareStatement(
      "INSERT INTO ITEM (" +
      // 1        2          3              4         5           
      "  ITEM_ID, ITEM_TYPE, DEPARTMENT_ID, CLASS_ID, SUBCLASS_ID, " +
      // 6                  7                 8       9
      "  SHORT_DESCRIPTION, LONG_DESCRIPTION, STATUS, ORDER_AS_TYPE, " +
      // 10              11                 12          13        14
      "  PARENT_ITEM_ID, TRANSACTION_LEVEL, ITEM_LEVEL, SELLABLE, ORDERABLE, " +
      // 15               16         17                     18
      "  UNIT_OF_MEASURE, CASE_SIZE, ESTIMATE_SOH_FOR_PACK, IS_PRIMARY, " +
      // 19
      "  BRAND) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
    {

      String skuId;

      if (isForcingUppercaseIds()) {
        skuId = pItem.getRepositoryId().toUpperCase();
        // Only save the conversion if it has modified the string in some way.
        if (!skuId.equals(pItem.getRepositoryId())) {
          // if the ID can be converted back to the original by converting it to lowercase
          // don't save it in the map.
          if (!skuId.toLowerCase().equals(pItem.getRepositoryId())) {
            mConvertedSkuIdsToOriginalSkuIds.put(skuId, pItem.getRepositoryId());
          }
        }
      } else {
        skuId = pItem.getRepositoryId();
      }

      stmt.setString(1, skuId);
      stmt.setInt(2, 0);                          // 0 == Item
      stmt.setLong(3, 1234L);                     // Department (see ITEM_HIERARCHY)
      stmt.setLong(4, 1L);                        // Class? (ITEM_HIERARCHY.CLASS_ID?)
      stmt.setLong(5, 1L);                        // Subclass? (ITEM_HIERARCHY.SUBCLASS_ID?)
      stmt.setString(6, (String) pItem.getPropertyValue("displayName"));
      stmt.setString(7, (String) pItem.getPropertyValue("description"));
      stmt.setString(8, "A");                     // Status 'A' = Available
      stmt.setString(9, null);

      String parentId = pParentItem == null ? null : pParentItem.getRepositoryId();
      if (parentId != null && isForcingUppercaseIds()) {
        parentId = parentId.toUpperCase();
      }

      stmt.setString(10,
        (parentId));
      stmt.setInt(11, SIM_SKU_LEVEL);
      stmt.setInt(12, (pParentItem == null ? SIM_PRODUCT_LEVEL : SIM_SKU_LEVEL));
      stmt.setString(13, "Y");                    // Y = Sellable
      stmt.setString(14, "Y");                    // Y = Orderable
      stmt.setString(15, "EA");
      stmt.setInt(16, 100);                       // 100 items in a case, let's say
      stmt.setString(17, "N");
      stmt.setString(18, "N");
      stmt.setString(19, ORACLE_COMMERCE_BRAND);
      stmt.executeUpdate();
    }
  }

  /**
   * Create dummy prices for each item at each store. This is all
   * dummy information to keep the SIM happy when you look up an item
   * using its UI. All products are $100.00 USD.
   *
   * NB: This method uses an Oracle sequence (item_price_seq.nextval)
   * to generate IDs for the rows it inserts. Sequences must be
   * installed in the schema.
   */
  protected void createItemPrices() throws Exception
  {
    vlogInfo("Creating Prices for Items");
    
    try (PreparedStatement stmt = getConnection().prepareStatement(
      "INSERT INTO ITEM_PRICE(id, store_id, item_id, effective_date, price_type, " +
      "  store_requested, price_currency, price_value, status, update_date) " +
      "SELECT item_price_seq.nextval, si.store_id, si.item_id, ?, 202, 0, 'USD', " +
      "  100.0, 3, ? " +
      "FROM STORE_ITEM si, ITEM im, STORE s " +
      "WHERE s.id = si.store_id " +
      "AND si.item_id = im.item_id "))
    {
      stmt.setDate(1, new Date(2014, 1, 1));
      stmt.setDate(2, new Date(new java.util.Date().getTime()));
      stmt.executeUpdate();
    }
  }
  
  protected void createStoreItems() throws Exception
  {
    vlogInfo("Creating Inventory Items for individual Stores");

    try (PreparedStatement stmt = getConnection().prepareStatement(
      "INSERT INTO STORE_ITEM (ITEM_ID, STORE_ID, ITEM_TYPE, SHORT_DESCRIPTION, " +
      "  LONG_DESCRIPTION, STATUS, STATUS_DATE, SELLING_UNIT_OF_MEASURE, " + 
      "  DEFAULT_CURRENCY, PRIMARY_SUPPLIER_ID, UIN_REQUIRED, STORE_ORDER_ALLOWED, " +
      "  STORE_CONTROL_PRICING) " +
      "SELECT distinct im.item_id, s.id, im.item_type, im.short_description, " +
      "  im.long_description, im.status, ?, 'EA', 'USD', null, 'N', 'Y', 1 " +
      "FROM STORE s, ITEM im " +
      "WHERE s.organization_unit_id = '" + ORGANIZATION_UNIT_ID + "'" +
      "AND im.item_level = ?"))
    {
      stmt.setDate(1, new Date(2014, 1, 1));
      stmt.setInt(2, SIM_SKU_LEVEL);
      stmt.executeUpdate();
    }
  }

  protected void createSupplierItems() throws Exception
  {
    vlogInfo("Creating Suppliers for Items");
    
    executeStatement(
      "INSERT INTO SUPPLIER_ITEM (ITEM_ID, SUPPLIER_ID, IS_PRIMARY) " +
        "SELECT distinct im.item_id, s.id, 'Y' " +
        "FROM SUPPLIER s, ITEM im");

    executeStatement(
      "INSERT INTO SUPPLIER_ITEM_COUNTRY (ITEM_ID, SUPPLIER_ID, COUNTRY_ID, " +
        "  CASE_SIZE, UNIT_COST_CURRENCY,UNIT_COST_VALUE) " +
        "SELECT distinct si.item_id, si.supplier_id, 'US', 12, null, null " +
        "FROM SUPPLIER_ITEM si, SUPPLIER s, ITEM im " +
        "WHERE si.supplier_id = s.id " +
        "AND si.item_id = im.item_id ");
  }

  /**
   * From Heidi Yu @ SIM, "It is recommended to set one default
   * area_type = 0 for each store in the system"
   */
  protected void createStoreSequenceArea() throws Exception
  {
    vlogInfo("Creating Store Sequence Areas");
    
    executeStatement(
      "INSERT INTO STORE_SEQUENCE_AREA " +
      "  (id, store_id, description, area_type, sequence_order) " + 
      "SELECT store_sequence_area_seq.nextval, store.id as store_id, " +
      "  'No Location', 0, 0 " +
      "FROM STORE");
  }
  
  /**
   * Copy stock level data to SIM SKU-level ITEM rows
   */
  protected void createStoreItemsStock(RepositoryItem [] pStores) throws Exception
  {
    try (PreparedStatement stmt = getConnection().prepareStatement(
           "SELECT ITEM_ID FROM ITEM WHERE ITEM_LEVEL = ? " +
           "AND BRAND = '" + ORACLE_COMMERCE_BRAND + "'"))
    {
      stmt.setLong(1, SIM_SKU_LEVEL);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String skuId = rs.getString(1);
        vlogInfo("Setting store-specific stock levels for SKU {0}", skuId);
        for (RepositoryItem store : pStores) {
          createStoreItemStock(store, skuId);
        }
      }
    }
  }

  protected void createStoreItemStock(RepositoryItem pStore, String pSkuId)
    throws Exception
  {
    try (PreparedStatement stmt = getConnection().prepareStatement(
      "INSERT INTO STORE_ITEM_STOCK (ITEM_ID, STORE_ID, QUANTITY_TOTAL," +
      "  QUANTITY_VENDOR_RETURN) " + 
      "VALUES (?, ?, ?, 0)"))
    {
      String skuId = pSkuId;
      if (isForcingUppercaseIds()) {
        skuId = pSkuId.toUpperCase();
      }

      stmt.setString(1, skuId);
      stmt.setLong(2,
        parseLong((String) pStore.getPropertyValue("externalLocationId")));

      // If the sku ID has been converted to uppercase, we need to get the original ID
      // before querying the repositiory.
      if (isForcingUppercaseIds()) {
        String originalId = mConvertedSkuIdsToOriginalSkuIds.get(skuId);
        skuId = originalId == null ? skuId.toLowerCase() : originalId;
      }

      stmt.setLong(3, getStockLevelForItemAtStore(skuId, pStore.getRepositoryId()));
      stmt.executeUpdate();
    }
  }
  
  protected void createFakeStoreItemStock(int pStockLevel) throws Exception
  {
    executeStatement(
      "INSERT INTO STORE_ITEM_STOCK (ITEM_ID, STORE_ID, QUANTITY_TOTAL," +
        "  QUANTITY_VENDOR_RETURN) " +
        "SELECT SI.ITEM_ID, SI.STORE_ID, " + pStockLevel + ", 0 " +
        "FROM STORE_ITEM SI, STORE S, ITEM IM " +
        "WHERE SI.STORE_ID = S.ID AND SI.ITEM_ID = IM.ITEM_ID " +
        "AND IM.BRAND = '" + ORACLE_COMMERCE_BRAND + "' ");
  }

  protected RepositoryItem [] exportStores() throws Exception
  {
    vlogInfo("Exporting Stores");
    
    RepositoryItem [] stores = getItemsOfType(getLocationRepository(), "store");

    for (RepositoryItem store : stores) {
      insertStore(store);
    }

    return stores;
  }

  protected void insertStore(RepositoryItem pStore) throws Exception
  {
    long id = parseLong((String) pStore.getPropertyValue("externalLocationId"));

    try (PreparedStatement stmt = getConnection().prepareStatement(
      //                  1   2     3               4          5
      "INSERT INTO STORE (ID, NAME, LOCALE_COUNTRY, OPEN_DATE, SIM_STORE, " +
      // 6                   7
      "ORGANIZATION_UNIT_ID, TIMEZONE) VALUES (?, ?, ?, ?, ?, ?, ?) "))
    {
      stmt.setLong(1, id);
      stmt.setString(2, (String) pStore.getPropertyValue("name"));
      stmt.setString(3,
        getCountryNamesToLocales().get((String) pStore.getPropertyValue("country")));
      stmt.setTimestamp(4, (Timestamp) pStore.getPropertyValue("startDate"));      
      stmt.setString(5, "Y");
      stmt.setString(6, ORGANIZATION_UNIT_ID);
      // We don't currently care about timezones (we think) so use a dummy
      // one for now.
      stmt.setString(7, "America/Chicago");
      stmt.executeUpdate();
    }

    insertStoreAddress(pStore, id);
  }

  protected void insertStoreAddress(RepositoryItem pStore, long pStoreId)
    throws Exception
  {
    try (PreparedStatement stmt = getConnection().prepareStatement(
      "INSERT INTO ADDRESS (" +
      // 1    2            3          4     5           6
      "  ID,  ENTITY_TYPE, ENTITY_ID, TYPE, IS_PRIMARY, ADDRESS_LINE_1, " +
      // 7               8               9     10     11          12
      "  ADDRESS_LINE_2, ADDRESS_LINE_3, CITY, STATE, COUNTRY_ID, POSTAL_CODE, " +
      // 13             14           15
      "  CONTACT_PHONE, CONTACT_FAX, CONTACT_EMAIL" +
      ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "))
    {
      stmt.setString(1, "" + pStoreId);
      stmt.setString(2, "ST");     // ST=Store
      stmt.setString(3, ""+pStoreId);
      stmt.setString(4, "01");       // Business Address
      stmt.setString(5, "Y");      // Y=Is primary address
      stmt.setString(6, (String) pStore.getPropertyValue("address1"));
      stmt.setString(7, (String) pStore.getPropertyValue("address2"));
      stmt.setString(8, (String) pStore.getPropertyValue("address3"));
      stmt.setString(9, (String) pStore.getPropertyValue("city"));
      stmt.setString(10,
        truncate(3, (String) pStore.getPropertyValue("stateAddress")));
      stmt.setString(11, getCountryNamesToLocales().get(
          (String) pStore.getPropertyValue("country")));
      stmt.setString(12, (String) pStore.getPropertyValue("postalCode"));
      stmt.setString(13, (String) pStore.getPropertyValue("phoneNumber"));
      stmt.setString(14, (String) pStore.getPropertyValue("faxNumber"));
      stmt.setString(15, (String) pStore.getPropertyValue("email"));
      stmt.executeUpdate();
    }
  }

  protected void createBuddyStores() throws Exception
  {
    vlogInfo("Creating Store Groups (Buddy Stores)");
    
    executeStatement(
      "INSERT INTO STORE_GROUP(STORE_ID, MEMBER_ID) " +
      "SELECT S1.ID, S2.ID " + 
      "FROM STORE S1, STORE S2, ADDRESS A1, ADDRESS A2 " + 
      "WHERE S1.ID = A1.ENTITY_ID AND S2.ID = A2.ENTITY_ID " + 
      "AND A1.COUNTRY_ID = A2.COUNTRY_ID AND A1.STATE = A2.STATE " + 
      "AND S1.ID != S2.ID");
  }
  
  protected String truncate(int pMaxLength, String pString)
  {
    if (pString == null) {
      return pString;
    }
    else {
      return pString.substring(0, Math.min(pString.length(), pMaxLength));
    }
  }
  
  protected List<RepositoryItem> getRelatedItems(RepositoryItem pItem,
    String pPropertyName) throws RepositoryException
  {
    return (List<RepositoryItem>) pItem.getPropertyValue(pPropertyName);
  }   
  
  protected Long parseLong(String pLongStr)
  {
    try {
      return Long.parseLong(pLongStr);
    }
    catch (Exception ex) {
      return null;
    }
  }

  protected long getStockLevelForItemAtStore(String pItemId, String pStoreId)
  {
    long stockLevel = 0L;
    
    try {
      stockLevel = getLocationInventoryManager().queryStockLevel(pItemId, pStoreId);
    }
    catch (InventoryException ie) {
      vlogDebug(ie, "Unable to retrieve inventory data for Item:{0} at Store {1}",
        pItemId, pStoreId);
    }

    return stockLevel;
  }
}

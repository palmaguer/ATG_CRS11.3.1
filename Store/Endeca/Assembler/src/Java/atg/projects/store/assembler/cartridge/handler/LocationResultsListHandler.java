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

package atg.projects.store.assembler.cartridge.handler;

import java.util.Iterator;
import java.util.List;

import atg.repository.Repository;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryException;
import atg.endeca.assembler.cartridge.handler.ResultsListHandler;
import atg.endeca.assembler.AssemblerTools;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.cartridge.ResultsListConfig;
import com.endeca.infront.cartridge.model.SortOptionConfig;
import com.endeca.infront.cartridge.model.Record;
import com.endeca.infront.cartridge.model.Attribute;

/**
 * This handler populates the content item with the details from repository
 * 
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/LocationResultsListHandler.java#3 $
 * @updated $DateTime: 2018/04/13 08:11:14 $
 */
public class LocationResultsListHandler extends ResultsListHandler {

  //-------------------------------------
  /** Class version string */

  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/LocationResultsListHandler.java#3 $$Change: 1536476 $";

  

  //----------------------------------------------------------------------------
  // STATICS
  //----------------------------------------------------------------------------
  /**
   * Label string for distance sorter ascending.
   */
  public static final String DISTANCE_ASC = "sort.distanceAscending";
  
  /**
   * Label string for distance sorter descending.
   */
  public static final String DISTANCE_DSC = "sort.distanceDescending";
  
  /**
   * Open brace.
   */
  public static final String OPEN_BRACE = "(";
  
  /**
   * Close brace.
   */
  public static final String CLOSE_BRACE = ")";
  
  /**
   * Pipe separator.
   */
  public static final String SEPARATOR_PIPE = "|";
  
  /**
   * Comma separator.
   */
  public static final String SEPARATOR_COMMA = ",";
  
  /**
   * Ascending sort order.
   */
  public static final String ASCENDING = "0";
  
  /**
   * Descending sort order.
   */
  public static final String DESCENDING = "1";
  
  /**
   *  Property name to retrieve store repository id.
   */
  public static final String STORE_REPOSITORY_ID = "store.repositoryId";
  
  //----------------------------------------------------------------------------
  // PROPERTIES
  //----------------------------------------------------------------------------
  
  //------------------------------------
  // property name: geocodeProperty
  //------------------------------------
  private String mGeocodeProperty = null;
    
  /**
   * @return geocode 
   *   Get the geocode property of store.
   */
  public String getGeocodeProperty() {
    return mGeocodeProperty;
  }
  
  /**
   * @param pGeocodeProperty
   *   Sets the geocode property for store.
   */ 
  public void setGeocodeProperty(String pGeocodeProperty) {
    mGeocodeProperty = pGeocodeProperty;
  }
  
  //------------------------------------
  // property name: repository
  //------------------------------------
  private Repository mRepository = null;

  /**
   * @param pRepository 
   *   Sets the location repository.
   */
  public void setRepository( Repository pRepository ) {
    mRepository = pRepository;
  }
  
  /**
   * @return mRepository
   *   Returns the location repository.
   */
  public Repository getRepository() {
    return mRepository;
  }

  //------------------------------------
  // property name: itemDescriptor
  //------------------------------------
  private String mItemDescriptorName = null;

  /**
   * @param pItemDescriptorName
   *   Set the itemDescriptorName of the location repository.
   */
  public void setItemDescriptorName( String pItemDescriptorName ) {
    mItemDescriptorName = pItemDescriptorName;
  }

  /**
   * @return the mItemDescriptorName
   *   Get the name of item descriptor of the location repository.
   */
  public String getItemDescriptorName() {
    return mItemDescriptorName;
  }

  //------------------------------------
  // property name: storeProperties
  //------------------------------------
  private String[] mStoreProperties;

  /**
   * @param pStoreProperties 
   *   Set the list of properties to be populated into content item from repository.
   */
  public void setStoreProperties( String[] pStoreProperties ) {
    mStoreProperties = pStoreProperties;
  }

  /**
   * @return mStoreProperties
   *   Get the list of properties to be populated into content item from the repository.
   */
  public String[] getStoreProperties() {
    return mStoreProperties;
  }

  //----------------------------------------------------------------------------
  // METHODS
  //----------------------------------------------------------------------------  
  
  /**
   * Adds distance sorters based on the geo filter
   * 
   * @param pCartridgeConfig
   *   LocationResultsList Cartridge configuration.
   * @throws CartridgeHandlerException
   *   If an error occurs that is scoped to an individual cartridge
   *   instance. This exception will not halt the entire assembly process,
   *   which occurs across multiple cartridges; instead, this exception will
   *   be packaged in the overall response model. If an unchecked exception
   *   is thrown, then the entire assembly process will be halted.
   * 
   */
  public void preprocess(ResultsListConfig pCartridgeConfig)
    throws CartridgeHandlerException {
    if (this.getNavigationState().getFilterState().getGeoFilter() != null) {
      double latitude = this.getNavigationState().getFilterState()
        .getGeoFilter().getLatitude();
      double longitude = this.getNavigationState().getFilterState()
        .getGeoFilter().getLongitude();
      SortOptionConfig sorterAscending = new SortOptionConfig(DISTANCE_ASC,
        getGeocodeProperty() + OPEN_BRACE+ latitude + SEPARATOR_COMMA +
        longitude + CLOSE_BRACE + SEPARATOR_PIPE + ASCENDING);
      SortOptionConfig sorterDescending = new SortOptionConfig(DISTANCE_DSC,
        getGeocodeProperty() + OPEN_BRACE + latitude + SEPARATOR_COMMA +
        longitude + CLOSE_BRACE + SEPARATOR_PIPE + DESCENDING);
      this.getSortOptions().add(sorterAscending);
      this.getSortOptions().add(sorterDescending);
    }
    super.preprocess(pCartridgeConfig);
  }

  /**
   * Iterates through the records in results list and populates the content item
   * with the data from repository. 
   * 
   * @param pCartridgeConfig
   *   LocationResultsList cartridge configuration.
   * @return results
   *   List of stores returned from MDEX.
   * @throws CartridgeHandlerException
   *   if an error occurs that is scoped to an individual cartridge
   *   instance. This exception will not halt the entire assembly process,
   *   which occurs across multiple cartridges; instead, this exception will
   *   be packaged in the overall response model. If an unchecked exception
   *   is thrown, then the entire assembly process will be halted.
   *   
   */
  public ResultsList process(ResultsListConfig pCartridgeConfig)
    throws CartridgeHandlerException {
 
    ResultsList results = super.process(pCartridgeConfig);
    List<Record> records = results.getRecords();
    Iterator<Record> recordsIterator = records.iterator();
    while (recordsIterator.hasNext()) {
      Record record = recordsIterator.next();
  
      try{
        RepositoryItem storeItem = getRepository().getItem(
          record.getAttributes().get(STORE_REPOSITORY_ID).toString(),
          getItemDescriptorName());
          
        // add additional properties to each record from repository item if store exists in respository  
        // else remove the record from list to avoid exceptions.
        if (storeItem==null) {
          recordsIterator.remove();
        }
        else {
          for (String propertyName : getStoreProperties()) {
            Object property = storeItem.getPropertyValue(propertyName);
            if (property != null) {
              Attribute<Object> storeAttribute = new Attribute<>();
              storeAttribute.add(property);
              record.getAttributes().put(propertyName,storeAttribute);
            }
          }
        }
      }
      catch(RepositoryException re) {
        AssemblerTools.getApplicationLogging().vlogError(re,  
          "There was a problem populating the content item : ",
          record.getAttributes().get(STORE_REPOSITORY_ID));
      }
    }
    return results;
  }
  
}

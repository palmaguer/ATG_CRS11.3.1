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

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import atg.projects.store.assembler.cartridge.LocationSearchRadiusContentItem;

import com.endeca.infront.assembler.BasicContentItem;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.NavigationCartridgeHandler;
import com.endeca.infront.navigation.NavigationState;
import com.endeca.infront.navigation.request.BreadcrumbsMdexQuery;
import com.endeca.infront.navigation.request.MdexRequest;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.Navigation;

/**
 * Handler for the location search radius cartridge. This class is responsible for creating
 * and initializing the LocationSearchRadiusContentItem. It extends the NavigationCartridgeHandler.
 *
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/LocationSearchRadiusHandler.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class LocationSearchRadiusHandler 
  extends NavigationCartridgeHandler<ContentItem, LocationSearchRadiusContentItem> { 

  /** Class version string. */
  public static final String CLASS_VERSION = 
    "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/cartridge/handler/LocationSearchRadiusHandler.java#3 $$Change: 1536476 $";

  //--------------------------------------------------------------------------
  // MEMBERS
  //--------------------------------------------------------------------------
  
  private MdexRequest mMdexRequest = null;
  
  //--------------------------------------------------------------------------
  // PROPERTIES
  //--------------------------------------------------------------------------
  
  //----------------------------------------------
  // property: searchRadius
  //----------------------------------------------
  private List<String> mSearchRadius = null;
  
  /**
   * @param pSearchRadius
   *   The Search Radius list to be displayed on screen.
   */
  public void setSearchRadius(List<String> pSearchRadius) {
    mSearchRadius = pSearchRadius;
  }
  
  /**
   * @return
   *   The Search Radius to be used.
   */
  public List<String> getSearchRadius() {
    return mSearchRadius;
  }
  
  //--------------------------------------------------------------------------
  // METHODS
  //--------------------------------------------------------------------------

  /**
   * Returns sorted list of search radius
   *  
   * @return
   *   Sorted list of search radius.
   */
  public List<String> getSortedSearchRadius() {
    List<String> sortedList = new ArrayList<String>(mSearchRadius);
    Collections.sort(sortedList, new Comparator<String>() {
      @Override
      public int compare(String key1, String key2) {
        return (new Integer(key1.trim())).compareTo(new Integer(key2.trim()));
      }
    });
    return sortedList;
  }

  //--------------------------------------------------------------------------
  /**
   * Create a new BasicContentItem using the passed in ContentItem.
   * 
   * @param pContentItem
   *   The configuration content item for this cartridge handler. This will either be 
   *   the fully initialized configuration object, if a {@link ContentItemInitializer} 
   *   has been set, or it will simply be the instance configuration.
   * @return
   *   An instance of <code>BasicContentItem</code> which wraps the input {@link ContentItem}.
   */
  @Override
  protected ContentItem wrapConfig(ContentItem pContentItem) {
    return new BasicContentItem(pContentItem);
  }

  //--------------------------------------------------------------------------
  /**
   * Currently only used to create an MdexRequest. Does not execute it.
   * 
   * @param pCartridgeConfig
   *   The LocationSearchRadius refinement cartridge configuration.
   * 
   * @throws 
   *   CartridgeHandlerException if the operation fails.
   */
  @Override
  public void preprocess(ContentItem pCartridgeConfig) throws CartridgeHandlerException {
    // Create the request.  Do not execute it yet.
    mMdexRequest = createMdexRequest(getNavigationState().getFilterState(), new BreadcrumbsMdexQuery());
  }
  
  //--------------------------------------------------------------------------
  /**
   * Create a new LocationSearchRadiusContentItem. 
   * 
   * @param pCartridgeConfig
   *   The Geo Filter cartridge configuration.
   * @return
   *   A LocationSearchRadiusContentItem with search radius configured.
   * @throws 
   *   CartridgeHandlerException if a NavigationException is caught.
   */
  @Override
  public LocationSearchRadiusContentItem process(ContentItem pCartridgeConfig) 
    throws CartridgeHandlerException  {
    
    ENEQueryResults results = executeMdexRequest(mMdexRequest);
    NavigationState navigationState = getNavigationState();
    navigationState.inform(results);
    
    // Create a default radius refinement then configure it.
    LocationSearchRadiusContentItem searchRadius = new LocationSearchRadiusContentItem(pCartridgeConfig);
    searchRadius.setSearchRadius(getSortedSearchRadius());
    
    Navigation navigation = results.getNavigation();
    if (navigation != null) {
      long numResults = 0;
      
      // Total number of aggregated records.
      if (navigationState.getFilterState().getRollupKey() != null) {
        numResults = navigation.getTotalNumAggrERecs();
      }
      // Non rolled up records.
      else {
        numResults = navigation.getTotalNumERecs();
      }

      if (numResults < 1) {
        searchRadius.setEnabled(false);
      }
    }
    
    // Now also hide this refinement if "search near me / geo search" was not selected.
    if (navigationState.getFilterState().getGeoFilter() == null) {
      searchRadius.setEnabled(false);
    }
	
    return searchRadius;
  } 
  
}

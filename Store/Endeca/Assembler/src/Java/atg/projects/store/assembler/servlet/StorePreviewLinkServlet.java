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

package atg.project.store.assembler.servlet;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.endeca.infront.navigation.NavigationStateBuilder;
import atg.endeca.servlet.NucleusPreviewLinkServlet;
import atg.servlet.ServletUtil;

/**
 * Extends the {@link NucleusPreviewLinkServlet} to provide the applicable
 * {@link NavigationStateBuilder} components as per experience manager pages.
 * 
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/servlet/StorePreviewLinkServlet.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $$Author: releng $
 */
public class StorePreviewLinkServlet extends NucleusPreviewLinkServlet {

  /** Class version string */
  public static final String CLASS_VERSION = 
    "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/assembler/servlet/StorePreviewLinkServlet.java#3 $$Change: 1536476 $";

  //--------------------------------------------------------------------------
  //  CONSTANTS
  //--------------------------------------------------------------------------
  
  /** The servlet parameter used to specify map of xmgr page vs {@link NavigationStateBuilder} Nucleus component to use. */
  public static final String NAVIGATION_STATE_BUILDER_COMPONENT_MAP_PARAM = "navigationStateBuilderComponentMap";
  
  //--------------------------------------------------------------------------
  //  PROPERTIES
  //--------------------------------------------------------------------------

  private String navigationStateBuilderComponent = null;
  private HashMap<String, String> navigationStateBuilderComponentMap = null;
  
  //--------------------------------------------------------------------------
  //  METHODS
  //--------------------------------------------------------------------------
  
  //--------------------------------------------------------------------------
  /**
   * Initialize the servlet and retrieve the mapping between xmgr path and 
   * NavigationStateBuilder component to be used in the super class.
   * 
   * @param pConfig - The servlet configuration object.
   * 
   * @throws ServletException When the required mapping for 
   *          NavigationStateBuilder component can't be resolved.
   */
  @Override
  public void init(ServletConfig pConfig) throws ServletException {
    
    super.init(pConfig);
    
    // Retrieve the required component paths from the web app configuration.
    navigationStateBuilderComponent = pConfig.getInitParameter(NAVIGATION_STATE_BUILDER_COMPONENT_PARAM);
    String navigationStateBuilderComponentMapString = pConfig.getInitParameter(NAVIGATION_STATE_BUILDER_COMPONENT_MAP_PARAM);
    
    navigationStateBuilderComponentMap = new HashMap<String, String>();
    
    if (navigationStateBuilderComponentMapString != null) {
      String[] keyValuePairs = navigationStateBuilderComponentMapString.split(",");
      for(String pair : keyValuePairs){
        String[] keyAndValue = pair.split("=");
        if(keyAndValue.length > 1){
          navigationStateBuilderComponentMap.put(keyAndValue[0].trim(), keyAndValue[1].trim());
        }
      }
    }
  }

  //--------------------------------------------------------------------------
  /**
   * @return The resolved NavigationStateBuilder component.
   */
  @Override
  protected NavigationStateBuilder getNavigationStateBuilder() {
    String contentUri = ServletUtil.getCurrentRequest().getQueryParameter("__contentUri");
    String xmPage = contentUri.substring(contentUri.lastIndexOf('/'));
    
    if(navigationStateBuilderComponentMap.containsKey(xmPage)){
      return (NavigationStateBuilder) ServletUtil.getCurrentRequest().resolveName(navigationStateBuilderComponentMap.get(xmPage), true);
    } else {
      return (NavigationStateBuilder) ServletUtil.getCurrentRequest().resolveName(navigationStateBuilderComponent, true);
    }
  }
}

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

package atg.projects.store.droplet;

import atg.commerce.util.PlaceList;
import atg.commerce.util.PlaceUtils;
import atg.nucleus.naming.ParameterName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.repository.RepositoryItem;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;

/**
 * <p>
 *   Given a country code this droplet returns a list of states.
 * </p>
 *
 * <p>
 *   This droplet uses {@link PlaceUtils#getLocalizedSortedPlaces(String, Locale)} to obtain list of 
 *   localized sorted places. If you prefer different sorting, you need to override this method.
 * </p>
 *
 * <p>
 *   This droplet takes the following input parameters:
 *   <ul>
 *     <li>
 *       countryCode - Country Code
 *     </li>
 *     <li>
 *       stateCodes -  a collection of store repository items
 *     </li>
 *     <li>
 *       userLocale - optional parameter. 
 *         Specify current locale for user. If no locale specified as parameter,
 *         use locale from pRequest.RequestLocale.
 *     </li>
 *   </ul>
 *   or
 *   <ul>
 *     <li>
 *       countryCode - Country Code
 *     </li>
 *     <li>
 *       stateCode -  Code of a state
 *     </li>
 *     <li>
 *       userLocale - optional parameter 
 *         Specify current locale for user. If no locale specified as parameter,
 *         use locale from pRequest.RequestLocale.
 *     </li>
 *   </ul>
 * </p>
 *
 * <p>
 *   This droplet renders the following oparams:
 *   <ul>
 *     <li>
 *       output - always rendered.
 *     </li>
 *   </ul>
 * </p> 
 *
 * <p>
 *   This droplet sets the following output parameters:
 *   <ul>
 *     <li>
 *       states - The list of states set using the country code input parameter. 
 *         This will only be set when no stateCode input parameter is available. 
 *     </li>
 *     <li>
 *       state - The state name set using the state code and country code input parameters. 
 *               This will only be set when the stateCode input parameter is available. 
 *     </li>
 *   </ul>
 * </p>
 * 
 * <p>
 * Example:
 * <pre>
 * 
 * &lt;dsp:droplet name="StateListFilterDroplet"&gt;
 *   &lt;dsp:param name="countryCode" param="country" /&gt;
 *   &lt;dsp:param name="stateCodes" param="filteredCollection" /&gt;
 *     &lt;dsp:oparam name="output"&gt;
 *       &lt;dsp:getvalueof var="states" param="states"/&gt;
 *       &lt;%-- Check if selected country have states --%&gt;
 *       &lt;c:set var="numOfStates" value="${fn:length(states)}"/&gt;
 *       &lt;c:if test="${numOfStates &gt; 0}"&gt;
 *         &lt;c:forEach var="state" items="${states}" varStatus="counter"&gt;
 *           &lt;dsp:param name="state" value="${state}"/&gt;
 *           &lt;dsp:getvalueof var="code" vartype="java.lang.String" param="state.code"&gt;
 *             &lt;option value="${code}"&gt;&lt;dsp:valueof param="state.displayName"/&gt;&lt;/option&gt;
 *           &lt;/dsp:getvalueof&gt;
 *         &lt;/c:forEach&gt;
 *       &lt;/c:if&gt; 
 *     &lt;/dsp:oparam&gt;
 * &lt;/dsp:droplet&gt;
 * 
 * </pre>
 * </p>
 *
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/src/atg/projects/store/droplet/StateListFilterDroplet.java#3 $$;
 * @updated $DateTime: 2018/04/13 08:11:14 $
 */
public class StateListFilterDroplet extends DynamoServlet {

  //-------------------------------------
  /** Class version string */

  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/src/atg/projects/store/droplet/StateListFilterDroplet.java#3 $$Change: 1536476 $";



  //----------------------------------------------------------------------------
  // Constants
  //----------------------------------------------------------------------------

  /**
   * Country code parameter name.
   */
  public static final ParameterName COUNTRY_CODE = ParameterName.getParameterName("countryCode");

  /**
   * State codes parameter name.
   */
  public static final ParameterName STATE_CODES = ParameterName.getParameterName("stateCodes");
  
  /**
   * State code parameter name.
   */
  public static final ParameterName STATE_CODE = ParameterName.getParameterName("stateCode");
  
  /**
   * User locale parameter name.
   */
  public static final ParameterName USER_LOCALE = ParameterName.getParameterName("userLocale");

  /**
   * State parameter name.
   */
  public static final String STATE_PARAM = "state";
  
  /**
   * States parameter name.
   */
  public static final String STATES_PARAM = "states";

  /**
   * Output parameter name.
   */
  public static final String OUTPUT_OPARAM = "output";
  
  //----------------------------------------------------------------------------
  // PROPERTIES
  //----------------------------------------------------------------------------
  
  //------------------------------------
  // property: placeUtils
  //------------------------------------
  private PlaceUtils mPlaceUtils;

  /**
   * @return 
   *   The PlaceUtils.
   */
  public PlaceUtils getPlaceUtils() {
    return mPlaceUtils;
  }

  /**
   * @param pPlaceUtils
   *   The PlaceUtils to set.
   */
  public void setPlaceUtils(PlaceUtils pPlaceUtils) {
    mPlaceUtils = pPlaceUtils;
  }

  /**
   * Removes the states that do not have stores from the country state list.
   *
   * @param pRequest
   *   HTTP request.
   * @param pResponse 
   *   HTTP response.
   * @throws ServletException
   *   If an error occurs.
   * @throws IOException
   *   If an error occurs.
   * @see PlaceUtils#getLocalizedSortedPlaces(String, Locale)
   */
  public void service(DynamoHttpServletRequest pRequest,
                      DynamoHttpServletResponse pResponse)
    throws ServletException, IOException {
    String countryCode = (String) pRequest.getObjectParameter(COUNTRY_CODE);
    Object stateCodes =  pRequest.getObjectParameter(STATE_CODES);
    Locale locale = getLocale(pRequest);
    PlaceList.Place[] localizedSortedPlaces =
      getPlaceUtils().getLocalizedSortedPlaces(countryCode, locale);
    
    if (stateCodes != null) {
      Object[] stateCodeCollection = extractStateCode((Collection)stateCodes);
      PlaceList.Place[] filteredLocalizedSortedPlaces = new PlaceList.Place[stateCodeCollection.length];
      //Locate state code in the array of places to obtain the selected place name.
      int numberOfPlaces = 0;
      for (PlaceList.Place place : localizedSortedPlaces) {
        int counter = 0;
        while (counter < stateCodeCollection.length) {
          if (place.getCode().equals(stateCodeCollection[counter])) {
            filteredLocalizedSortedPlaces[numberOfPlaces] = place;
            numberOfPlaces++;
            break;
          }
          counter++;
        }
      }  
      pRequest.setParameter(STATES_PARAM, filteredLocalizedSortedPlaces);
      pRequest.serviceLocalParameter(OUTPUT_OPARAM, pRequest, pResponse);
    }
    else {
       Object stateCode =  pRequest.getObjectParameter(STATE_CODE);
       String stateName = null;
       // Locate state code in the array of places to obtain the selected place name.
       for (PlaceList.Place place : localizedSortedPlaces) {
         if (place.getCode().equals(stateCode)) {
           stateName = place.getDisplayName();
           break;
         }
       }
       pRequest.setParameter(STATE_PARAM, stateName);
       pRequest.serviceLocalParameter(OUTPUT_OPARAM, pRequest, pResponse);
    }   
  }

  /**
   * Returns Locale object. If no locale specified as parameter,
   * use locale from pRequest.RequestLocale. 
   * 
   * @param pRequest 
   *   Dynamo HTTP request object.
   * @return locale
   */
  public Locale getLocale(DynamoHttpServletRequest pRequest) {
    Locale locale = (Locale) pRequest.getObjectParameter(USER_LOCALE);
    if (locale == null) {
      locale = pRequest.getRequestLocale().getLocale();
    }
    vlogDebug("Locale is {0}", locale);
    return locale;
  }
  
  /**
   * This method takes a collection of store RepositoryItems and
   * returns an array of state codes having at least one store.
   * 
   * @param pStoreRepositoryItems 
   *   Collection of store RepositoryItems.
   * @return states 
   *   An array of state codes.
   */
  public Object[] extractStateCode(Collection pStoreRepositoryItems) {
    int counter = 0;
    String storeAddress = null;
    Set<String> states = new HashSet<String>();
    Object[] storeRepositoryArray =  pStoreRepositoryItems.toArray();
    while (counter < pStoreRepositoryItems.size()) {
      storeAddress = (String)((RepositoryItem)
        storeRepositoryArray[counter]).getPropertyValue("stateAddress");
      states.add(storeAddress);
      counter++;  
    }
    return states.toArray();
  }
}

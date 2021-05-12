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

import java.io.IOException;

import javax.servlet.ServletException;

import atg.nucleus.naming.ParameterName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

/**
 * <p>
 *   This Droplet is used to extract the distance of store from current location.
 * </p>
 * 
 * <p>
 * Example1:
 * <pre>
 * &lt;dsp:droplet name="StoreDistanceDroplet"&gt;
 *   &lt;dsp:param name="variable" value="${recordAttribute}"/&gt; 
 *   &lt;dsp:param name="fraction" value="3"/&gt;
 *   &lt;dsp:oparam name="output"&gt;
 *     &lt;dsp:getvalueof var="distance" param="distance"/&gt;
 *   &lt;/dsp:oparam&gt;
 * &lt;/dsp:droplet&gt;
 * </pre>
 * </p>
 * 
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/src/atg/projects/store/droplet/StoreDistanceDroplet.java#3 $
 * @updated $DateTime: 2018/04/13 08:11:14 $
 */
public class StoreDistanceDroplet extends DynamoServlet {

  //-------------------------------------
  /** Class version string */

  public static String CLASS_VERSION = "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/src/atg/projects/store/droplet/StoreDistanceDroplet.java#3 $$Change: 1536476 $";


  
  //----------------------------------------------------------------------------
  // Constants
  //----------------------------------------------------------------------------
  
  /** Endeca generated record attributes */
  static final ParameterName PARAM_VARIABLE = ParameterName.getParameterName("variable");
  
  /** Numeric value to be formatted upto maximum Number of decimal places */
  static final ParameterName PARAM_VALUE = ParameterName.getParameterName("value");
  
  /** Maximum Number of decimal places */
  static final ParameterName PARAM_FRACTION = ParameterName.getParameterName("fraction");
  
  static final String OUTPUT_OPARAM = "output";
  static final String PARAM_DISTANCE = "distance";
  
  //----------------------------------------------------------------------------
  // PROPERTIES
  //----------------------------------------------------------------------------

  //------------------------------------
  //   property:numberOfDecimalPlaces
  //------------------------------------
  int mNumberOfDecimalPlaces = 0;
  
  /**
   * @return mNumberOfDecimal
   *   Number of decimal places.
   */
  public int getNumberOfDecimalPlaces() {
    return mNumberOfDecimalPlaces;
  }

  /**
   * @param pNumberOfDecimal
   *   Number of decimal places.
   */
  public void setNumberOfDecimalPlaces(int pNumberOfDecimalPlaces) {
    mNumberOfDecimalPlaces = pNumberOfDecimalPlaces;
  }

  //------------------------------------
  //   property:distanceVariable
  //------------------------------------
  String mDistanceVariable = null;
  
  /**
   * @return mDistanceVariable
   *   Name of distance variable.
   */
  public String getDistanceVariable() {
    return mDistanceVariable;
  }
  
  /**
   * @param pDistanceVariable
   *   Name of distance variable.
   */
  public void setDistanceVariable(String pDistanceVariable) {
      mDistanceVariable = pDistanceVariable;
  }
  
  /**
   * Extracts the store distance from Endeca generated record attributes.
   *
   * @param pRequest 
   *   A <code>DynamoHttpServletRequest</code> value.
   * @param pResponse 
   *   A <code>DynamoHttpServletResponse</code> value.
   * @exception ServletException 
   *   If an error occurs.
   * @exception IOException 
   *   If an error occurs.
   */
  public void service(DynamoHttpServletRequest pRequest,
                      DynamoHttpServletResponse pResponse)
    throws ServletException, IOException {

    double formatValue = 0;
    //Default maximum fraction digits(if fraction parameter is null).
    int maxNumberOfDecimalPlaces = getNumberOfDecimalPlaces();
    Object maxDecimalPlacesObject = pRequest.getParameter(PARAM_FRACTION);
    if(maxDecimalPlacesObject != null){
      maxNumberOfDecimalPlaces = Integer.parseInt((String)maxDecimalPlacesObject);
    }
    Object recordAttributeObject = pRequest.getParameter(PARAM_VARIABLE);
    if(recordAttributeObject != null) {
      vlogDebug("Use item descriptor parameter {0}", (String)recordAttributeObject);
      formatValue = extractValue((String)recordAttributeObject);
      if(formatValue >= 0){
        formatValue = formatDistance(formatValue, maxNumberOfDecimalPlaces);
      }
    }
    Object valueObject = pRequest.getParameter(PARAM_VALUE);
    if(valueObject != null) {
      formatValue = Double.parseDouble((String) valueObject);
      if(formatValue >= 0) {
        formatValue = formatDistance(formatValue, maxNumberOfDecimalPlaces);    
      }
    }
    
    if(recordAttributeObject != null && formatValue >= 0) {
      pRequest.setParameter(PARAM_DISTANCE, formatValue);
    }
    else {
       if(valueObject != null && formatValue >= 0) {
         pRequest.setParameter(PARAM_DISTANCE, formatValue);
       }
    }
    pRequest.serviceLocalParameter(OUTPUT_OPARAM, pRequest, pResponse);
     
  }
  
  /**
   * This method extracts the value of distance variable from 
   * recordAttribute that is dynamically generated by Endeca.
   * 
   * @param pRecordAttribute
   *   A string that holds the value of the Endeca generated content item.
   * @return value
   *   Distance of store in kilometer. 
   * 
   */
  public double extractValue(String pRecordAttribute){
    double value = -1;
    if(pRecordAttribute.contains(getDistanceVariable())){
      String subRecordAttribute = null;
      int endIndex = pRecordAttribute.length();
      String modifiedRecordAttribute = pRecordAttribute.substring(0, endIndex-2);
      int startIndex = modifiedRecordAttribute.indexOf(getDistanceVariable());
      subRecordAttribute = modifiedRecordAttribute.substring(startIndex);
      String distanceName[] = subRecordAttribute.split(",");
      String storeDistance[] = distanceName[1].split("=");
      value = Double.parseDouble(storeDistance[1]);
    }
    return value;
  }
  
  /**
   * This method is used to round off the given double number
   * up to maxNumberOfDecimalPlaces.
   * 
   * @return roundOff
   *   Value after round off.
   * @param formatValue
   *   Input value to format.
   * @param maxNumberOfDecimalPlaces
   *   Maximum number of decimal places.
   */
  public double formatDistance(double formatValue, int maxNumberOfDecimalPlaces){
    double roundOff = Math.round(formatValue * Math.pow(10, maxNumberOfDecimalPlaces))
                        / Math.pow(10, maxNumberOfDecimalPlaces);
    return roundOff;
  }
} 

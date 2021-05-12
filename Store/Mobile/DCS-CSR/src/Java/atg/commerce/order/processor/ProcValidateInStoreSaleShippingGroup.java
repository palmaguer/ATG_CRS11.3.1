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

package atg.commerce.order.processor;

import atg.commerce.catalog.CatalogTools;
import atg.commerce.order.CommerceItemRelationship;
import atg.commerce.order.InStoreSaleShippingGroup;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.Order;
import atg.commerce.order.PipelineConstants;
import atg.commerce.order.ShippingGroup;
import atg.core.i18n.LayeredResourceBundle;
import atg.core.util.ResourceUtils;
import atg.nucleus.logging.ApplicationLoggingImpl;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class validates an in-store sale shipping group.
 *
 * @author pmacrory
 * @version: $Id: //product/DAS/main/Java/atg
 * @updated: $DateTime: 10:01
 */
public class ProcValidateInStoreSaleShippingGroup extends ApplicationLoggingImpl implements PipelineProcessor {
  /** Class version string */
  public static String CLASS_VERSION =
      "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/src/atg/commerce/order/processor/ProcValidateInStoreSaleShippingGroup.java#2 $$Change: 1503965 $";

  static final String RESOURCE_NAME = "atg.commerce.order.OrderResources";
  static final String USER_MSGS_RES_NAME = "atg.commerce.order.UserMessages";

  //---------------------------------------------------------------------------
  // Resource bundles

  private static ResourceBundle sResourceBundle =
      LayeredResourceBundle.getBundle(RESOURCE_NAME, atg.service.dynamo.LangLicense.getLicensedDefault());
  private static ResourceBundle sUserResourceBundle =
      LayeredResourceBundle.getBundle(USER_MSGS_RES_NAME, java.util.Locale.getDefault());

  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------/

  private static final String MISSING_LOCATION_ID = "ShipLocationMissing";
  private static final String INVALID_LOCATION_ID = "ShipLocationInvalid";
  private static final String ONLINE_ONLY_ITEM_WITH_IN_STORE_SHIPPING = "OnlineOnlyItemWithInStoreShipping";

  private final int SUCCESS = 1;


  //---------------------------------------------------------------------------
  // Properties
  //---------------------------------------------------------------------------/

  //---------------------------------------------------------------------------
  // Property: catalogTools
  private CatalogTools mCatalogTools;

  /**
   * Sets the catalog tools
   * @param pCatalogTools the catalog tools to set
   */
  public void setCatalogTools(CatalogTools pCatalogTools) {
    mCatalogTools = pCatalogTools;
  }

  /**
   * Gets the catalog tools.
   * @return the catalog tools.
   */
  public CatalogTools getCatalogTools() {
    return mCatalogTools;
  }

  //---------------------------------------------------------------------------
  // Property: validateLocationId
  private boolean mValidateLocationId;

  /**
   * Sets the property validateLocationId. If true the location id on the
   * shipping group will be validated.
   * @param pValidateLocationId The validateLocationId to set
   */
  public void setValidateLocationId(boolean pValidateLocationId) {
    mValidateLocationId = pValidateLocationId;
  }

  /**
   * Gets the property validateLocationId. If true the location id on the
   * shipping group will be validated.
   * @return the value of validateLocationId
   */
  public boolean isValidateLocationId() {
    return mValidateLocationId;
  }

  //---------------------------------------------------------------------------
  // Property: locationRepository
  private Repository mLocationRepository;

  /**
   * The locationRepository to set
   * @param pLocationRepository location repository to set
   */
  public void setLocationRepository(Repository pLocationRepository) {
    mLocationRepository = pLocationRepository;
  }

  /**
   * Get the locationRepository. This repository, if set will be checked to ensure that the locationId on the
   * InStoreSaleShippingGroup is a valid item for the repository
   * @return the location repository
   */
  public Repository getLocationRepository() {
    return mLocationRepository;
  }

  //---------------------------------------------------------------------------
  // Property: locationItemType
  private String mLocationItemType;

  /**
   * Sets the locationItemType property
   * @param pLocationItemType the locationItemType to set
   */
  public void setLocationItemType(String pLocationItemType) {
    mLocationItemType = pLocationItemType;
  }

  /**
   * Gets the locationItemType property. This is the item descriptor that the locationId corresponds to. If set along with
   * locationRepository then the locationId will be validated as a valid item in the repository.
   * @return the location item type
   */
  public String getLocationItemType() {
    return mLocationItemType;
  }

  //---------------------------------------------------------------------------

  /**
   * Returns the set of valid return codes for this processor.
   * This processor always returns a value of 1, indicating successful
   * completion.  If any errors occur during validation, they are added
   * to the pipeline result object where the caller can examine them.
   **/

  public int[] getRetCodes()
  {
    int[] ret = {SUCCESS};
    return ret;
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Verify that all required properties are present in an instance
   * of InStoreSaleShippingGroup.  By default the only required property
   * is the locationId.
   **/
  public int runProcess(Object pParam, PipelineResult pResult) throws Exception
  {
    ValidateShippingGroupPipelineArgs args = (ValidateShippingGroupPipelineArgs)pParam;
    Order order = args.getOrder();
    Locale locale = args.getLocale();
    ShippingGroup shippingGroup = args.getShippingGroup();
    ResourceBundle resourceBundle;

    if (locale == null)
      resourceBundle = sUserResourceBundle;
    else
      resourceBundle = LayeredResourceBundle.getBundle(USER_MSGS_RES_NAME, locale);

    if (shippingGroup == null)
      throw new InvalidParameterException(
          ResourceUtils.getMsgResource("InvalidShippingGroupParameter", RESOURCE_NAME, sResourceBundle));

    if (!(shippingGroup instanceof InStoreSaleShippingGroup))
      throw new InvalidParameterException(
          ResourceUtils.getMsgResource("InvalidShippingGroupParameter", RESOURCE_NAME, sResourceBundle));

    if (isLoggingDebug())
      logDebug("Validating one InStoreSaleShippingGroup of type " + shippingGroup.getShippingGroupClassType());

    validateInStoreSaleShippingGroupFields((InStoreSaleShippingGroup) shippingGroup, pResult, resourceBundle);
    validateOnlineOnly((InStoreSaleShippingGroup)shippingGroup, pResult, resourceBundle);

    return SUCCESS;
  }

  /**
   * This method validates the InStoreSaleShippingGroup properties.  By default the location Id is checked.
   * If the locationId is empty an error is thrown. If locationRepository and locationItemType are set then an item
   * of the provided type will be retrieved from the locationRepository. If the item does not exist then an error is thrown.
   *
   * @param pShippingGroup
   *    an InStoreSaleShippingGroup object to validate
   * @param pResult
   *    the PipelineResult object which was supplied in runProcess()
   * @param pResourceBundle
   *    a resource bundle containing error messages
   * @throws atg.repository.RepositoryException
   **/
  protected void validateInStoreSaleShippingGroupFields(InStoreSaleShippingGroup pShippingGroup, PipelineResult pResult, ResourceBundle pResourceBundle) throws RepositoryException
  {
    if(isValidateLocationId()) {
      if(pShippingGroup.getLocationId() == null || pShippingGroup.getLocationId().trim().length() == 0) {
        String msg = pResourceBundle.getString(MISSING_LOCATION_ID);
        addHashedError(pResult, PipelineConstants.MISSINGSHIPPINGLOCATIONID, pShippingGroup.getId(), msg);
      }
      else if(getLocationRepository() != null && getLocationItemType() != null) {
        RepositoryItem location = getLocationRepository().getItem(pShippingGroup.getLocationId(),getLocationItemType());
        if(location == null) {
          String msg = pResourceBundle.getString(INVALID_LOCATION_ID);
          addHashedError(pResult, PipelineConstants.INVALIDSHIPPINGLOCATIONID, pShippingGroup.getId(), msg);
        }
      }
    }
  }

  /**
   * This method validates the InStoreSaleShippingGroup does not contain any relationships to an item that is marked
   * as online only.
   * @param pShippingGroup The shipping group
   * @param pResult
   *    the PipelineResult object which was supplied in runProcess()
   * @param pResourceBundle
   *    a resource bundle containing error messages
   */
  protected void validateOnlineOnly(InStoreSaleShippingGroup pShippingGroup, PipelineResult pResult, ResourceBundle pResourceBundle) throws Exception{
    List<CommerceItemRelationship> cirs = pShippingGroup.getCommerceItemRelationships();
    for(CommerceItemRelationship cir : cirs) {
      RepositoryItem prod = (RepositoryItem)cir.getCommerceItem().getAuxiliaryData().getProductRef();
      RepositoryItem sku = (RepositoryItem)cir.getCommerceItem().getAuxiliaryData().getCatalogRef();

      if(getCatalogTools().isOnlineOnly(prod, sku)) {
        String msg = pResourceBundle.getString(ONLINE_ONLY_ITEM_WITH_IN_STORE_SHIPPING);
        addHashedError(pResult, PipelineConstants.ONLINEONLYITEMWITHINSTORESHIPPING, pShippingGroup.getId(),
            MessageFormat.format(msg, pShippingGroup.getId(), prod.getRepositoryId(), sku.getRepositoryId()));
      }
    }
  }

  /**
   * This method adds an error to the PipelineResult object. This method,
   * rather than just storing a single error object in pResult, stores a Map
   * of errors. This allows more than one error to be stored using the same
   * key in the pResult object. pKey is used to reference a HashMap of
   * errors in pResult. So, calling pResult.getError(pKey) will return an
   * object which should be cast to a Map.  Each entry within the map is
   * keyed by pId and its value is pError.
   *
   * @param pResult the PipelineResult object supplied in runProcess()
   * @param pKey the key to use to store the HashMap in the PipelineResult object
   * @param pId the key to use to store the error message within the HashMap in the
   *            PipelineResult object
   * @param pError the error object to store in the HashMap
   * @see atg.service.pipeline.PipelineResult
   * @see #runProcess(Object, PipelineResult)
   */
  protected void addHashedError(PipelineResult pResult, String pKey, String pId, Object pError)
  {
    Object error = pResult.getError(pKey);
    if (error == null) {
      HashMap map = new HashMap(5);
      pResult.addError(pKey, map);
      map.put(pId, pError);
    }
    else if (error instanceof Map) {
      Map map = (Map) error;
      map.put(pId, pError);
    }
  }
}

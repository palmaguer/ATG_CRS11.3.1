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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;

import atg.commerce.catalog.CatalogTools;
import atg.commerce.endeca.cache.DimensionValueCacheObject;
import atg.commerce.endeca.cache.DimensionValueCacheTools;
import atg.core.util.StringUtils;
import atg.projects.store.util.DocumentLinksService;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

/**
 * <p>
 *   This is an Endeca extension of {@link DocumentLinksDroplet} that enables
 *   document link generation for Endeca driven pages.
 * </p>
 * <p>
 *   This extended droplet takes the following optional extra input parameter:
 *   <dl>
 *     <dt>canonicalLink</dt>
 *     <dd>
 *       A canonical link that will be most likely be retrieved from the 
 *       rootContentItem. This could be null, for example, if on a non-Endeca 
 *       driven page.
 *     </dd>
 *   </dl>
 * </p>
 * Example:
 *   <pre>
 *     &lt;dsp:droplet name="DocumentLinksDroplet"
 *                        currentCategory="${currentCategory}"
 *                        currentProduct="${currentProduct}"
 *                        currentSiteId="${siteId}"
 *                        canonicalLink="${canonicalLink}"
 *                        var="linkDetails"&gt;
 *       &lt;dsp:oparam name="output"&gt;
 *                       
 *         &lt;c:choose&gt;
 *           &lt;c:when test="${linkDetails.rel == 'alternate'}"&gt;                           
 *             &lt;link rel="${linkDetails.rel}" 
 *                   lang="${linkDetails.lang}" 
 *                   hreflang="${linkDetails.hreflang}" 
 *                   href="${httpServer}${linkDetails.href}"/&gt;
 *           &lt;/c:when&gt;
 *           &lt;c:otherwise&gt;
 *             &lt;link rel="${linkDetails.rel}" 
 *                   href="${httpServer}${linkDetails.href}"/&gt;
 *            &lt;/c:otherwise&gt;
 *         &lt;/c:choose&gt;    
 *       &lt;/dsp:oparam&gt;    
 *     &lt;/dsp:droplet&gt;
 *   <pre>
 * 
 * @author Oracle
 * @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/droplet/EndecaDocumentLinksDroplet.java#3 $$Change: 1536476 $
 * @updated $DateTime: 2018/04/13 08:11:14 $
 */
public class EndecaDocumentLinksDroplet extends DocumentLinksDroplet {
  
  /** Class version string */
  public static String CLASS_VERSION = 
    "$Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Endeca/Assembler/src/atg/projects/store/droplet/EndecaDocumentLinksDroplet.java#3 $$Change: 1536476 $";

  //----------------------------------------------------------------------------
  //  CONSTANTS
  //----------------------------------------------------------------------------
  
  /** The Endeca generated canonical link parameter. */
  public final static String CANONICAL_LINK = "canonicalLink";
  
  //----------------------------------------------------------------------------
  //  PROPERTIES
  //----------------------------------------------------------------------------
  
  //------------------------------------
  // property: dimensionValueCacheTools
  //------------------------------------
  private DimensionValueCacheTools mDimensionValueCacheTools = null;
  
  /**
   * @return 
   *   The DimensionValueCacheTools component.
   */
  public DimensionValueCacheTools getDimensionValueCacheTools() {
    return mDimensionValueCacheTools;
  }
  
  /**
   * @param 
   *   The DimensionValueCacheTools component.
   */  
  public void setDimensionValueCacheTools(
    DimensionValueCacheTools pDimensionValueCacheTools) {
    mDimensionValueCacheTools = pDimensionValueCacheTools;
  }
  
  //------------------------------------
  // property: catalogTools
  //------------------------------------
  private CatalogTools mCatalogTools = null;
  
  /**
   * @return
   *   The CatalogTools component.
   */
  public CatalogTools getCatalogTools() {
    return mCatalogTools;
  }
  
  /**
   * @param
   *   The CatalogTools component.
   */  
  public void setCatalogTools(CatalogTools pCatalogTools) {
    mCatalogTools = pCatalogTools;
  }
  
  //------------------------------------
  // property: navigationActionPath
  //------------------------------------
  private String mNavigationActionPath = "/browse";
  
  /**
   * @return
   *   The NavigationActionPath value. This will represent an Endeca driven
   *   category page.
   */
  public String getNavigationActionPath() {
    return mNavigationActionPath;
  }
  
  /**
   * @param
   *   The NavigationActionPath value. This will represent an Endeca driven
   *   category page.
   */  
  public void setNavigationActionPath(String pNavigationActionPath) {
    mNavigationActionPath = pNavigationActionPath;
  }
  
  //----------------------------------------------------------------------------
  // METHODS
  //----------------------------------------------------------------------------
  
  /**
   * See {@link EndecaDocumentLinksDroplet} for service details.
   * 
   * @param pRequest 
   *   The request to be processed.
   * @param pResponse 
   *   The response object for this request.
   * @throws ServletException 
   *   An application specific error occurred processing this request.
   * @throws IOException
   *   An error occurred reading data from the request or writing 
   *   data to the response.
   */
  @Override
  public void service(DynamoHttpServletRequest pRequest,
    DynamoHttpServletResponse pResponse) throws ServletException, IOException { 
    
    super.service(pRequest, pResponse);
    
    String canonicalLink = (String) pRequest.getObjectParameter(CANONICAL_LINK);
    boolean linksSet = false;
    
    RepositoryItem currentCategory = (RepositoryItem) 
      pRequest.getObjectParameter(DocumentLinksDroplet.CURRENT_CATEGORY);
    RepositoryItem currentProduct = (RepositoryItem) 
      pRequest.getObjectParameter(DocumentLinksDroplet.CURRENT_PRODUCT);
    
    if (!StringUtils.isEmpty(canonicalLink) 
        && (currentCategory == null && currentProduct == null)) {
      
      String url = removeParametersFromUrl(canonicalLink);
      
      if (!StringUtils.isEmpty(url)) {
        pRequest.setParameter(
          DocumentLinksDroplet.HREF, url);
        pRequest.setParameter(
          DocumentLinksDroplet.REL, DocumentLinksService.CANONICAL);
        pRequest.serviceLocalParameter(
          DocumentLinksDroplet.S_OUTPUT, pRequest, pResponse);
        
        linksSet = true;                  
        
        if (isLoggingDebug()) {
          logDebug("EndecaDocumentLinksDroplet output parameters rendered:");
        }
        
        vlogDebug("rel: {0} - href: {1}",
            DocumentLinksService.CANONICAL, url);
        
        linksSet = true;
      }
      
      Map<String,Locale> alternateLink = getDocumentLinksService()
        .getAlternateLink(pRequest, getRequestLocale());
      
      if (alternateLink != null) {
        Locale locale = 
          (Locale) alternateLink.get(DocumentLinksService.ALTERNATE);
                
        pRequest.setParameter(DocumentLinksDroplet.LOCALE, locale);
        
        if (canonicalLink != null) {
          
          if (StringUtils.isEmpty(url)) {
            url = removeParametersFromUrl(canonicalLink);
          }
          
          pRequest.setParameter(
            DocumentLinksDroplet.REL, DocumentLinksService.ALTERNATE);
          pRequest.setParameter(
            DocumentLinksDroplet.HREF, url);
          pRequest.setParameter(
            DocumentLinksDroplet.HREFLANG, locale.getLanguage());
          pRequest.setParameter(
            DocumentLinksDroplet.LANG, locale.getLanguage());
          pRequest.serviceLocalParameter(
            DocumentLinksDroplet.S_OUTPUT, pRequest, pResponse);      
          
          linksSet = true;
          
          if (isLoggingDebug()) {
            logDebug("EndecaDocumentLinksDroplet output parameters rendered:");
          }
          
          vlogDebug("rel: {0} - href: {1} - langhref: {2} - lang: {3}", 
            DocumentLinksService.ALTERNATE, url, locale.getLanguage(), 
            locale.getLanguage());
        }
      }
    }
    if (linksSet == true
        && pRequest.getParameter(DocumentLinksDroplet.S_EMPTY) != null) {
      pRequest.removeParameter(DocumentLinksDroplet.S_EMPTY.getName());
    }
  }
  
  //----------------------------------------------------------------------------
  /**
   * This method gets the canonical link for Endeca driven categories. Otherwise
   * the canonical link is retrieved from the super.getCanonicalLink method.
   * 
   * @param pItem 
   *   The item for which the canonical link will be generated.
   * @param pRequest 
   *   The HTTP request.
   * @return 
   *   The Endeca/ATG driven category canonical link for pItem, otherwise null. 
   */
  @Override
  protected String getCanonicalLink(Object pItem, 
    DynamoHttpServletRequest pRequest) {

    RepositoryItem item = (RepositoryItem) pItem;
    
    try {
      if (getCatalogTools().isCategory(item)) {
      
        List<DimensionValueCacheObject> dimensionValueForItem = 
          getDimensionValueCacheTools().get(item.getRepositoryId());

        if (dimensionValueForItem != null && dimensionValueForItem.size() > 0) {
          return dimensionValueForItem.get(0).getUrl();
        }
        
        return getNavigationActionPath();
      }
    }
    catch (RepositoryException re) {
      vlogError(re,
        "There was a problem trying to determine if item {0} is a category",
          item.getRepositoryId());
    }
    
    return super.getCanonicalLink(pItem, pRequest);
  }
  
  /**
   * Remove all parameters from the passed in URL i.e. everything
   * after the '?' delimiter. 
   * 
   * @param pUrl
   *   The URL that will have all parameters removed from.
   * @return
   *   The modified URL that will contain no parameters.
   */
  public String removeParametersFromUrl(String pUrl) {
    String result = null;
    int indexOfParamSeparator = pUrl.indexOf("?");
    
    // Remove any existing parameters from the URL.
    if (indexOfParamSeparator != -1) {
      vlogDebug("Removing parameters from the canonicalLink URL: {0}", 
        pUrl.substring(indexOfParamSeparator));
      
      result = pUrl.substring(0, indexOfParamSeparator) + "/";
    }
    else {
      result = pUrl + "/";
    }
    return result;
  }
  
}

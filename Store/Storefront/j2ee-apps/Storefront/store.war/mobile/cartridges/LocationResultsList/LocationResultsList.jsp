<%--
  This page is the Store Results List page.
  Displays store list for searched location.

  Required Parameters:
    contentItem
      The content item - results list type 
    
  Optional Parameters:
    None

--%>
<dsp:page>
  <dsp:importbean bean="/atg/store/droplet/StoreDistanceDroplet"/>
  <dsp:importbean bean="/atg/store/droplet/URLProcessor"/>
  <dsp:importbean bean="/atg/store/droplet/StoreUnitsOfMeasureConversionDroplet"/>
  <dsp:importbean bean="/atg/endeca/assembler/cartridge/manager/LocationActionPathProvider"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/multisite/SiteContext"/>
  
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 
  <dsp:setvalue param="countryCode" beanvalue="SiteContext.site.defaultCountry"/>
  
  <dsp:getvalueof var="navigationActionPath" 
    bean="/atg/endeca/assembler/cartridge/manager/LocationActionPathProvider.defaultExperienceManagerNavigationActionPath" scope="request"/>  
      
  <dsp:getvalueof var="totalNumRecs" value="${contentItem.totalNumRecs}"/>
  <dsp:getvalueof var="recsPerPage" value="${contentItem.recsPerPage}" scope="session"/>
  <dsp:getvalueof var="lastRecNum" value="${contentItem.lastRecNum}"/>
 
  <%-- Display the number of search results if this is a query search --%>
  
  <div class="searchResults" data-results-list-count="${totalNumRecs}">
    <ul class="searchResults">
      <c:choose>
        <%-- No Results --%>
        <c:when test="${empty totalNumRecs || totalNumRecs == 0 }">
          <li></li>
          <li><h2><fmt:message key="mobile.locator.NoStoresFound"/></h2></li>
          <li></li>
        </c:when>
        <c:otherwise> 
          <%-- Display Results --%>
          <div class="searchSectionHeader" style="font-size:125%">
            <span class="searchSectionHeaderCaption">
              <span id="paginationInfo">
                <fmt:message key="mobile.locationResultsList.StoresFound">
                  <fmt:param value="${lastRecNum}"/>
                  <fmt:param value="${totalNumRecs}"/>
                </fmt:message>
                <fmt:message key="mobile.locationResultsList.detailStoresFound"/>
              </span>
            </span>
          </div>
    
          <dsp:include page="${mobileStorePrefix}/browse/gadgets/locationSortToolbar.jsp">
            <dsp:param name="contentItem" value="${contentItem}"/>
          </dsp:include>
   
          <%-- Get the default experience manager record detail path for stores. --%>
          <dsp:getvalueof var="recordActionPath" 
            bean="/atg/endeca/assembler/cartridge/manager/LocationActionPathProvider.defaultExperienceManagerRecordActionPath"/>
          
          <%-- Store Details Display --%>
          <dsp:getvalueof var = "counter" param = "No"/>
          <c:forEach var="record" items="${contentItem.records}" varStatus="loopStatus">                  
            <dsp:getvalueof var="index" value="${loopStatus.index}"/>
            <dsp:getvalueof var="count" value="${loopStatus.count}"/>
            <dsp:getvalueof var="storeIndex" value="${counter+count}"/>        
            <dsp:getvalueof var="searchAttribute" value="${record.attributes}"/>
            <%--
              This droplet return the distance of store using Endeca 
              generated content item as string and it formats the 
              particular number to input number of digits 
             
              Input parameters:
                variable
                  A endeca generated object use as string for extracting distance
                
                value
                  value need to be formatted
                fraction
                  upto how many digit need to be formatted
                  
              Output parameter:    
                 distance 
           
              Open parameters: 
                output
                      
            --%>       
            <dsp:droplet name="StoreDistanceDroplet">
              <dsp:param name="variable" value="${searchAttribute}"/> 
              <dsp:param name="fraction" value="3"/>
              <dsp:oparam name="output">
                <dsp:getvalueof var="distance" param="distance"/>   
              </dsp:oparam>
            </dsp:droplet>    
            <preview:repositoryItem itemId="${record.attributes['store.repositoryId']}" itemType="store" repositoryName="LocationRepository">
            
              <%-- Store Details in Tabular Form --%> 
              <li class="icon-ArrowRight" id="searchItem">
                <dsp:a href="${originatingRequest.contextPath}${recordActionPath}${record.detailsAction.recordState}" 
                       title="${storeName}" onclick="CRSMA.locationsearch.clearNavParamInHistoryBeforeMove();">

                  <%-- First Cell of Table --%>
                  <span class="searchResult">
                    <table>
                      <tr>
                        <c:if test="${not empty distance}">
                          <%--             
                            Droplet used to convert a value from one unit of measurement to another
                            This droplet takes the following parameters:
                   
                            Input parameters:
                              value
                                The value (quantity) that will be converted
                              from (optional)
                                The starting unit of measure. Null will use locale's default. default will use the base unit of measured defined by the unit of measure tools
                              to (optional)
                                The ending unit of measure. Null will use locale's default. default will use the base unit of measured defined by the unit of measure tools
                              language (optional)
                                The language of the locale to use
                              country (optional)
                                The country of the locale to use

                              Output parameters:
                               This droplet renders the following parameters as part of the output:
                               result
                                 The resulting quantity after the conversion (in the uom defined by the to parameter)
                               from
                                 The initial unit of measure that was used
                               to
                                 The resulting unit of measure
                               conversionRate
                                 The rate of conversion to convert the unit from the initial uom to the resulting uom
                          --%>
                          <dsp:droplet name="StoreUnitsOfMeasureConversionDroplet">
                            <dsp:param name="value" value="${distance}"/> 
                            <dsp:param name="from" value="km"/>
                            <dsp:oparam name="output">
                              <dsp:getvalueof var="result" param="result"/>
                              <dsp:getvalueof var="to" param="to"/>
                              <dsp:droplet name="StoreDistanceDroplet">
                                <dsp:param name="value" value="${result}"/> 
                                <dsp:param name="fraction" value="2"/>
                                <dsp:oparam name="output">
                                  <dsp:getvalueof var="result" param="distance"/>
                                </dsp:oparam>
                              </dsp:droplet>
                              <td align="center">
                                <div class="crs_store_diamond_image">
                                  <div>${storeIndex}</div>
                                </div>
                                <div>${result} ${to}</div>
                                <div><fmt:message key="mobile.details.fromYou"/></div>
                              </td>
                            </dsp:oparam>
                          </dsp:droplet>
                        </c:if>
                        <c:if test="${empty distance}">
                          <td align="center">
                            <div class="crs_store_diamond_image"> 
                              <div>${storeIndex}</div>
                            </div>
                          </td>
                        </c:if>

                        <%-- Second Cell of table --%>
                        <td>
                          <div class="store-search-result"> 
                            <span class="storeName">
                              ${record.attributes['store.name']}
                            </span>
                            <div>
                              <dsp:getvalueof var="Address1" value="${record.attributes['address1']}"/>
                              <c:if test="${not empty Address1}">
                                <div class="street-address"><c:out value="${Address1}"/></div>
                              </c:if>
                              <dsp:getvalueof var="City" value="${record.attributes['city']}"/>
                              <c:if test="${not empty City}">
                                <span class="locality"><c:out value="${City}"/></span>,
                              </c:if>
                              <dsp:getvalueof var="County" value="${record.attributes['county']}"/>
                              <c:if test="${not empty County}">
                                <span class="county-name"><c:out value="${County}"/></span>,
                              </c:if>
                              <dsp:getvalueof var="StateAddress" value="${record.attributes['stateAddress']}"/>
                              <c:if test="${not empty StateAddress}">
                                <span class="region"><c:out value="${StateAddress}"/></span>,
                              </c:if>
                              <dsp:getvalueof var="PostalCode" value="${record.attributes['postalCode']}"/>
                              <c:if test="${not empty PostalCode}">
                                <span class="postal-code"><c:out value="${PostalCode}"/></span>
                              </c:if>
                              <dsp:getvalueof var="Country" value="${record.attributes['country']}"/>
                              <c:if test="${not empty Country}">
                                <span class="country-name"><c:out value="${Country}"/></span>
                              </c:if>
                            </div>
                          </div>
                        </td>
                      </tr>
                    </table>       
                  </span>
                  <div class="shadow">&nbsp;</div>    
                  <dsp:param name="storeDistance" value="${result}"/>
                  <dsp:param name="distanceUnit" value="${to}"/>
                </dsp:a>
              </li>
            </preview:repositoryItem>
          </c:forEach>
      
          <%-- Show pagination links --%>
          <c:set var="pagingAction" value="${fn:replace(contentItem.pagingActionTemplate.navigationState, '%7Boffset%7D', 0)}"/>
          <dsp:droplet name="URLProcessor">
            <dsp:param name="url" value="${pagingAction}"/>
            <dsp:param name="parameter" value="No"/>
            <dsp:param name="parameterValue" value="${lastRecNum}"/>
            <dsp:oparam name="output">
              <dsp:getvalueof var="element" param="element"/>
              <c:set var="pagingAction" value="${element}"/>
            </dsp:oparam>
          </dsp:droplet>

          <%-- Switch to "Results List" mode (actual in case if we were in "Refinement" mode) --%>
          <c:set var="pagingAction" value="${fn:replace(pagingAction, '&nav=true', '')}"/>
          <c:set var="url" value="${siteBaseURL}${contentItem.pagingActionTemplate.contentPath}${pagingAction}"/>
          
          <dsp:include page="${mobileStorePrefix}/cartridges/LocationResultsList/storeListRangePagination.jsp">
            <dsp:param name="lastRecordIndex" value="${lastRecNum}"/>
            <dsp:param name="totalNumRecords" value="${totalNumRecs}"/>
            <dsp:param name="recordsPerPage" value="${recsPerPage}"/>
            <dsp:param name="url" value="${url}"/>
          </dsp:include> 
        </c:otherwise>
      </c:choose>
    </ul>
  </div>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/LocationResultsList/LocationResultsList.jsp#1 $$Change: 1497274 $ --%>

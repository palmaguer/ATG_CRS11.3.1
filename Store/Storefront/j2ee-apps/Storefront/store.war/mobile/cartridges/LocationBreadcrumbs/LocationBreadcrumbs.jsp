<%--
  LocationBreadcrumbs
  
  Renders refinement that have been selected. Selected refinements can consist
  of search refinements, dimension refinements or geo-filter refinements.
  
  There are a number of different types of breadcrumb that can be returned
  inside this content item:
    refinementCrumbs - As a result of selecting a dimension
    searchCrumbs - As a result of performing a search
    geoFilterCrumb - As a result of search by geocode
--%>
<dsp:page>
  <dsp:importbean bean="/atg/store/droplet/StoreUnitsOfMeasureConversionDroplet"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

  <c:if test="${not empty contentItem.searchCrumbs || not empty contentItem.refinementCrumbs || not empty contentItem.geoFilterCrumb}">
    <ul class="crumbDataList">
      <%-- "Search Term" crumbs block --%>
      <c:forEach var="searchCrumb" items="${contentItem.searchCrumbs}">
        <c:set var="searchCrumbText">
          <c:choose>
            <c:when test="${searchCrumb.correctedTerms != null}">&quot;<c:out value="${searchCrumb.correctedTerms}"/>&quot;</c:when>
            <c:otherwise>&quot;<c:out value="${searchCrumb.terms}"/>&quot;</c:otherwise>
          </c:choose>
        </c:set>
        <li role="link" aria-describedby="delId">
          <dsp:include page="${mobileStorePrefix}/global/util/getNavLink.jsp">
            <dsp:param name="navAction" value="${searchCrumb.removeAction}"/>
          </dsp:include>
          <div class="crumbContent" onclick="document.location = CRSMA.search.setNavState('${navLink}');">
            <div class="crumbContentText">
              <c:out value="${searchCrumbText}"/>
            </div>
          </div>
        </li>
      </c:forEach>

      <%-- "Refinement" crumbs block (dimension values) --%>
      <c:forEach var="refinementCrumb" items="${contentItem.refinementCrumbs}">
        <dsp:include page="${mobileStorePrefix}/global/util/getNavLink.jsp">
          <dsp:param name="navAction" value="${refinementCrumb.removeAction}"/>
        </dsp:include>
        <li id="${fn:replace(refinementCrumb.displayName, '.', '_')}" onclick="CRSMA.search.removeCrumb(this, CRSMA.search.setNavState('${navLink}'))" role="link" aria-describedby="delId">
          <div class="crumbContent">
            <div class="crumbContentText">
              <span><c:out value="${refinementCrumb.label}"/></span>
            </div>
          </div>
        </li>
      </c:forEach>
    
      <%-- Display geofilter crumb if there are any.  e.g "Within 5 Miles" --%>
      <c:if test="${not empty contentItem.geoFilterCrumb}">
        <dsp:getvalueof var="geoCrumb" value="${contentItem.geoFilterCrumb}"/>
        <%-- 
          Convert search radius from kilometer to locale based unit that is in use.
         
          Input parameters:
            value
              search radius in km
            from
              km
          Output parameters:
            to
              Unit as per shopper's locale
            result
              search radius in unit as per shopper's locale
        --%> 
        <dsp:droplet name="StoreUnitsOfMeasureConversionDroplet">
          <dsp:param name="value" value="${geoCrumb.radiusKM}"/> 
          <dsp:param name="from" value="km"/>
          <dsp:oparam name="output">
            <dsp:getvalueof var="result" param="result"/>
            <dsp:getvalueof var="to" param="to"/>
            <fmt:formatNumber var="radius" value="${result}" groupingUsed="false" maxFractionDigits="0" />
            <c:set var="radiusAndUnit" value="${radius} ${to}"/>
          </dsp:oparam>
        </dsp:droplet>         

        <%-- Display crumb link --%> 
        <li onclick="CRSMA.search.removeCrumb(this, CRSMA.locationsearch.clearGeoSort('${siteBaseURL}${geoCrumb.removeAction.contentPath}${geoCrumb.removeAction.navigationState}', '${geoCrumb.propertyName}'))" role="link" aria-describedby="delId">
          <div class="crumbContent" id="geo">
            <div class="crumbContentText">
              <%-- Create message crumb to be displayed --%>
              <fmt:message key="mobile.searchRadius.searchNearMe">
                <fmt:param>
                  <c:out value="${radiusAndUnit}"/>
                </fmt:param>
              </fmt:message>
            </div>
          </div>
        </li>
      </c:if>
    </ul>

    <div id="delId" style="display:none">
      <fmt:message>mobile.breadcrumb.a11y.remove</fmt:message>
    </div>
  </c:if>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/LocationBreadcrumbs/LocationBreadcrumbs.jsp#1 $$Change: 1497274 $--%>

<%--
  Renders the sort toolbar for an Endeca-driven location results list page.

  Required parameters:
    contentItem
      The "LocationResultsList" content item.

  NOTES:
    1) The "siteContextPath" request-scoped variables (request attributes), which are used here,
       are defined in the "mobilePageContainer" tag ("mobilePageContainer.tag" file).
       These variables become available within the <crs:mobilePageContainer> ... </crs:mobilePageContainer> block
       and in all the included pages (gadgets and Endeca cartridges).
--%>
<dsp:page>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
  <dsp:getvalueof var="storeLocationsPath" bean="/atg/endeca/assembler/cartridge/manager/LocationActionPathProvider.defaultExperienceManagerNavigationActionPath"/>
  <c:if test="${contentItem.totalNumRecs > 1}">
    <fmt:message var="citySortLabel" key="mobile.locationResultsList.sort.city"/>
    <fmt:message var="distanceSortLabel" key="mobile.locationResultsList.sort.distance"/>
    <fmt:message var="citySortLabelVoiceOver" key="mobile.locationResultsList.sort.city"/>
    <fmt:message var="distanceSortLabelVoiceOver" key="mobile.locationResultsList.sort.distance"/>
    <fmt:message var="voiceOverSortBy" key="common.sortBy"/>
    <fmt:message var="voiceOverSelected" key="mobile.locationResultsList.sort.selected.voiceOver"/>
    <c:set var="citySelected" value=""/>
    <c:set var="distanceSelected" value=""/>
    <c:set var="distanceEnabled" value=""/>

    <%--
      Determine the right labels based on all sort options and selected sort option.
      Also, save the sort actions associated with each sort option for later use.
    --%>
    <c:forEach var="sortOption" items="${contentItem.sortOptions}">
      <c:choose>
        <c:when test="${sortOption.label == 'sort.distanceAscending'}">
          <c:set var="distanceLHAction" value="${sortOption.navigationState}"/>
          <c:if test="${sortOption.selected}">
            <c:set var="selected" value="distanceLH"/>
            <fmt:message var="distanceSortLabel" key="mobile.locationResultsList.sort.distance.asc"/>
            <fmt:message var="distanceSortLabelVoiceOver" key="mobile.locationResultsList.sort.distance.asc.voiceOver"/>
            <c:set var="distanceSelected" value="selected"/>
          </c:if>
          <c:set var="distanceEnabled" value="enabled"/>
        </c:when>
        <c:when test="${sortOption.label == 'sort.distanceDescending'}">
          <c:set var="distanceHLAction" value="${sortOption.navigationState}"/>
          <c:if test="${sortOption.selected}">
            <c:set var="selected" value="distanceHL"/>
            <fmt:message var="distanceSortLabel" key="mobile.locationResultsList.sort.distance.desc"/>
            <fmt:message var="distanceSortLabelVoiceOver" key="mobile.locationResultsList.sort.distance.desc.voiceOver"/>
            <c:set var="distanceSelected" value="selected"/>
          </c:if>
          <c:set var="distanceEnabled" value="enabled"/>
        </c:when>
        <c:when test="${sortOption.label == 'sort.cityAZ'}">
          <c:set var="cityAZAction" value="${sortOption.navigationState}"/>
          <c:if test="${sortOption.selected}">
            <fmt:message var="citySortLabel" key="mobile.locationResultsList.sort.city.asc"/>
            <fmt:message var="citySortLabelVoiceOver" key="mobile.locationResultsList.sort.city.asc.voiceOver"/>
            <c:set var="selected" value="cityAZ"/>
            <c:set var="citySelected" value="selected"/>
          </c:if>
        </c:when>
        <c:when test="${sortOption.label == 'sort.cityZA'}">
          <c:set var="cityZAAction" value="${sortOption.navigationState}"/>
          <c:if test="${sortOption.selected}">
            <c:set var="selected" value="cityZA"/>
            <fmt:message var="citySortLabel" key="mobile.locationResultsList.sort.city.desc"/>
            <fmt:message var="citySortLabelVoiceOver" key="mobile.locationResultsList.sort.city.desc.voiceOver"/>
            <c:set var="citySelected" value="selected"/>
          </c:if>
        </c:when>
      </c:choose>
    </c:forEach>

    <c:if test="${(empty citySelected) && (empty distanceSelected)}">
      <c:set var="citySelected" value="selected"/>
    </c:if>

    <%--
      Modify the URL to remove the nav=true part. Removing this parameter will take the user
      directly to the list view as opposed to landing them on the filter view.
    --%>
    <c:set var="cityZAAction" value="${fn:replace(cityZAAction, '&nav=true', '')}"/>
    <c:set var="cityAZAction" value="${fn:replace(cityAZAction, '&nav=true', '')}"/>
    <c:set var="distanceLHAction" value="${fn:replace(distanceLHAction, '&nav=true', '')}"/>
    <c:set var="distanceHLAction" value="${fn:replace(distanceHLAction, '&nav=true', '')}"/>

    <%-- Determine the right action for the sorting buttons --%>
    <c:choose>
      <c:when test="${selected == 'cityAZ'}">
        <c:set var="cityAction" value="${cityZAAction}"/>
        <c:set var="distanceAction" value="${distanceLHAction}"/>
      </c:when>
      <c:when test="${selected == 'cityZA'}">
        <c:set var="cityAction" value="${cityAZAction}"/>
        <c:set var="distanceAction" value="${distanceLHAction}"/>
      </c:when>
      <c:when test="${selected == 'distanceHL'}">
        <c:set var="cityAction" value="${cityAZAction}"/>
        <c:set var="distanceAction" value="${distanceLHAction}"/>
      </c:when>
      <c:when test="${selected == 'distanceLH'}">
        <c:set var="cityAction" value="${cityAZAction}"/>
        <c:set var="distanceAction" value="${distanceHLAction}"/>
      </c:when>
      <c:otherwise>
        <c:set var="cityAction" value="${cityAZAction}"/>
        <c:set var="distanceAction" value="${distanceLHAction}"/>
     </c:otherwise>
    </c:choose>

    <%-- Remember the current sort key --%>
    <input type="hidden" id="currentSort" value="${selected}"/>

    <%-- Draw the sorting "buttons" --%>

    <li class="${empty distanceEnabled ? 'citySortToolbar' : 'dcitySortToolbar'}">
      <div id="citySort" class="${citySelected}">
        <a href="${siteContextPath}${storeLocationsPath}${cityAction}" class="voiceOverText" role="button">
          <%-- Have Voice Over read "Selected" if element is selected" --%>
          <c:if test="${citySelected == 'selected'}">
            <span><c:out value="${voiceOverSelected}"/></span>
          </c:if>
          <span><c:out value="${voiceOverSortBy}"/></span>
          <span><c:out value="${citySortLabelVoiceOver}"/></span>
        </a>
        <a href="${siteContextPath}${storeLocationsPath}${cityAction}" aria-hidden="true"><c:out value="${citySortLabel}"/></a>
      </div>
      
      <c:if test="${not empty distanceEnabled}">
      <div id="distanceSort" class="${distanceSelected}">
        <a href="${siteContextPath}${storeLocationsPath}${distanceAction}" class="voiceOverText" role="button">
          <%-- Have Voice Over read "Selected" if element is selected" --%>
          <c:if test="${distanceSelected == 'selected'}">
            <span><c:out value="${voiceOverSelected}"/></span>
          </c:if>
          <span><c:out value="${voiceOverSortBy}"/></span>
          <span><c:out value="${distanceSortLabelVoiceOver}"/></span>
        </a>
        <a href="${siteContextPath}${storeLocationsPath}${distanceAction}" aria-hidden="true"><c:out value="${distanceSortLabel}"/></a>
      </div>
    </c:if>
    </li>
  </c:if>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/browse/gadgets/locationSortToolbar.jsp#1 $$Change: 1497274 $--%>

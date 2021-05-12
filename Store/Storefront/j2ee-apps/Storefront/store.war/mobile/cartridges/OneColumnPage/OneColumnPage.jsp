<%--
  "OneColumnPage" cartridge renderer.

  Includes:
    /mobile/global/gadgets/loadingWindow.jsp - "Loading..." message box
    /mobile/global/util/hasRecsInstalled.jsp - Checks if "Recommendations" is installed
    /mobile/global/util/currencyCode.jsp - Returns ISO 4217 currency code/symbol corresponding to "Price List Locale"

  Required Parameters:
    contentItem
      The "OneColumnPage" content item to render.

  Optional parameters:
    None

  NOTES:
    1) The "mobileStorePrefix" request-scoped variable (request attribute), which is used here,
       is defined in the "mobilePageContainer" tag ("mobilePageContainer.tag" file).
       This variable becomes available within the <crs:mobilePageContainer> ... </crs:mobilePageContainer> block
       and in all the included pages (gadgets and Endeca cartridges).
--%>
<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

  <fmt:message var="pageTitle" key="mobile.homepage.title"/>
  <crs:mobilePageContainer titleString="${pageTitle}">
    <jsp:attribute name="modalContent">
      <%-- "Loading..." message box --%>
      <dsp:include page="${mobileStorePrefix}/global/gadgets/loadingWindow.jsp"/>
    </jsp:attribute>

    <jsp:body>
      <endeca:previewAnchor contentItem="${contentItem}">
        <c:forEach var="element" items="${contentItem.MainContent}">
          <dsp:renderContentItem contentItem="${element}" />
        </c:forEach>
      </endeca:previewAnchor>
    </jsp:body>
  </crs:mobilePageContainer>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/OneColumnPage/OneColumnPage.jsp#1 $$Change: 1497274 $--%>

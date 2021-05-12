<%--
  "MobilePage" cartridge renderer.

  Includes:
    /mobile/global/gadgets/noSearchResults.jsp - "No Search Results" pseudo-modal popup renderer

  Required Parameters:
    contentItem
      The "MobilePage" content item to render.

  Optional parameters:
    nav
      nav=true - The page and all the subpages are in "Refinement" mode.
                 Otherwise, the mode is considered "Results list".

  NOTES:
    1) The "mobileStorePrefix" request-scoped variable (request attribute), which is used here,
       is defined in the "mobilePageContainer" tag ("mobilePageContainer.tag" file).
       This variable becomes available within the <crs:mobilePageContainer> ... </crs:mobilePageContainer> block
       and in all the included pages (gadgets and Endeca cartridges).
--%>
<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

  <fmt:message var="pageTitle" key="mobile.resultsList.pageTitle"/>
  <crs:mobilePageContainer titleString="${pageTitle}">
    <jsp:attribute name="modalContent">
      <%-- "No Search Results" pseudo-modal popup --%>
      <dsp:include page="${mobileStorePrefix}/global/gadgets/noSearchResults.jsp"/>
    </jsp:attribute>

    <jsp:body>
      <div id="switchBar"></div>

      <div id="main" style="display:none">
        <endeca:previewAnchor contentItem="${contentItem}">
          <c:forEach var="element" items="${contentItem.MainContent}">
            <dsp:renderContentItem contentItem="${element}"/>
          </c:forEach>
        </endeca:previewAnchor>
      </div>

      <div id="secondary" style="display:none">
        <endeca:previewAnchor contentItem="${contentItem}">
          <c:forEach var="element" items="${contentItem.SecondaryContent}">
            <dsp:renderContentItem contentItem="${element}"/>
          </c:forEach>
        </endeca:previewAnchor>
      </div>

      <script>
        $(document).ready(function() {
          CRSMA.mobilepage.initMobilePage();
        });
      </script>
    </jsp:body>
  </crs:mobilePageContainer>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/MobilePage/MobilePage.jsp#1 $$Change: 1497274 $--%>

<%--
  "PageSlot" cartridge renderer.
  Mobile version.

  Passes the contents of the "PageSlot" to a renderer JSP that knows how to
  handle the contents of particular type.
--%>
<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

  <endeca:previewAnchor contentItem="${contentItem}">
    <c:forEach var="element" items="${contentItem.contents}">
      <dsp:renderContentItem contentItem="${element}"/>
    </c:forEach>
  </endeca:previewAnchor>

</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/PageSlot/PageSlot.jsp#1 $$Change: 1497274 $--%>

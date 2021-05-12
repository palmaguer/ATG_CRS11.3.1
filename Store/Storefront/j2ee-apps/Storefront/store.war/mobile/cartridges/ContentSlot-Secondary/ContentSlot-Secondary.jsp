<%--
  ~ Copyright (C) 1994, 2018, Oracle and/or its affiliates. All rights reserved.
  ~ Oracle and Java are registered trademarks of Oracle and/or its
  ~ affiliates. Other names may be trademarks of their respective owners.
  ~ UNIX is a registered trademark of The Open Group.

  This renderer calls the renderContentItem for it's contents.

  Required Parameters:
    contentItem
      The page slot content item to render.

  Optional Parameters:
    None
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
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/ContentSlot-Secondary/ContentSlot-Secondary.jsp#2 $$Change: 1503965 $--%>

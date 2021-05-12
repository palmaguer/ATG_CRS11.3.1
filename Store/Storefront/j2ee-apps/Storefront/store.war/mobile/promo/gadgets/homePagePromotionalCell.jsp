<%--
  Renders promotional item cell html (top slot).
                                                       Required Parameters:
    promotionalContent
      Promotional item

  Optional Parameters:
    None
--%>
<dsp:page>
  <%-- Request parameters - to variables --%>
  <dsp:getvalueof var="promotionalContent" param="promotionalContent"/>

  <div class="cell">
    <div class="homePromotionalWrap">
      <c:choose>
        <c:when test="${not empty promotionalContent.deviceLinkUrl}">
          <%-- Display "Link destination" --%>
          <c:choose>
            <%-- handle Endeca links --%>
            <c:when test="${fn:startsWith(promotionalContent.deviceLinkUrl, '/browse')}">
                <img onclick='window.location.href="${siteBaseURL}${promotionalContent.deviceLinkUrl}"' src="${promotionalContent.derivedDeviceImage}"
				alt="${promotionalContent.storeDisplayName}" class="homePromotionalImage"/>
            </c:when>
            <c:when test="${fn:startsWith(promotionalContent.deviceLinkUrl, 'ocrsiua://')}">
                <img onclick='window.location.href="${promotionalContent.deviceLinkUrl}"' src="${promotionalContent.derivedDeviceImage}"
                alt="${promotionalContent.storeDisplayName}" class="homePromotionalImage"/>
            </c:when>
            <%-- otherwise popup the link in a new window --%>
            <c:otherwise>
                <img onclick='window.open("${promotionalContent.deviceLinkUrl}")' src="${promotionalContent.derivedDeviceImage}"
				alt="${promotionalContent.storeDisplayName}" class="homePromotionalImage"/>
            </c:otherwise>
          </c:choose>
        </c:when>
        <c:otherwise>
          <%-- Display static image --%>
          <img src="${promotionalContent.derivedDeviceImage}" alt="${promotionalContent.storeDisplayName}"
               class="homePromotionalImage"/>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/promo/gadgets/homePagePromotionalCell.jsp#1 $$Change: 1497274 $--%>

<%-- 
  This page lays out the elements that make up the RefinementMenu.
    
  Required Parameters:
    None.
   
  Optional Parameters:
    None
--%>
<dsp:page>
  <dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/multisite/SiteContext"/>
  <dsp:setvalue param="countryCode" beanvalue="SiteContext.site.defaultCountry"/>
  
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
  <c:if test="${not empty contentItem.refinements}">
    <endeca:previewAnchor contentItem="${contentItem}">
      <div class="refinementFacetGroupContainer">
        <%-- Facet group name --%>
        <span class="refinementFacetGroupName">
          <fmt:message>mobile.${not empty contentItem.dimensionName ? contentItem.dimensionName : contentItem.name}</fmt:message>
        </span>

        <ul class="refinementDataList" id="ul_${fn:replace(contentItem.name, '.', '_')}">
          <%-- Facet rows --%>
          <c:choose>
            <c:when test="${contentItem.dimensionName eq 'store.state'}">
              <c:forEach var="refinement" items="${contentItem.refinements}">
                <dsp:include page="${mobileStorePrefix}/global/util/getNavLink.jsp">
                  <dsp:param name="navAction" value="${refinement}"/>
                </dsp:include>
                <li onclick="CRSMA.search.applyFacet(this, '${navLink}')" role="link" aria-describedby="addId">
                  <div class="refinementContent">
                    <div class="refinementLabel"> 
                      <%--
                        Given a country code and state code this droplet 
                          returns complete state name along with code.
            
                        Input parameters:
                          countryCode
                            Country code.
                          stateCode
                            State Code
              
                        Open parameters:
                          output
                            Our output is rendered inside this open parameter.
            
                        Output parameters:
                          state
                            State Code - State Name combination for given state code.
                      --%>  
                      <dsp:droplet name="/atg/commerce/util/StateListDroplet">
                        <dsp:param name="countryCode" param="countryCode"/>
                        <dsp:param name="stateCode" value="${refinement.label}"/>
                        <dsp:oparam name="output">
                          <dsp:getvalueof var="state" param="state"/>
                          <c:choose>
                            <c:when test="${not empty state}">
                              <c:set var="stateName" value="${fn:split(state, '-')}"/>
                              <c:out value="${stateName[1]}"/>
                            </c:when>
                            <c:otherwise>
                              <c:out value="${refinement.label}"/>
                            </c:otherwise>
                          </c:choose>
                        </dsp:oparam>
                      </dsp:droplet>
                    </div>
                    <div class="refinementCount">${refinement.count}</div>
                  </div>
                </li>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <c:forEach var="refinement" items="${contentItem.refinements}">
                <dsp:include page="${mobileStorePrefix}/global/util/getNavLink.jsp">
                  <dsp:param name="navAction" value="${refinement}"/>
                </dsp:include>
                <li onclick="CRSMA.search.applyFacet(this, '${navLink}')" role="link" aria-describedby="addId">
                  <div class="refinementContent">
                    <div class="refinementLabel"> 
                      <c:out value="${refinement.label}"/>
                    </div>
                    <div class="refinementCount">${refinement.count}</div>
                  </div>
                </li>
              </c:forEach>
            </c:otherwise>
          </c:choose>
            
          <%-- "Show More" item --%>
          <c:set var="hasMoreLink" value="${not empty contentItem.moreLink.navigationState}"/>
          <c:if test="${hasMoreLink}">
            <dsp:include page="${mobileStorePrefix}/global/util/getNavLink.jsp">
              <dsp:param name="navAction" value="${contentItem.moreLink}"/>
            </dsp:include>
            <li onclick="document.location = CRSMA.search.setNavState('${navLink}');">
              <div class="refinementContent" style="text-align:center;">
                <fmt:message key="mobile.refinement.link.showMore"/>
              </div>
            </li>
          </c:if>

          <%-- "Show Less" item --%>
          <c:set var="hasLessLink" value="${not empty contentItem.lessLink.navigationState}"/>
          <c:if test="${hasLessLink}">
            <dsp:include page="${mobileStorePrefix}/global/util/getNavLink.jsp">
              <dsp:param name="navAction" value="${contentItem.lessLink}"/>
            </dsp:include>
            <li onclick="document.location = CRSMA.search.setNavState('${navLink}');">
              <div class="refinementContent" style="text-align:center;">
                <fmt:message key="mobile.refinement.link.showLess"/>
              </div>
            </li>
          </c:if>
        </ul>
      </div>

      <div id="addId" style="display:none">
        <fmt:message>mobile.refinement.a11y.add</fmt:message>
      </div>
    </endeca:previewAnchor>
  </c:if>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/LocationRefinementMenu/LocationRefinementMenu.jsp#1 $$Change: 1497274 $--%>

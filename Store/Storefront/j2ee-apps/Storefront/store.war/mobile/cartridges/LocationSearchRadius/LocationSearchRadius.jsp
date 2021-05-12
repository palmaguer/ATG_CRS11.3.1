<%-- 
  This page lays out the elements that make up the search radius refinement menu.
    
  Required Parameters:
    None.
    
  Optional Parameters:
    None.
--%>

<dsp:page>
  <dsp:getvalueof var="contextPath" vartype="java.lang.String"  
    bean="/OriginatingRequest.contextPath"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/store/droplet/StoreUnitsOfMeasureConversionDroplet"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" 
    value="${originatingRequest.contentItem}"/> 
  <dsp:getvalueof var="enabled" value="${contentItem.enabled}"/>

  <c:if test="${enabled}">
    <div class="refinementFacetGroupContainer">
      <%-- Facet group name --%>
      <span class="refinementFacetGroupName">
        <fmt:message>mobile.searchRadius.Distance</fmt:message>
      </span>

      <ul class="refinementDataList" id="ul_${fn:replace(contentItem.name, '.', '_')}">
        <%-- Facet rows --%>
        <c:forEach var="refinement" items="${contentItem.searchRadius}">
        
          <%-- 
            Determine locale based unit to be used and convert search radius 
            to kilometer for use by endeca. 
             
            Input parameters:
              value
                search radius in unit as per shopper's locale
              to
                km: As Endeca expects radius to be in kilometer.

            Output parameters:
              from
                Unit used as per shopper's locale
              result
                search radius in kilometer to be sent to endeca
          --%> 
          <dsp:droplet name="StoreUnitsOfMeasureConversionDroplet">
            <dsp:param name="value" value="${refinement}"/> 
            <dsp:param name="to" value="km"/>
            <dsp:oparam name="output">
              <dsp:getvalueof var="result" param="result"/>
              <dsp:getvalueof var="from" param="from"/>
              <li id="${refinement}" onclick="CRSMA.locationsearch.setLocationAndSubmit(this,'${result}','${contentItem.geoProperty}');" 
                  role="link" aria-describedby="addId">
                <div class="refinementContent">
                  <div class="refinementLabel"> 
                    <c:out value="${refinement}"/> <c:out value="${from}"/>
                  </div>
                </div>
              </li>
            </dsp:oparam>
          </dsp:droplet>
        </c:forEach>
      </ul>
    </div>
    <div id="addId" style="display:none">
      <fmt:message>mobile.refinement.a11y.add</fmt:message>
    </div>
  </c:if>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/LocationSearchRadius/LocationSearchRadius.jsp#1 $$Change: 1497274 $--%>
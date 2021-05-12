<%-- 
  This page lays out the elements that make up the Store Search by Current
  location Content.
    
  Required Parameters:
    None.
   
  Optional Parameters:
    None.
--%>
<dsp:page>
  <dsp:importbean bean="/atg/store/droplet/StoreUnitsOfMeasureConversionDroplet"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contextPath" vartype="java.lang.String" 
                  value="${originatingRequest.contextPath}"/>
  <dsp:getvalueof var="actionPath" 
    bean="/atg/endeca/assembler/cartridge/manager/LocationActionPathProvider.defaultExperienceManagerNavigationActionPath"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" 
                  value="${originatingRequest.contentItem}"/>
  <%-- 
    Converts default search radius from locale based unit to kilometer.
   
    Input parameters:
      value
        search radius in unit as per shopper's locale.
      to
        km
    Output parameter:
      result
        search radius in km.
  --%> 
  <dsp:droplet name="StoreUnitsOfMeasureConversionDroplet">
    <dsp:param name="value" value="${contentItem.defaultSearchRadius}"/> 
    <dsp:param name="to" value="km"/>
    <dsp:oparam name="output">
      <dsp:getvalueof var="result" param="result"/>
    </dsp:oparam>
  </dsp:droplet>
  <div class="dataContainer guidedNavigation" style="padding-bottom: 0;" data-navigation-groups-count="1">
    <div class="refinementFacetGroupContainer">
      <div id="GeoNotSupported" class="search-near-message" >
        <fmt:message key="mobile.location.geoLocationNotSupported"/>
      </div>
  
      <div id="GeoRequestDenied" class="search-near-message" >
        <fmt:message key="mobile.location.geoLocationRequestDenied"/>
      </div >

      <ul class="refinementDataList" id="ul_${fn:replace(contentItem.name, '.', '_')}">
        <li id="${refinement}" onclick="CRSMA.locationsearch.setLocationAndSubmit(this,'${result}','${contentItem.geoProperty}')" 
            role="link" aria-describedby="addId" class="search-near-location">
          <div class="refinementContent">
            <fmt:message key="store.location.searchNearMe"/>
          </div>
        </li>
      </ul>
    </div>
    <div id="addId" style="display:none">
      <fmt:message>mobile.refinement.a11y.add</fmt:message>
    </div>
  </div>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/LocationSearch-CurrentLocation/LocationSearch-CurrentLocation.jsp#1 $$Change: 1497274 $--%>
<%--
  Renders StoreDetail cartridge content.

  Required parameters:
    None.
    
  Optional parameters:
    storeDistance
      Distance of this store from shopper location.
    distanceUnit
      Derived unit as per user locale.
--%>
<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/endeca/store/droplet/ActionURLDroplet"/>
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" 
                  value="${originatingRequest.contentItem.record}"/>
  <dsp:getvalueof var="storeImagePath" value="${originatingRequest.contentItem.storeImagePath}"/>
  <dsp:getvalueof var="storeDistance" param="storeDistance"/>
  <dsp:getvalueof var="distanceUnit" param="distanceUnit"/>
  <dsp:getvalueof var="storeItem" value="${contentItem.attributes}"/>

  <div class="store-image-container">
    <fmt:message var="imageDetails" 
                 key="mobile.location.storeLocation"/>
    <img src="${storeImagePath}" alt="${imageDetails}">
  </div>
  
  <preview:repositoryItem itemId="${contentItem.attributes['store.repositoryId']}" itemType="store" repositoryName="LocationRepository">
    <div class="store-data-container">      
      <div class="store-name-header"><c:out value="${storeItem.name}"/></div>
  
      <a href="tel:${storeItem.phoneNumber}" title="${storeItem.phoneNumber}">
        <div class="address-table"> 
          <div class="address-cell-left">
            <table>
              <tr>
                <td width="40px">
                  <img src="/crsdocroot/content/images/phone2.gif">
                </td>
                <td>
                  <div>
                    <c:out value="${storeItem.phoneNumber}"/>
                  </div>
                </td>
              </tr>
            </table>
          </div>
          <div class="address-cell-right store-details-arrow-right"></div> 
        </div>
      </a>
        
      <div class="address-table"> 
        <div class="address-cell-left"> 
          <table>
            <tr>
              <td width="40px">
                <img src="/crsdocroot/content/images/address.gif">
              </td>
              <td>
                <div>
                  <c:out value="${storeItem.address1}"/>
                  <c:out value="${storeItem.address2}"/>
                  <c:out value="${storeItem.address3}"/> 
                </div>
                <div>
                  <c:out value="${storeItem.city}"/>,
                  <c:out value="${storeItem.county}"/>
                  <c:out value="${storeItem.stateAddress}"/>
                </div>
                <div>
                  <c:out value="${storeItem.postalCode}"/>
                  <c:out value="${storeItem.country}"/>
                </div>
              </td>
            </tr>
          </table>
        </div> 
        <div class="address-cell-right">
          <c:if test="${not empty storeDistance}">
            <b>
              <c:out value="${storeDistance}"/>
              <c:out value="${distanceUnit}"/>
            </b><br>
            <fmt:message key="mobile.details.FromYourLocation"/>
          </c:if>
        </div>
      </div>
        
      <div class="address-table">
        <div class="address-cell-left">
          <table>
            <tr>
              <td width="40px">
                <img src="/crsdocroot/content/images/hours.gif">
              </td>
              <td>      
                <div>
                  <b><fmt:message key="mobile.locationResultList.storeHours"/></b>
                </div>
                <div>
                  <c:out value="${storeItem.hours}"/>
                </div>
              </td>
            </tr>
          </table>
        </div>
      </div>
        
      <div class="address-table">
        <div class="address-cell-left">
          <table>
            <tr>
              <td width="40px">
                <img src="/crsdocroot/content/images/fax.gif">
              </td>
              <td>
                <dsp:getvalueof var="FaxNumber" value="${storeItem.faxNumber}"/>
                <c:if test='${not empty FaxNumber && FaxNumber != ""}'>
                  <div>
                    <span class="type"><fmt:message key="common.fax"/></span>
                    <fmt:message key="mobile.common.colon"/>
                    <span class="value"><c:out value="${FaxNumber}"/></span>
                  </div>
                </c:if>
                <dsp:getvalueof var="Email" value="${storeItem.email}"/>
                <c:if test="${not empty Email}">
                  <div>
                    <a class="email" href="mailto:${Email}">
                      <c:out value="${Email}"/>
                    </a>
                  </div>
                </c:if>
              </td>
            </tr>
          </table>
        </div>
      </div> 
    </div>
  </preview:repositoryItem>
  <script>
    $(document).ready(function() {
    	$('#switchBar').hide();
    });
  </script>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/cartridges/StoreDetail/StoreDetail.jsp#1 $$Change: 1497274 $--%>

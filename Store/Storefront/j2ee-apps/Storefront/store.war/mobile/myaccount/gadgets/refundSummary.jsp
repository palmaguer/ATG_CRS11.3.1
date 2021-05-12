<%--
  This gadget renders refund summary for the specified return request.
  
  Page includes:
    /global/gadgets/formattedPrice.jsp - Price formatter
  
  Required parameters:
    return
      The ReturnReqeust object to display refund summary for.
      
  Optional parameters:
    isActiveReturn
      Boolean indicating whether the return request is currently active.
    priceListLocale
      Specifies a locale in which to format the price (as string).
      If not specified, locale will be taken from profile price list (Profile.priceList.locale). 
--%>
<dsp:page>
  <%-- Request parameters - to variables --%>
  <dsp:getvalueof var="returnRequest" param="return"/>
  <dsp:getvalueof var="isActiveReturn" param="isActiveReturn"/>

  <%-- Display Refund Summary header --%>
  <div class="sectionHeader"><fmt:message key="mobile.return.header.refundSummary"/></div>

  <div class="roundedBox">
    <%-- Display refund of return items --%>
    <div class="label"><fmt:message key="mobile.return.label.refundItems"/></div>
    <div class="value">
      <dsp:include page="/global/gadgets/formattedPrice.jsp">
        <dsp:param name="price" value="${returnRequest.totalReturnItemRefund}"/>
        <dsp:param name="priceListLocale" param="priceListLocale"/>
      </dsp:include>
    </div>

    <hr/>

    <%-- Display total of non-return items adjustments, only if there is indeed adjustemnts --%>
    <dsp:getvalueof var="promotionAdjustments" value="${returnRequest.promotionValueAdjustments}"/>
	<dsp:getvalueof var="nonReturnItemsAdjustments" value="${returnRequest.nonReturnItemSubtotalAdjustment}"/>
    <div class="label"><fmt:message key="mobile.return.label.adjustment"/><c:out value="${nonReturnItemsAdjustments != 0 ? '*' : '' }"/></div>
    <div class="value">
      <dsp:include page="/global/gadgets/formattedPrice.jsp">
        <dsp:param name="price" value="${nonReturnItemsAdjustments}"/>
        <dsp:param name="priceListLocale" param="priceListLocale"/>
      </dsp:include>
    </div>
    <hr/>
    
    <%-- Display shipping refund --%>
    <div class="label"><fmt:message key="mobile.return.label.shipping"/></div>
    <div class="value">
      <dsp:include page="/global/gadgets/formattedPrice.jsp">
        <dsp:param name="price" value="${returnRequest.actualShippingRefund}"/>
        <dsp:param name="priceListLocale" param="priceListLocale"/>
      </dsp:include>
    </div>

    <hr/>

    <%-- Display taxes refund --%>
    <div class="label"><fmt:message key="mobile.return.label.tax"/></div>
    <div class="value">
      <dsp:include page="/global/gadgets/formattedPrice.jsp">
        <dsp:param name="price" value="${returnRequest.actualTaxRefund}"/>
        <dsp:param name="priceListLocale" param="priceListLocale"/>
      </dsp:include>
    </div>

    <hr/>

    <%-- Total refund amount --%>
    <div class="label"><fmt:message key="mobile.return.label.totalRefund"/></div>
    <div class="value">
      <dsp:include page="/global/gadgets/formattedPrice.jsp">
        <dsp:param name="price" value="${returnRequest.totalRefundAmount}"/>
        <dsp:param name="priceListLocale" param="priceListLocale"/>
      </dsp:include>
    </div>
  </div>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/mobile/myaccount/gadgets/refundSummary.jsp#1 $$Change: 1497274 $--%>

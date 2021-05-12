<%--
  Renders the pdf in the spotlight with the thumbnail image and pdf name.
  
  Required parameters:
    mediaItem
      The audio media item to render.
      
    Optional Parameters:
      none
--%>
<dsp:page>
  <dsp:getvalueof var="media" param="mediaItem"/>
  <preview:repositoryItem item="${media}">
    <c:set var="mediaImageUrl" value="${media.thumbnailImage.url}"/>
    <c:set var="mediaImageName" value="${media.thumbnailImage.name}"/>
    <div class="crs_tile_container">

      <%-- onclick of thumnail image if user agent supports pdf it will open in same page. --%>
      <a href="${media.url}" class="crs_web_content_height" download="${media.name}.pdf">
        <c:if test="${empty mediaImageUrl}">
          <span class="crs_web_content_generic_pdf">
        </c:if>
        <c:if test="${not empty mediaImageUrl}">
          <span class="crs_web_content_image" style="background-image:url(${media.thumbnailImage.url}); background-size: 100% 100%;">  
        </c:if>
          <span><c:out value="${media.title}"/></span>
        </span>
      </a>
    </div>
  </preview:repositoryItem>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/global/gadgets/pdfTile.jsp#1 $$Change: 1497274 $--%>

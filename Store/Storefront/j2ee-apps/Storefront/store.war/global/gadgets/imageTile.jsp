<%--
  Renders the image in the spotlight with the thumbnail image and image title.
  
  Required parameters:
    mediaItem
      The video media item to render.
      
    Optional Parameters:
      none
--%>
<dsp:page>
  <dsp:getvalueof var="media" param="mediaItem"/>
  <dsp:getvalueof var="contentIndex" param="contentIndex"/>
  <c:set var="mediaImageUrl" value="${media.thumbnailImage.url}"/>
  <c:set var="mediaImageName" value="${media.thumbnailImage.name}"/>
  
  <preview:repositoryItem item="${media}">
  <div class="crs_tile_container">
    <a href="#" class="crs_web_content_height" onclick="dijit.byId('media${contentIndex}').show();">
      <c:if test="${empty mediaImageUrl}">
        <span class="crs_web_content_generic_image">
      </c:if>
      <c:if test="${not empty mediaImageUrl}">
        <span class="crs_web_content_image" style="background-image:url(${media.thumbnailImage.url}); background-size: 100% 100%;">  
      </c:if>
        <span><c:out value="${media.title}"/></span>
      </span>
    </a>
	  <span id="media${contentIndex}" dojoType="atg.store.Dialog" closable="true" class="crs_web_content_dialog">
      <a href="#" class="atg_store_gwpClose" onclick="dijit.byId('media${contentIndex}').hide();">X</a>
      <div class="crs_media_align">
	    <img class="image-dimensions" src="${media.url}" />
	  </div>
    </span>
  </div>
  </preview:repositoryItem>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/global/gadgets/imageTile.jsp#1 $$Change: 1497274 $--%>

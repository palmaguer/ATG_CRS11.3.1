<%--
  Renders the audio in the spotlight with the thumbnail image and audio tile.
  
  Required parameters:
    mediaItem
      The audio media item to render.
      
    Optional Parameters:
      none
--%>
<dsp:page>
  <dsp:getvalueof var="media" param="mediaItem"/>
  <dsp:getvalueof var="contentIndex" param="contentIndex"/>
  <preview:repositoryItem item="${media}">
    <c:set var="mediaImageUrl" value="${media.thumbnailImage.url}"/>
    <c:set var="mediaImageName" value="${media.thumbnailImage.name}"/>
    <div class="crs_tile_container">
	  <a href="#" class="crs_web_content_height" onclick="dijit.byId('media${contentIndex}').show();">
	    <c:if test="${empty mediaImageUrl}">
          <span class="crs_web_content_generic_audio">
        </c:if>
        <c:if test="${not empty mediaImageUrl}">
          <span class="crs_web_content_image" style="background-image:url(${media.thumbnailImage.url}); background-size: 100% 100%;">  
        </c:if>
          <span><c:out value="${media.title}"/></span>
          <img src="/crsdocroot/content/images/icon_play.png">
        </span>
	  </a>
 	  <span id="media${contentIndex}" dojoType="atg.store.Dialog" closable="true" class="crs_audio_content_dialog">
        <a href="#" class="atg_store_gwpClose" onclick="dijit.byId('media${contentIndex}').hide();">X</a>
        <audio id="audio_${contentIndex}" controls poster="${mediaImageUrl}" width="600" height="350" class="crs_media_align">
          <source src="${media.url}" type='audio/mpeg' />
          <source src="${media.url}" type='audio/ogg' />
          <fmt:message key="webcontent.audio_not_supported" />
        </audio>
      </span>
    </div>
  </preview:repositoryItem>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/global/gadgets/audioTile.jsp#1 $$Change: 1497274 $--%>
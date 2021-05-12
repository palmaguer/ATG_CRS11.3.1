<%--
  Renders the video in the spotlight with the thumbnail image and video title.
  
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
        <span class="crs_web_content_generic_video">
      </c:if>
      <c:if test="${not empty mediaImageUrl}">
        <span class="crs_web_content_image" style="background-image:url(${media.thumbnailImage.url}); background-size: 100% 100%;">  
      </c:if>
        <span><c:out value="${media.title}"/></span>
        <img src="/crsdocroot/content/images/icon_play.png">
      </span>
    </a>
    <div id="media${contentIndex}" dojoType="atg.store.Dialog" closable="true" class="crs_web_content_dialog">
      <a href="#" class="atg_store_gwpClose" onclick="dijit.byId('media${contentIndex}').hide();"></a>
      <div class="crs_media_align"> 
      
        <%-- Check for you tube video --%>
        <c:if test="${media.url.contains('/embed/')}">
          <iframe width="600" height="350" src="" frameborder="0" allowfullscreen></iframe>
        </c:if>
        
        <c:if test="${not media.url.contains('/embed/')}">
          <video id="video_${media.id}" controls poster="${mediaImageUrl}" width="600" height="350">
            <source src="${media.url}" type='video/mp4' />
            <source src="${media.url}" type='video/webm' />
            <source src="${media.url}" type='video/ogg' />
            <fmt:message key="webcontent.video_not_supported" />
          </video>
        </c:if>
      </div>
    </div>
  </div>
  </preview:repositoryItem>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/global/gadgets/videoTile.jsp#1 $$Change: 1497274 $--%>

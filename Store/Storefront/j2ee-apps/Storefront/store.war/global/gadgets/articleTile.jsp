<%--
  Renders the article in the spotlight with the thumbnail image and article name.
  
  Required parameters:
    articleItem
      The article item to render.
      
  Optional Parameters:
    none
--%>
<dsp:page>
  <dsp:getvalueof var="article" param="articleItem"/>
  <preview:repositoryItem item="${article}">
    <c:set var="articleImageUrl" value="${article.thumbnailImage.url}"/>
    <c:set var="articleImageName" value="${article.thumbnailImage.name}"/>
    <div class="crs_tile_container">
      <a class="crs_web_content_height" href="${originatingRequest.contextPath}/browse/articleDetails.jsp?articleId=${article.repositoryId}">
        <c:if test="${empty articleImageUrl}">
          <span class="crs_web_content_generic_article">
        </c:if>
        <c:if test="${not empty articleImageUrl}">
          <span class="crs_web_content_image" style="background-image:url(${articleImageUrl}); background-size: 100% 100%;">  
        </c:if>
          <span><c:out value="${article.headline}"/></span>
          <span><c:out value="${article.author}"/></span>
        </span>
      </a>
    </div>
  </preview:repositoryItem>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/global/gadgets/articleTile.jsp#1 $$Change: 1497274 $--%>
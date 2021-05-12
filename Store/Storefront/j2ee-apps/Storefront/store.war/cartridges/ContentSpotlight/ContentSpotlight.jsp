<%--
  Renders a list of articles,videos,pdf within a Dojo horizontal scroller widget.
  The widget provides paging by means of previous and next buttons
  to move back/forward through sub pages of products. 

  Required parameters:
    contentItem
      The content item to render.
      
  Optional Parameters:
    none
--%>

<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest" />
  <dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
  <dsp:importbean bean="/atg/endeca/store/droplet/ActionURLDroplet"/>
  <dsp:importbean bean="/atg/multisite/Site"/>
  
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
  <dsp:getvalueof var="itemsPerPage" value="${contentItem.itemsPerPage}"  /> 
  <dsp:getvalueof var="contextPath" vartype="java.lang.String" value="${originatingRequest.contextPath}"/>
  <c:set var="totalProducts" value="${fn:length(contentItem.records)}"/>
  <c:set var="pagingAction" value="${contentItem.pagingActionTemplate.navigationState}" />

  <endeca:previewAnchor contentItem="${contentItem}">
  <%--
    In order to support multiple widgets of the same type on the same page
    each widget instance needs a unique id. spotlightInstanceNo will be incremented
    for each widget and is appended to widget id to ensure it is unique
  --%>
  <c:choose>
    <c:when test='${empty spotlightInstanceNo}'>
      <c:set var="spotlightInstanceNo" value="0" scope="request"/>
    </c:when>
    <c:otherwise>
      <c:set var="spotlightInstanceNo" value="${spotlightInstanceNo + 1}" scope="request"/>
    </c:otherwise>
  </c:choose>

  <%-- 
    Since we are now displaying at least one article or media content,
    the title and 'See All' link can be displayed. 
  --%>
  <c:if test="${totalProducts > 0}">

    <%-- displays the spotlight title based on the title key  --%>
    <div id="atg_store_spotlightText">
      <h3>
        <crs:outMessage key="${contentItem.title}" />
      </h3>
    </div>
  </c:if>
  

  <%--
    Instantiate the Dojo widget, passing in values for any properties that are
    required or that require a value other than the default value. See 
    ScrollableProductSpotlight.js
  --%>
  <span dojotype="atg.store.widget.ScrollableProductSpotlight"
        id="scrollableProductSpotlight${spotlightInstanceNo}"
        productsPerPage="${itemsPerPage}"
        totalProducts="${totalProducts}"
        class="crs_generic_spotlight"
        style="display: none">
         
    <div class="carousel_inner">
      <c:forEach var="record" items="${contentItem.records}" varStatus="status" begin="0" end="11">
        <dsp:getvalueof var="articleId" value="${record.attributes['article.repositoryId']}"/>
        <dsp:getvalueof var="mediaContentId" value="${record.attributes['mediaContent.repositoryId']}"/>
        <c:if test="${not empty articleId}">
          <%--
            Get the article item using the repository id returned from the record attribute.
                 
            Input Parameters:
              id
                The article id we want to look up.

              Open Parameters:
                output
                  Serviced when no errors occur.
          
              Output Parameters:
                items
                  list of items matches the repository id.
          --%> 		  
          <dsp:droplet name="/atg/content/droplet/ArticleLookup">
            <dsp:param name="id" value="${articleId}"/>
            <dsp:oparam name="output">
              <div class="product crs_content_spotlight_height">
                <dsp:getvalueof var="article" param="element"/>
                <dsp:include page="/global/gadgets/articleTile.jsp">
                  <dsp:param name="articleItem" value="${article}"/>
                </dsp:include>
              </div>
            </dsp:oparam>
          </dsp:droplet>
        </c:if>
        <c:if test="${not empty mediaContentId}">
          <%--
            Get the media content item using the repository id returned from the record attribute.
                
            Input Parameters:
              id
                The media content id we want to look up.

              Open Parameters:
                output
                  Serviced when no errors occur.
          
              Output Parameters:
                items
                  list of items matches the repository id.
          --%>            
          <dsp:droplet name="/atg/content/droplet/MediaContentLookup">
            <dsp:param name="id" value="${mediaContentId}"/>
            <dsp:oparam name="output">
              <dsp:getvalueof var="media" param="element"/>
              <c:if test="${media.mediaType == 'Image'}">
                <div class="product crs_content_spotlight_height">
                  <dsp:include page="/global/gadgets/imageTile.jsp">
                    <dsp:param name="mediaItem" value="${media}"/>
                    <dsp:param name="contentIndex" value="${status.count}"/>
                  </dsp:include>
                </div>
              </c:if> 
              <c:if test="${media.mediaType == 'Video'}">
                <div class="product crs_content_spotlight_height">
                  <dsp:include page="/global/gadgets/videoTile.jsp">
                    <dsp:param name="mediaItem" value="${media}"/>
                    <dsp:param name="contentIndex" value="${status.count}"/>
                  </dsp:include>
                </div>
              </c:if>
              <c:if test="${media.mediaType == 'Audio'}">
                <div class="product crs_content_spotlight_height">
                  <dsp:include page="/global/gadgets/audioTile.jsp">
                    <dsp:param name="mediaItem" value="${media}"/>
                    <dsp:param name="contentIndex" value="${status.count}"/>
                  </dsp:include>
                </div>
              </c:if> 
              <c:if test="${media.mediaType == 'PDF'}">
                <div class="product crs_content_spotlight_height">
                  <dsp:include page="/global/gadgets/pdfTile.jsp">
                    <dsp:param name="mediaItem" value="${media}"/>
                  </dsp:include>
                </div>
              </c:if>       
            </dsp:oparam>
          </dsp:droplet>
        </c:if>
      </c:forEach>
    </div>
	
    <%-- Renders the pagination for scrollable widget. --%>
    <c:if test="${totalProducts > itemsPerPage}">
      <div class="navigationWrapper">
        <div class="navigation">
          <span class="control previous"></span>
          <span class="pageNum"></span>
          <span class="control next"></span>
        </div>
      </div>
    </c:if>
  </span>
  </endeca:previewAnchor>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/cartridges/ContentSpotlight/ContentSpotlight.jsp#1 $$Change: 1497274 $--%>

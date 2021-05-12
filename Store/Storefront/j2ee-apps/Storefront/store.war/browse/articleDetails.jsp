<%--
  Renders Article Detail cartridge content.

  Required parameters:
    None.
    
  Optional parameters:
    None.
--%>
<dsp:page>
  <crs:pageContainer>
    <dsp:getvalueof var="articleId" param="articleId"/>
    <dsp:importbean bean="/atg/store/droplet/ItemValidatorDroplet" />
    <%--
      Get the article item using the repository id returned from the record attribute.
             
      Input Parameters:
        id
          The RQL with the article id we want to look up.

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
        <dsp:getvalueof var="article" param="element"/>
        <dsp:droplet name="ItemValidatorDroplet">
          <dsp:param name="item" value="${article}"/>    
          <dsp:oparam name="true">
            <preview:repositoryItem item="${article}">
              <div class="article-content">
                <h1>
                  <c:out value="${article.headline}" />
                </h1>
                <fmt:message var="articleBy" key="article_details.by"/>
                <c:if test="${not empty fn:trim(article.author)}">
                  <c:out value="${articleBy}"/>
                  <c:out value="${article.author}" /> 
                </c:if>
                <c:out value="${article.postDate}" />
                <img src="${article.mainImage.url}" alt="${article.mainImage.url}"/>
                <div id="article-abstract-div">
                  <p>
                    <strong>
                      ${article.abstract}
                    </strong>
                  </p>
                </div>
                <div id="article-body-div">
                  <p>
                    ${article.body}
                  </p>
                </div>
              </div>
            </preview:repositoryItem>  
            <div>
              <dsp:include page="/browse/gadgets/relatedProducts.jsp">
                <dsp:param name="article" value="${article}"/>
              </dsp:include>
            </div>
          </dsp:oparam>
          <dsp:oparam name="false">              
            <div id="" class=" atg_store_generalMessage">
              <div class="errorMessage"> 
                <fmt:message key="common.articleNotAvailable"/>
              </div>
            </div>              
          </dsp:oparam>
        </dsp:droplet>
      </dsp:oparam>
    </dsp:droplet>    
  </crs:pageContainer>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Storefront/j2ee/store.war/browse/articleDetails.jsp#1 $$Change: 1497274 $--%>
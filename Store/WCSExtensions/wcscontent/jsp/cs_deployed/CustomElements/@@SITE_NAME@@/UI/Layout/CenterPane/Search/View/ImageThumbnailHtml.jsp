<%@page import="com.fatwire.cs.ui.framework.UIException"
%><%@page import="java.util.*"
%><%@page import="org.apache.commons.collections.CollectionUtils"
%><%@page import="org.apache.commons.lang.StringUtils"
%><%@page import="com.fatwire.ui.util.GenericUtil"
%><%@ page import="com.fatwire.services.ui.beans.LabelValueBean"
%><%@ page import="org.codehaus.jackson.map.ObjectMapper"
%><%@ page import="org.codehaus.jackson.type.TypeReference"
%><%@page import="com.fatwire.system.Session"
%><%@page import="com.fatwire.system.SessionFactory"
%><%@page import="com.fatwire.assetapi.data.AssetDataManager"
%><%@page import="com.fatwire.assetapi.data.AssetData"
%><%@page import="com.openmarket.xcelerate.asset.AssetIdImpl"
%><%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld"
%><cs:ftcs><xlat:lookup key="UI/Search/ViewFullSizeImage" escape="true" varname="viewfullsizeimage"/>
<xlat:lookup key="UI/Search/NoImage" escape="true" varname="noimage"/>
<%
try {

	//get the json data from client and convert to java object
	// this data is for display text we show for each image
	String jsonData = request.getParameter("displayData");
	List<LabelValueBean> displayBeans = new ArrayList<LabelValueBean>();
	if(StringUtils.isNotBlank(jsonData)) {
		displayBeans =  new ObjectMapper().readValue(jsonData, new TypeReference<List<LabelValueBean>>(){});
	}
	String id = StringUtils.defaultString(request.getParameter("id"));
	String type = StringUtils.defaultString(request.getParameter("type"));
	String name = StringUtils.defaultString(request.getParameter("name"));
	String viewMode = StringUtils.defaultString(request.getParameter("viewMode"));
	String docType = StringUtils.defaultString(request.getParameter("docType"), "asset");
	String docId = StringUtils.defaultString(request.getParameter("docId"), type+ ":"+id);
	StringBuilder buf = new StringBuilder();
	StringBuilder builder = new StringBuilder();
	String fileName = name;
	if(GenericUtil.isSupported(ics,type)){
		builder.append("<a href= \"#\" onclick=\"fw.ui.GridFormatter.open('").append(docType).append("' , '").append(docId).append("')\"");
		if(StringUtils.equals(viewMode, "normal")){
			builder.append(" title=\"" + name + "\"");
		}
		builder.append(">"+name+"</a>");
	}
	else{
		builder.append(name);
	}
  
  Session csSession = SessionFactory.getSession();
  String imageUrlAttribute = StringUtils.defaultString(request.getParameter("attribute"));
  AssetDataManager manager = (AssetDataManager) csSession.getManager(AssetDataManager.class.getName());
  List<String> attributeNames = new ArrayList<String>(1);
  attributeNames.add(imageUrlAttribute);
  AssetData assetData = manager.readAttributes(new AssetIdImpl(type, Long.parseLong(id)), attributeNames);
  String url = (String) assetData.getAttributeData(imageUrlAttribute).getData();
  String tooltipImageUrl = url;
	if(StringUtils.isNotBlank(url)) {
		tooltipImageUrl = url;
		buf.append("<div class='thumbnailSearchContent'>");
    buf.append("<div class='thumbnailImage'><a><img style='max-width:100%; max-height:100%;' src='");
		buf.append(url);
		buf.append("' alt='' /></a></div>");
		if(CollectionUtils.isNotEmpty(displayBeans)) {
			buf.append("<div class='thumbnailSearchLabel'>");
			for(LabelValueBean displayBean : displayBeans) {
				if(StringUtils.equalsIgnoreCase(displayBean.getLabel(), "name")) {
					buf.append("<div class='thumbnailSearchLabelTitle'><div class='ellipsis'><strong>").append(builder).append("</strong></div></div>");
					fileName = displayBean.getValue();
				} else {
					if(displayBean.getValue() != null && StringUtils.isNotBlank(displayBean.getValue())){
						buf.append("<strong>").append(displayBean.getLabel()).append(":</strong> ").append(displayBean.getValue()).append("<br />");
					}
				}
				// For "dock" view show the first item from the display list
				if(StringUtils.equals(viewMode, "dock"))
					break;
			}
			buf.append("</div>");
		}	
		buf.append("<div class='enlargeImageIcon'><a href='#' alt='' title='"+ ics.GetVar("viewfullsizeimage")+"' onclick=\"fw.ui.GridFormatter.openFullImage('").append(fileName).append("','").append(tooltipImageUrl).append("')\">&nbsp;</a></div>");
	} else {
		buf.append( "<img src='js/fw/images/ui/ui/search/noImage.png' alt='"+ics.GetVar("noimage")+"' width='170' height='170' />");
	}
%><%=buf.toString()%><%
} catch(Exception e) {
	UIException uie = new UIException(e);
	request.setAttribute(UIException._UI_EXCEPTION_, uie);
	throw uie;
}%></cs:ftcs>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/wcscontent/jsp/cs_deployed/CustomElements/%40%40SITE_NAME%40%40/UI/Layout/CenterPane/Search/View/ImageThumbnailHtml.jsp#2 $$Change: 1505503 $--%>

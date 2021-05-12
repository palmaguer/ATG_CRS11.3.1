<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<cs:ftcs>
	<thumbnailviewconfig>
		<numberofitems>1000</numberofitems>
		<!-- configure the following if need to sort other than relevance by default-->
		<defaultsortfield></defaultsortfield>
		<defaultsortorder></defaultsortorder>
		<numberofitemsperpage>10</numberofitemsperpage>
		<formatter>fw.ui.GridFormatter.thumbnailFormatter</formatter>
		
		<!-- Below are the fields to display for each thumbnail image -->
		<!-- From the column definition below only the first column is displayed in the docked version 
		and the rest are displayed as part of tooltip  if the displayinttoltip element is true-->
		<fields>
			<field id="name">
				<fieldname>name</fieldname>
				<displayname><xlat:stream key="dvin/Common/Name" escape="true"/></displayname>
				<displayintooltip>true</displayintooltip>
			</field>
			<field id="AssetType_Description">
				<fieldname>AssetType_Description</fieldname>
				<displayname><xlat:stream key="dvin/Common/Type" escape="true"/></displayname>
				<displayintooltip>true</displayintooltip>
			</field>
			<field id="updateddate">
				<fieldname>updateddate</fieldname>
				<displayname><xlat:stream key="dvin/Common/Modified" escape="true"/></displayname>
				<displayintooltip>true</displayintooltip>
				<!-- dateformat is  an option to specify custom date format. This should be a valid java date format string 
				 If there is any dateformat string specified here it will be used to format the	date.-->
				<!-- <dateformat>MM/dd/yyyy hh:mm a z </dateformat> -->
				
				<!-- javadateformat will be used if there is no dataformat element present. 
				Valid values are SHORT, MEDIUM, LONG and FULL. Again if this element is not present or no value specified,
				system uses SHORT by default -->
				<javadateformat>SHORT</javadateformat>
			</field>
		</fields>
		<assettypes>
			<assettype id="CRSImage_CRSImage">
				<type>CRSImage</type>
				<subtype>CRSImage</subtype>
				<element>CustomElements/@@SITE_NAME@@/UI/Layout/CenterPane/Search/View/ImageThumbnail</element>
				<attribute>externalURL</attribute>
			</assettype>
		</assettypes>
	</thumbnailviewconfig>
</cs:ftcs>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/wcscontent/jsp/cs_deployed/CustomElements/%40%40SITE_NAME%40%40/CRSImage/UI/Layout/CenterPane/Search/View/ThumbnailViewConfig.jsp#2 $$Change: 1505503 $--%>

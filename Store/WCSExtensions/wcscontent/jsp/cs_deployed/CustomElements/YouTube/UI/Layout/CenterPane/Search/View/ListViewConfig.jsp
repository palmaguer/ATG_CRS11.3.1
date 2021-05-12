<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld"
%><cs:ftcs>
	<listviewconfig>
		<numberofitems>1000</numberofitems>
		<numberofitemsperpage>25</numberofitemsperpage>
		<pagestepsize>"10", "25"</pagestepsize>
		<fields>
			<field id="name">
				<fieldname>name</fieldname>
				<displayname><xlat:stream key="dvin/AT/Common/Name"/></displayname>
				<width>350px</width>
				<formatter>fw.ui.GridFormatter.nameFormatter</formatter>
				<displayintooltip>true</displayintooltip>
			</field>
			<field id="updateddate">
				<fieldname>updateddate</fieldname>
				<displayname><xlat:stream key="dvin/Common/Modified"/></displayname>
				<width>160px</width>
				<formatter></formatter>
				<displayintooltip>true</displayintooltip>
			</field>
			<field id="viewed">
				<fieldname>viewed</fieldname>
				<displayname><xlat:stream key="UI/UC1/Layout/ViewCount" escape="true"/></displayname>
				<width>auto</width>
				<formatter></formatter>
				<displayintooltip>true</displayintooltip>
			</field>
		</fields>
	</listviewconfig>
</cs:ftcs>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/wcscontent/jsp/cs_deployed/CustomElements/YouTube/UI/Layout/CenterPane/Search/View/ListViewConfig.jsp#1 $$Change: 1497274 $--%>




--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/sql/ddlgen/order_ddl.xml#1 $$Change: 1497274 $

create table crs_iss_ship_grp (
	shipping_group_id	varchar(40)	not null,
	location_id	varchar(40)	not null
,constraint crs_iss_ship_g_p primary key (shipping_group_id)
,constraint crs_issshippng__f foreign key (shipping_group_id) references dcspp_ship_group (shipping_group_id));

commit;



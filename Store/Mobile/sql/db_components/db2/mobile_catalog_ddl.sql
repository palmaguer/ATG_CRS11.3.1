


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/mobile_catalog_ddl.xml#1 $$Change: 1497274 $

create table crs_mobile_img (
	promo_content_id	varchar(40)	not null,
	device_name	varchar(254)	default null,
	url	varchar(254)	default null);


create table crs_mobile_link (
	promo_content_id	varchar(40)	not null,
	device_name	varchar(254)	default null,
	link_url	varchar(256)	default null);

commit;



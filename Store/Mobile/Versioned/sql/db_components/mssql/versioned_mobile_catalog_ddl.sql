


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/mobile_catalog_ddl.xml#1 $$Change: 1497274 $

create table crs_mobile_img (
	asset_version	numeric(19)	not null,
	promo_content_id	varchar(40)	not null,
	device_name	varchar(254)	null,
	url	varchar(254)	null)


create table crs_mobile_link (
	asset_version	numeric(19)	not null,
	promo_content_id	varchar(40)	not null,
	device_name	varchar(254)	null,
	link_url	varchar(256)	null)



go

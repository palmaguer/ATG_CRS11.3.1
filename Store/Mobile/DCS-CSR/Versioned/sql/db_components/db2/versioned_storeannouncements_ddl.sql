


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/sql/ddlgen/storeannouncements_ddl.xml#1 $$Change: 1497274 $

create table crs_store_ancmnt (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	id	varchar(40)	not null,
	title	varchar(254)	default null,
	content	varchar(4000)	default null,
	creation_time	timestamp	default null,
	enabled	numeric(1)	not null
,constraint crs_ancmnt_key_p primary key (id,asset_version));

create index crs_store_ancm_wsx on crs_store_ancmnt (workspace_id);
create index crs_store_ancm_cix on crs_store_ancmnt (checkin_date);

create table crs_store_ancmnt_sts (
	asset_version	numeric(19)	not null,
	store_id	varchar(40)	not null,
	announcement_id	varchar(40)	not null
,constraint crs_ancmnt_s_key_p primary key (announcement_id,store_id,asset_version));

commit;






--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/sql/ddlgen/storeannouncements_ddl.xml#1 $$Change: 1497274 $

create table crs_store_ancmnt (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	id	varchar(40)	not null,
	title	varchar(254)	null,
	content	longtext	null,
	creation_time	datetime	null,
	enabled	tinyint	not null
,constraint crs_ancmnt_key_p primary key (id,asset_version));

create index crs_store_ancm_wsx on crs_store_ancmnt (workspace_id);
create index crs_store_ancm_cix on crs_store_ancmnt (checkin_date);

create table crs_store_ancmnt_sts (
	asset_version	bigint	not null,
	store_id	varchar(40)	not null,
	announcement_id	varchar(40)	not null
,constraint crs_ancmnt_s_key_p primary key (announcement_id,store_id,asset_version));

commit;



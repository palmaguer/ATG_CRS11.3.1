


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/location_i18n_ddl.xml#1 $$Change: 1497274 $

create table crs_ibcnalrt_xlate (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	translation_id	varchar(40)	not null,
	alert_text	varchar(254)	null
,constraint crsibcnalrtxlate_p primary key (translation_id,asset_version));

create index crs_ibcnalrt_x_wsx on crs_ibcnalrt_xlate (workspace_id);
create index crs_ibcnalrt_x_cix on crs_ibcnalrt_xlate (checkin_date);

create table crs_alrt_alrt_xlate (
	asset_version	bigint	not null,
	alert_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crsalrtalrtxlt_p primary key (alert_id,locale,asset_version));

create index crsalrtalrtxlt_ix on crs_alrt_alrt_xlate (translation_id);
commit;






--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/location_ddl.xml#1 $$Change: 1497274 $

create table crs_ibeacon_alert (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	alert_id	varchar(40)	not null,
	name	varchar(254)	null,
	alert_text	nvarchar(160)	null,
	alert_key	varchar(254)	null,
	link_url	nvarchar(254)	null,
	is_push	tinyint	null
,constraint crsbeaconalert_pk primary key (alert_id,asset_version));

create index crs_ibeacon_al_wsx on crs_ibeacon_alert (workspace_id);
create index crs_ibeacon_al_cix on crs_ibeacon_alert (checkin_date);

create table crs_ibeacon (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	ibeacon_id	varchar(40)	not null,
	name	varchar(254)	null,
	uuid	varchar(254)	not null,
	major	varchar(254)	null,
	minor	varchar(254)	null,
	identifier	varchar(254)	null
,constraint crsbeacon_pk primary key (ibeacon_id,asset_version));

create index crs_ibeacon_wsx on crs_ibeacon (workspace_id);
create index crs_ibeacon_cix on crs_ibeacon (checkin_date);

create table crs_ibcn_stores (
	sec_asset_version	bigint	not null,
	asset_version	bigint	not null,
	ibeacon_id	varchar(40)	not null,
	location_id	varchar(40)	not null
,constraint crsibcnstores_pk primary key (ibeacon_id,location_id,asset_version,sec_asset_version));


create table crs_ibcnalrt_entr (
	asset_version	bigint	not null,
	ibeacon_id	varchar(40)	not null,
	alert_id	varchar(40)	not null,
	sequence_num	integer	not null
,constraint crsibcnalrtentr_p primary key (ibeacon_id,sequence_num,asset_version));


create table crs_ibcnalrt_exit (
	asset_version	bigint	not null,
	ibeacon_id	varchar(40)	not null,
	alert_id	varchar(40)	not null,
	sequence_num	integer	not null
,constraint crsibcnalrtexit_p primary key (ibeacon_id,sequence_num,asset_version));

commit;



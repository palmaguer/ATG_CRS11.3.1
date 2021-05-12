


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/sql/ddlgen/storecontent_ddl.xml#1 $$Change: 1497274 $

create table crs_store_image (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	image_id	varchar(40)	not null,
	name	varchar(255)	not null,
	external_id	varchar(40)	default null,
	url	varchar(255)	not null,
	creation_date	timestamp	default null,
	last_modified_date	timestamp	default null
,constraint crs_store_image_p primary key (image_id,asset_version));

create index crs_store_imag_wsx on crs_store_image (workspace_id);
create index crs_store_imag_cix on crs_store_image (checkin_date);

create table crs_store_video (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	video_id	varchar(40)	not null,
	name	varchar(255)	not null,
	external_id	varchar(40)	default null,
	url	varchar(255)	not null,
	creation_date	timestamp	default null,
	last_modified_date	timestamp	default null
,constraint crs_store_video_p primary key (video_id,asset_version));

create index crs_store_vide_wsx on crs_store_video (workspace_id);
create index crs_store_vide_cix on crs_store_video (checkin_date);

create table crs_store_article (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	article_id	varchar(40)	not null,
	name	varchar(255)	not null,
	external_id	varchar(40)	default null,
	headline	varchar(255)	default null,
	subheadline	varchar(255)	default null,
	abstract	clob	default null,
	article_body	clob	default null,
	main_image_id	varchar(40)	default null,
	author	varchar(255)	default null,
	post_date	timestamp	default null,
	start_date	timestamp	default null,
	end_date	timestamp	default null,
	creation_date	timestamp	default null,
	last_modified_date	timestamp	default null
,constraint crs_store_article_p primary key (article_id,asset_version));

create index crs_store_arti_wsx on crs_store_article (workspace_id);
create index crs_store_arti_cix on crs_store_article (checkin_date);
commit;



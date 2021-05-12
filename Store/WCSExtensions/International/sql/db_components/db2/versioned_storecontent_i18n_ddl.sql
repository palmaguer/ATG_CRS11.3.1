


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/International/sql/ddlgen/storecontent_i18n_ddl.xml#1 $$Change: 1497274 $

create table crs_image_xlate (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	translation_id	varchar(40)	not null,
	external_id	varchar(40)	default null,
	name	varchar(255)	not null
,constraint crs_image_xlate_p primary key (translation_id,asset_version));

create index crs_image_xlat_wsx on crs_image_xlate (workspace_id);
create index crs_image_xlat_cix on crs_image_xlate (checkin_date);

create table crs_img_img_xlate (
	asset_version	numeric(19)	not null,
	image_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_img_img_xlate_p primary key (image_id,locale,asset_version));


create table crs_video_xlate (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	translation_id	varchar(40)	not null,
	external_id	varchar(40)	default null,
	name	varchar(255)	not null
,constraint crs_video_xlate_p primary key (translation_id,asset_version));

create index crs_video_xlat_wsx on crs_video_xlate (workspace_id);
create index crs_video_xlat_cix on crs_video_xlate (checkin_date);

create table crs_vid_vid_xlate (
	asset_version	numeric(19)	not null,
	video_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_vid_vid_xlate_p primary key (video_id,locale,asset_version));


create table crs_article_xlate (
	asset_version	numeric(19)	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	numeric(1)	not null,
	version_deleted	numeric(1)	not null,
	version_editable	numeric(1)	not null,
	pred_version	numeric(19)	default null,
	checkin_date	timestamp	default null,
	translation_id	varchar(40)	not null,
	external_id	varchar(40)	default null,
	headline	varchar(255)	default null,
	subheadline	varchar(255)	default null,
	abstract	clob	default null,
	article_body	clob	default null,
	author	varchar(255)	default null
,constraint crs_article_xlate_p primary key (translation_id,asset_version));

create index crs_article_xl_wsx on crs_article_xlate (workspace_id);
create index crs_article_xl_cix on crs_article_xlate (checkin_date);

create table crs_art_art_xlate (
	asset_version	numeric(19)	not null,
	article_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_art_art_xlate_p primary key (article_id,locale,asset_version));

commit;






--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/sql/ddlgen/storecontent_ddl.xml#1 $$Change: 1497274 $

create table crs_store_image (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	image_id	varchar(40)	not null,
	name	nvarchar(255)	not null,
	external_id	varchar(40)	null,
	url	varchar(255)	not null,
	creation_date	datetime	null,
	last_modified_date	datetime	null
,constraint crs_store_image_p primary key (image_id,asset_version));

create index crs_store_imag_wsx on crs_store_image (workspace_id);
create index crs_store_imag_cix on crs_store_image (checkin_date);

create table crs_store_video (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	video_id	varchar(40)	not null,
	name	nvarchar(255)	not null,
	external_id	varchar(40)	null,
	url	varchar(255)	not null,
	creation_date	datetime	null,
	last_modified_date	datetime	null
,constraint crs_store_video_p primary key (video_id,asset_version));

create index crs_store_vide_wsx on crs_store_video (workspace_id);
create index crs_store_vide_cix on crs_store_video (checkin_date);

create table crs_store_article (
	asset_version	bigint	not null,
	workspace_id	varchar(40)	not null,
	branch_id	varchar(40)	not null,
	is_head	tinyint	not null,
	version_deleted	tinyint	not null,
	version_editable	tinyint	not null,
	pred_version	bigint	null,
	checkin_date	datetime	null,
	article_id	varchar(40)	not null,
	name	nvarchar(255)	not null,
	external_id	varchar(40)	null,
	headline	nvarchar(255)	null,
	subheadline	nvarchar(255)	null,
	abstract	longtext charset utf8	null,
	article_body	longtext charset utf8	null,
	main_image_id	varchar(40)	null,
	author	varchar(255)	null,
	post_date	datetime	null,
	start_date	datetime	null,
	end_date	datetime	null,
	creation_date	datetime	null,
	last_modified_date	datetime	null
,constraint crs_store_article_p primary key (article_id,asset_version));

create index crs_store_arti_wsx on crs_store_article (workspace_id);
create index crs_store_arti_cix on crs_store_article (checkin_date);
commit;



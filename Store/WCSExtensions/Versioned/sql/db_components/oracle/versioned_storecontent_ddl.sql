


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/sql/ddlgen/storecontent_ddl.xml#1 $$Change: 1497274 $

create table crs_store_image (
	asset_version	number(19)	not null,
	workspace_id	varchar2(40)	not null,
	branch_id	varchar2(40)	not null,
	is_head	number(1)	not null,
	version_deleted	number(1)	not null,
	version_editable	number(1)	not null,
	pred_version	number(19)	null,
	checkin_date	timestamp	null,
	image_id	varchar2(40)	not null,
	name	varchar2(255)	not null,
	external_id	varchar2(40)	null,
	url	varchar2(255)	not null,
	creation_date	timestamp	null,
	last_modified_date	timestamp	null
,constraint crs_store_image_p primary key (image_id,asset_version));

create index crs_store_imag_wsx on crs_store_image (workspace_id);
create index crs_store_imag_cix on crs_store_image (checkin_date);

create table crs_store_video (
	asset_version	number(19)	not null,
	workspace_id	varchar2(40)	not null,
	branch_id	varchar2(40)	not null,
	is_head	number(1)	not null,
	version_deleted	number(1)	not null,
	version_editable	number(1)	not null,
	pred_version	number(19)	null,
	checkin_date	timestamp	null,
	video_id	varchar2(40)	not null,
	name	varchar2(255)	not null,
	external_id	varchar2(40)	null,
	url	varchar2(255)	not null,
	creation_date	timestamp	null,
	last_modified_date	timestamp	null
,constraint crs_store_video_p primary key (video_id,asset_version));

create index crs_store_vide_wsx on crs_store_video (workspace_id);
create index crs_store_vide_cix on crs_store_video (checkin_date);

create table crs_store_article (
	asset_version	number(19)	not null,
	workspace_id	varchar2(40)	not null,
	branch_id	varchar2(40)	not null,
	is_head	number(1)	not null,
	version_deleted	number(1)	not null,
	version_editable	number(1)	not null,
	pred_version	number(19)	null,
	checkin_date	timestamp	null,
	article_id	varchar2(40)	not null,
	name	varchar2(255)	not null,
	external_id	varchar2(40)	null,
	headline	varchar2(255)	null,
	subheadline	varchar2(255)	null,
	abstract	clob	null,
	article_body	clob	null,
	main_image_id	varchar2(40)	null,
	author	varchar2(255)	null,
	post_date	timestamp	null,
	start_date	timestamp	null,
	end_date	timestamp	null,
	creation_date	timestamp	null,
	last_modified_date	timestamp	null
,constraint crs_store_article_p primary key (article_id,asset_version));

create index crs_store_arti_wsx on crs_store_article (workspace_id);
create index crs_store_arti_cix on crs_store_article (checkin_date);




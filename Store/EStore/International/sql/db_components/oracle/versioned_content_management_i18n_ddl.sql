


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/International/sql/ddlgen/content_management_i18n_ddl.xml#1 $$Change: 1497274 $

create table crs_media_content_xlate (
	asset_version	number(19)	not null,
	workspace_id	varchar2(40)	not null,
	branch_id	varchar2(40)	not null,
	is_head	number(1)	not null,
	version_deleted	number(1)	not null,
	version_editable	number(1)	not null,
	pred_version	number(19)	null,
	checkin_date	timestamp	null,
	translation_id	varchar2(40)	not null,
	title	varchar2(255)	null,
	description	varchar2(255)	null
,constraint crs_media_content_xlate_p primary key (translation_id,asset_version));

create index crs_media_cont_wsx on crs_media_content_xlate (workspace_id);
create index crs_media_cont_cix on crs_media_content_xlate (checkin_date);

create table crs_media_media_xlate (
	asset_version	number(19)	not null,
	id	varchar2(40)	not null,
	locale	varchar2(40)	not null,
	translation_id	varchar2(40)	not null
,constraint crs_media_media_xlate_p primary key (id,locale,asset_version));


create table crs_content_article_xlate (
	asset_version	number(19)	not null,
	workspace_id	varchar2(40)	not null,
	branch_id	varchar2(40)	not null,
	is_head	number(1)	not null,
	version_deleted	number(1)	not null,
	version_editable	number(1)	not null,
	pred_version	number(19)	null,
	checkin_date	timestamp	null,
	translation_id	varchar2(40)	not null,
	headline	varchar2(255)	null,
	abstract	clob	null,
	article_body	clob	null
,constraint crs_content_article_xlate_p primary key (translation_id,asset_version));

create index crs_content_ar_wsx on crs_content_article_xlate (workspace_id);
create index crs_content_ar_cix on crs_content_article_xlate (checkin_date);

create table crs_article_article_xlate (
	asset_version	number(19)	not null,
	id	varchar2(40)	not null,
	locale	varchar2(40)	not null,
	translation_id	varchar2(40)	not null
,constraint crs_article_article_xlate_p primary key (id,locale,asset_version));





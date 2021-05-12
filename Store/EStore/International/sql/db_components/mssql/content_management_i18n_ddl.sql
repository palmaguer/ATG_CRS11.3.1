


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/EStore/International/sql/ddlgen/content_management_i18n_ddl.xml#1 $$Change: 1497274 $

create table crs_media_content_xlate (
	translation_id	varchar(40)	not null,
	title	varchar(255)	null,
	description	varchar(255)	null
,constraint crs_media_content_xlate_p primary key (translation_id))


create table crs_media_media_xlate (
	id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_media_media_xlate_p primary key (id,locale)
,constraint crs_media_media_xlate_f1 foreign key (translation_id) references crs_media_content_xlate (translation_id))

create index crs_media_media_xlate_i1 on crs_media_media_xlate (translation_id)

create table crs_content_article_xlate (
	translation_id	varchar(40)	not null,
	headline	varchar(255)	null,
	abstract	text	null,
	article_body	text	null
,constraint crs_content_article_xlate_p primary key (translation_id))


create table crs_article_article_xlate (
	id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_article_article_xlate_p primary key (id,locale)
,constraint crs_article_article_xlate_f1 foreign key (translation_id) references crs_content_article_xlate (translation_id))

create index crs_article_article_xlate_i1 on crs_article_article_xlate (translation_id)


go

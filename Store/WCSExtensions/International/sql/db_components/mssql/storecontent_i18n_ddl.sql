


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/International/sql/ddlgen/storecontent_i18n_ddl.xml#1 $$Change: 1497274 $

create table crs_image_xlate (
	translation_id	varchar(40)	not null,
	external_id	varchar(40)	null,
	name	varchar(255)	not null
,constraint crs_image_xlate_p primary key (translation_id))


create table crs_img_img_xlate (
	image_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_img_img_xlate_p primary key (image_id,locale)
,constraint crs_img_img_xlate_f1 foreign key (translation_id) references crs_image_xlate (translation_id))

create index crs_img_img_xlate_i1 on crs_img_img_xlate (translation_id)

create table crs_video_xlate (
	translation_id	varchar(40)	not null,
	external_id	varchar(40)	null,
	name	varchar(255)	not null
,constraint crs_video_xlate_p primary key (translation_id))


create table crs_vid_vid_xlate (
	video_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_vid_vid_xlate_p primary key (video_id,locale)
,constraint crs_vid_vid_xlate_f1 foreign key (translation_id) references crs_video_xlate (translation_id))

create index crs_vid_vid_xlate_i1 on crs_vid_vid_xlate (translation_id)

create table crs_article_xlate (
	translation_id	varchar(40)	not null,
	external_id	varchar(40)	null,
	headline	varchar(255)	null,
	subheadline	varchar(255)	null,
	abstract	text	null,
	article_body	text	null,
	author	varchar(255)	null
,constraint crs_article_xlate_p primary key (translation_id))


create table crs_art_art_xlate (
	article_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crs_art_art_xlate_p primary key (article_id,locale)
,constraint crs_art_art_xlate_f1 foreign key (translation_id) references crs_article_xlate (translation_id))

create index crs_art_art_xlate_i1 on crs_art_art_xlate (translation_id)


go




--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/sql/ddlgen/storecontent_ddl.xml#1 $$Change: 1497274 $

create table crs_store_image (
	image_id	varchar2(40)	not null,
	name	varchar2(255)	not null,
	external_id	varchar2(40)	null,
	url	varchar2(255)	not null,
	creation_date	timestamp	null,
	last_modified_date	timestamp	null
,constraint crs_store_image_p primary key (image_id));


create table crs_store_video (
	video_id	varchar2(40)	not null,
	name	varchar2(255)	not null,
	external_id	varchar2(40)	null,
	url	varchar2(255)	not null,
	creation_date	timestamp	null,
	last_modified_date	timestamp	null
,constraint crs_store_video_p primary key (video_id));


create table crs_store_article (
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
,constraint crs_store_article_p primary key (article_id)
,constraint crs_store_article_f1 foreign key (main_image_id) references crs_store_image (image_id));

create index crs_store_article_i1 on crs_store_article (main_image_id);




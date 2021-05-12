


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/WCSExtensions/sql/ddlgen/storecontent_ddl.xml#1 $$Change: 1497274 $

create table crs_store_image (
	image_id	varchar(40)	not null,
	name	varchar(255)	not null,
	external_id	varchar(40)	default null,
	url	varchar(255)	not null,
	creation_date	timestamp	default null,
	last_modified_date	timestamp	default null
,constraint crs_store_image_p primary key (image_id));


create table crs_store_video (
	video_id	varchar(40)	not null,
	name	varchar(255)	not null,
	external_id	varchar(40)	default null,
	url	varchar(255)	not null,
	creation_date	timestamp	default null,
	last_modified_date	timestamp	default null
,constraint crs_store_video_p primary key (video_id));


create table crs_store_article (
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
,constraint crs_store_article_p primary key (article_id)
,constraint crs_store_article_f1 foreign key (main_image_id) references crs_store_image (image_id));

create index crs_store_article_i1 on crs_store_article (main_image_id);
commit;



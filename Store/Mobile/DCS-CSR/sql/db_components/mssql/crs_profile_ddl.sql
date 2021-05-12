


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/sql/ddlgen/crs_profile_ddl.xml#1 $$Change: 1497274 $

create table crs_instore_user (
	id	varchar(40)	not null,
	user_id	varchar(40)	not null,
	location_id	varchar(40)	not null,
	ibeacon_id	varchar(40)	null,
	last_seen_date	datetime	not null,
	help_requested	tinyint	not null,
	help_request_date	datetime	null,
	displayed_user_name	varchar(40)	null
,constraint crs_instore_user_pk primary key (id))



go

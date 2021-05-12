


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/DCS-CSR/sql/ddlgen/crs_profile_ddl.xml#1 $$Change: 1497274 $

create table crs_instore_user (
	id	varchar(40)	not null,
	user_id	varchar(40)	not null,
	location_id	varchar(40)	not null,
	ibeacon_id	varchar(40)	default null,
	last_seen_date	timestamp	not null,
	help_requested	numeric(1)	not null,
	help_request_date	timestamp	default null,
	displayed_user_name	varchar(40)	default null
,constraint crs_instore_user_pk primary key (id));

commit;



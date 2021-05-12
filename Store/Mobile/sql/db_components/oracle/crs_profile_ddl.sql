


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/crs_profile_ddl.xml#1 $$Change: 1497274 $

create table crs_mobile_device (
	device_id	varchar2(40)	not null,
	unique_id	varchar2(255)	not null,
	push_token	varchar2(255)	null,
	user_id	varchar2(40)	null,
	os	number(10)	not null,
	os_ver	varchar2(40)	not null,
	app	number(10)	not null,
	app_ver	varchar2(10)	not null,
	reg_date	timestamp	not null,
	seen_date	timestamp	not null
,constraint crs_mobile_device_p primary key (device_id)
,constraint crs_user_device_fk foreign key (user_id) references crs_user (user_id));





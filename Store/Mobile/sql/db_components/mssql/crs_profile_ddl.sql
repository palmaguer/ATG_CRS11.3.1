


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/crs_profile_ddl.xml#1 $$Change: 1497274 $

create table crs_mobile_device (
	device_id	varchar(40)	not null,
	unique_id	varchar(255)	not null,
	push_token	varchar(255)	null,
	user_id	varchar(40)	null,
	os	integer	not null,
	os_ver	varchar(40)	not null,
	app	integer	not null,
	app_ver	varchar(10)	not null,
	reg_date	datetime	not null,
	seen_date	datetime	not null
,constraint crs_mobile_device_p primary key (device_id)
,constraint crs_user_device_fk foreign key (user_id) references crs_user (user_id))



go

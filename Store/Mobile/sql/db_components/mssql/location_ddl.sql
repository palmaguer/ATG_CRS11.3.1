


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/location_ddl.xml#1 $$Change: 1497274 $

create table crs_ibeacon_alert (
	alert_id	varchar(40)	not null,
	name	varchar(254)	null,
	alert_text	varchar(160)	null,
	alert_key	varchar(254)	null,
	link_url	varchar(254)	null,
	is_push	numeric(1,0)	null
,constraint crsbeaconalert_pk primary key (alert_id))


create table crs_ibeacon (
	ibeacon_id	varchar(40)	not null,
	name	varchar(254)	null,
	uuid	varchar(254)	not null,
	major	varchar(254)	null,
	minor	varchar(254)	null,
	identifier	varchar(254)	null
,constraint crsbeacon_pk primary key (ibeacon_id))


create table crs_ibcn_stores (
	ibeacon_id	varchar(40)	not null,
	location_id	varchar(40)	not null
,constraint crsibcnstores_pk primary key (ibeacon_id,location_id)
,constraint crsibcnstores_fk1 foreign key (ibeacon_id) references crs_ibeacon (ibeacon_id)
,constraint crsibcnstores_fk2 foreign key (location_id) references dcs_location_store (location_id))


create table crs_ibcnalrt_entr (
	ibeacon_id	varchar(40)	not null,
	alert_id	varchar(40)	not null,
	sequence_num	integer	not null
,constraint crsibcnalrtentr_p primary key (ibeacon_id,sequence_num)
,constraint crsibcnalrtentr_f1 foreign key (ibeacon_id) references crs_ibeacon (ibeacon_id)
,constraint crsibcnalrtentr_f2 foreign key (alert_id) references crs_ibeacon_alert (alert_id))


create table crs_ibcnalrt_exit (
	ibeacon_id	varchar(40)	not null,
	alert_id	varchar(40)	not null,
	sequence_num	integer	not null
,constraint crsibcnalrtexit_p primary key (ibeacon_id,sequence_num)
,constraint crsibcnalrtexit_f1 foreign key (ibeacon_id) references crs_ibeacon (ibeacon_id)
,constraint crsibcnalrtexit_f2 foreign key (alert_id) references crs_ibeacon_alert (alert_id))



go

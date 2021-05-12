


--  @version $Id: //hosting-blueprint/B2CBlueprint/version/11.3.1/Mobile/sql/ddlgen/location_i18n_ddl.xml#1 $$Change: 1497274 $

create table crs_ibcnalrt_xlate (
	translation_id	varchar(40)	not null,
	alert_text	varchar(254)	null
,constraint crsibcnalrtxlate_p primary key (translation_id))


create table crs_alrt_alrt_xlate (
	alert_id	varchar(40)	not null,
	locale	varchar(40)	not null,
	translation_id	varchar(40)	not null
,constraint crsalrtalrtxlt_p primary key (alert_id,locale)
,constraint crsalrtalrtxlt_f foreign key (translation_id) references crs_ibcnalrt_xlate (translation_id))

create index crsalrtalrtxlt_ix on crs_alrt_alrt_xlate (translation_id)


go

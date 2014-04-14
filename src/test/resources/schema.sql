create table ID_GENERATOR (
  ENTITY_NAME varchar(50),
  ID_VALUE int,
  primary key(ENTITY_NAME)
);

create table JOB (
	JOB_ID              INT IDENTITY,
	STATE               varchar(20),
	SOURCE_URL          varchar(100),
	DESTINATION_URL     varchar(100),
	CALLBACK_URL        varchar(100),
	EXCEPTION_MESSAGE   varchar(255),
	primary key (JOB_ID)
);

create table JOB_OUTPUTFORMAT (
  JOB_ID      INT,
  LIST_IDX    INT,
  WIDTH       INT,
  HEIGHT      INT,
  BITRATE     INT,
  CONTAINER     varchar(20),
  VIDEO_CODEC   varchar(20),
  AUDIO_CODEC   varchar(20)
);

create index JOB_OUTPUTFORMAT_IDX on JOB_OUTPUTFORMAT (JOB_ID, LIST_IDX);
create table JOB (
	JOB_ID INT IDENTITY,
	STATE varchar(20),
	SOURCE_URL varchar(100),
	DESTINATION_URL varchar(100),
	CALLBACK_URL varchar(100),
	EXCEPTION_MESSAGE varchar(255)
);
CREATE TABLE profile(
	userID varchar2(20) PRIMARY KEY,
	name varchar2(50),
	password varchar2(50),
	date_of_birth date,
	lastlogin timestamp
);

CREATE TABLE friends(
	userID1 varchar2(20),
	userID2 varchar2(20),
	JDate date,
	message varchar2(200),
	CONSTRAINT f_pk primary key(userID1, userID2)
);

CREATE TABLE pendingFriends(
	fromID varchar2(20)
	toID varchar2(20)
	message varchar2(200)
);


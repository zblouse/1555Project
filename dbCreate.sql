CREATE TABLE profile(
	userID varchar2(20),
	name varchar2(50),
	password varchar2(50),
	date_of_birth date,
	lastlogin timestamp,
	CONSTRAINT p_pk PRIMARY KEY userID
);

CREATE TABLE friends(
	userID1 varchar2(20),
	userID2 varchar2(20),
	JDate date,
	message varchar2(200),
	CONSTRAINT f_pk primary key(userID1, userID2),
	CONSTRAINT F_FK1 FOREIGN KEY(userID1) REFERENCES profile(userID),
	CONSTRAINT f_fk2 FOREIGN KEY (userID2) REFERENCES profile(userID)
);

CREATE TABLE pendingFriends(
	fromID varchar2(20)
	toID varchar2(20)
	message varchar2(200)
	CONSTRAINT pf_pk PRIMARY KEY(fromID, toID),
	CONSTRAINT pf_fk1 FOREIGN KEY(fromID) REFERENCES profile(userID),
	CONSTRAINT pf_fk2 FOREIGN KEY(toID) REFERENCES profile(userID)
);

CREATE TABLE messages(
	msgID varchar2(20),
	fromID varchar2(20),
	message varchar2(20),
	toUserID varchar2(20),
	toGroupID varchar2(20),
	dateSent date,
	CONSTRAINT m_pk PRIMARY KEY(msgID),
	CONSTRAINT m_fk1 FOREIGN KEY(fromID) REFERENCES profile(userID),
	CONSTRAINT m_fk2 FOREIGN KEY(toUserID) REFERENCES profile(userID),
	CONSTRAINT m_fk2 FOREIGN KEY(toGroupID) REFERENCES groups(gID)
);

CREATE TABLE messageRecipient(
	msgID varchar2(20),
	userID varchar2(20),
	CONSTRAINT mr_pk PRIMARY KEY(msgID),
	CONSTRAINT mr_fk FOREIGN KEY(userID) REFERENCES profile(userID)	
);
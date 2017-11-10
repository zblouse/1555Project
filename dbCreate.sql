CREATE TABLE profile(
	userID varchar2(20),
	name varchar2(50) NOT NULL,
	password varchar2(50) NOT NULL,
	date_of_birth date,
	lastlogin timestamp,
	CONSTRAINT profile_pk PRIMARY KEY userID
);

CREATE TABLE friends(
	userID1 varchar2(20),
	userID2 varchar2(20),
	JDate date,
	message varchar2(200),
	CONSTRAINT friends_pk PRIMARY KEY(userID1, userID2),
	CONSTRAINT users_fk1 FOREIGN KEY(userID1) REFERENCES profile(userID),
	CONSTRAINT users_fk2 FOREIGN KEY (userID2) REFERENCES profile(userID)
);

CREATE TABLE pendingFriends(
	fromID varchar2(20),
	toID varchar2(20),
	message varchar2(200),
	CONSTRAINT pendingfrineds_pk PRIMARY KEY(fromID, toID),
	CONSTRAINT profile_fk1 FOREIGN KEY(fromID) REFERENCES profile(userID),
	CONSTRAINT profile_fk2 FOREIGN KEY(toID) REFERENCES profile(userID)
);

--need to declare either to userid or group id to not null
CREATE TABLE messages(
	msgID varchar2(20),
	fromID varchar2(20) NOT NULL,
	message varchar2(20),
	toUserID varchar2(20),
	toGroupID varchar2(20),
	dateSent date,
	CONSTRAINT messages_pk PRIMARY KEY(msgID),
	CONSTRAINT profile_fk1 FOREIGN KEY(fromID) REFERENCES profile(userID),
	CONSTRAINT profile_fk2 FOREIGN KEY(toUserID) REFERENCES profile(userID),
	CONSTRAINT groups_fk FOREIGN KEY(toGroupID) REFERENCES groups(gID)
);

CREATE TABLE messageRecipient(
	msgID varchar2(20),
	userID varchar2(20) NOT NULL,
	CONSTRAINT messagerecipient_pk PRIMARY KEY(msgID),
	CONSTRAINT profile_fk FOREIGN KEY(userID) REFERENCES profile(userID)	
);
CREATE TABLE groups(
	gID varchar2(20),
	name varchar2(50),
	description varchar2(200),
	CONSTRAINT groups_pk PRIMARY KEY (gID)
);
CREATE TABLE groupMembership(
	gID varchar2(20),
	userID varchar2(20),
	role varchar2(20) NOt NULL,
	CONSTRAINT groupMembership_pk PRIMARY KEY (gID,userID),
	CONSTRAINT groupMembershipToGroup_fk FOREIGN KEY (gID) REFERENCES groups(gID),
	CONSTRAINT groupMembershipToProfile_fk FOREIGN KEY (userID) REFERENCES profiles(userID)
);
CREATE TABLE pendingGroupmembers(
	gID varchar2(20),
	userID varchar2(20),
	message varchar2(20),
	CONSTRAINT pendingGroupmembers_pk PRIMARY KEY (gID,userID)
	CONSTRAINT group_fk FOREIGN KEY (userID) REFERENCES groups(gID)
	CONSTRAINT user_fk FOREIGN KEY (gID) REFERENCES profile(userID) 
);
--ASSUMPTIONS
--message attributes are just strings within those shcemas and do not exist within the messages table
--e.g. the message attribute in friends and pendingfriends are not in messages

DROP TABLE profile cascade constraints;
DROP TABLE friends cascade constraints;
DROP TABLE pendingFriends cascade constraints;
DROP TABLE messages cascade constraints;
DROP TABLE messageRecipient cascade constraints;
DROP TABLE groups cascade constraints;
DROP TABLE groupMembership cascade constraints;
DROP TABLE pendingGroupmembers cascade constraints;



CREATE TABLE profile(
	userID varchar2(20),
	name varchar2(50) NOT NULL,
	password varchar2(50) NOT NULL,
	date_of_birth date,
	lastlogin timestamp,
	CONSTRAINT profile_pk PRIMARY KEY(userID)
);

CREATE TABLE friends(
	userID1 varchar2(20),
	userID2 varchar2(20),
	JDate date,
	message varchar2(200),
	CONSTRAINT friends_pk PRIMARY KEY(userID1, userID2),
	CONSTRAINT friends_fk1 FOREIGN KEY(userID1) REFERENCES profile(userID),
	CONSTRAINT friends_fk2 FOREIGN KEY(userID2) REFERENCES profile(userID)
);

CREATE TABLE groups(
	gID varchar2(20),
	name varchar2(50),
	description varchar2(200),
	memberLimit integer, 
	CONSTRAINT groups_pk PRIMARY KEY (gID)
);

CREATE TABLE pendingFriends(
	fromID varchar2(20),
	toID varchar2(20),
	message varchar2(200),
	CONSTRAINT pendingfrineds_pk PRIMARY KEY(fromID, toID),
	CONSTRAINT pendingFriends_fk1 FOREIGN KEY(fromID) REFERENCES profile(userID),
	CONSTRAINT pendingFriends_fk2 FOREIGN KEY(toID) REFERENCES profile(userID)
);

--need to declare either to userid or group id to not null
CREATE TABLE messages(
	msgID varchar2(20),
	fromID varchar2(20) NOT NULL,
	message varchar2(200),
	toUserID varchar2(20) DEFAULT NULL,
	toGroupID varchar2(20) DEFAULT NULL,
	dateSent date,
	CONSTRAINT messages_pk PRIMARY KEY(msgID),
	CONSTRAINT messages_fk1 FOREIGN KEY(fromID) REFERENCES profile(userID),
	CONSTRAINT messages_fk2 FOREIGN KEY(toUserID) REFERENCES profile(userID),
	CONSTRAINT groups_fk FOREIGN KEY(toGroupID) REFERENCES groups(gID)
);

CREATE TABLE messageRecipient(
	msgID varchar2(20),
	userID varchar2(20) NOT NULL,
	CONSTRAINT messagerecipient_pk PRIMARY KEY(msgID),
	CONSTRAINT profile_fk FOREIGN KEY(userID) REFERENCES profile(userID)	
);

CREATE TABLE groupMembership(
	gID varchar2(20),
	userID varchar2(20),
	role varchar2(20) NOt NULL,
	CONSTRAINT groupMembership_pk PRIMARY KEY (gID,userID),
	CONSTRAINT groupMembershipToGroup_fk FOREIGN KEY (gID) REFERENCES groups(gID),
	CONSTRAINT groupMembershipToProfile_fk FOREIGN KEY (userID) REFERENCES profile(userID)
);
CREATE TABLE pendingGroupmembers(
	gID varchar2(20),
	userID varchar2(20),
	message varchar2(20),
	CONSTRAINT pendingGroupmembers_pk PRIMARY KEY (gID,userID),
	CONSTRAINT group_fk FOREIGN KEY(gID) REFERENCES groups(gID),
	CONSTRAINT user_fk FOREIGN KEY(userID) REFERENCES profile(userID)
);
CREATE OR REPLACE TRIGGER Remove_From_Pending_Friends
  BEFORE INSERT ON friends
  FOR EACH ROW
	BEGIN
		DELETE FROM pendingFriends WHERE fromID = :new.userID1 AND toID = :new.userID2;
	END;
/
CREATE OR REPLACE TRIGGER Remove_From_Pending_Groups
  BEFORE INSERT ON groupMembership
  FOR EACH ROW
	BEGIN
		DELETE FROM pendingGroupmembers WHERE gID = :new.gID AND userID = :new.userID;
	END;
/
CREATE OR REPLACE TRIGGER Remove_User
  BEFORE DELETE ON profile
  REFERENCING OLD AS oldprof
  FOR EACH ROW
	BEGIN
		DELETE FROM friends WHERE userID1 = :oldprof.userID OR userID2 = :oldprof.userID;
		DELETE FROM pendingFriends WHERE fromID = :oldprof.userID OR toID = :oldprof.userID;
		DELETE FROM messages WHERE fromID = :oldprof.userID;
		DELETE FROM messages WHERE toUserID = :oldprof.userID;
		DELETE FROM messageRecipient WHERE userID = :oldprof.userID;
		DELETE FROM pendingGroupmembers WHERE userID = :oldprof.userID;
		DELETE FROM groupMembership WHERE userID = :oldprof.userID;
	END;
/
CREATE OR REPLACE TRIGGER Message_Group
	BEFORE INSERT ON messages
	FOR EACH ROW
	WHEN(new.togroupID IS NOT NULL)
		BEGIN
			INSERT INTO messageRecipient Values(:new.msgID, (SELECT userID FROM groupmembership WHERE gID = :new.toGroupID));
		END;
/
CREATE OR REPLACE TRIGGER Message_Recipient_Populator
	BEFORE INSERT ON messages
	FOR EACH ROW
	WHEN(new.toUserID IS NOT NULL)
		BEGIN
			INSERT INTO messageRecipient Values(:new.msgID, :new.toUserID);
		END;
/
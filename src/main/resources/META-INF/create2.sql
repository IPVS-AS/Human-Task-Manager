-- -----------------------------------------------------
-- Table HumanTaskModel
-- -----------------------------------------------------
CREATE TABLE humantaskmodel (
  ID INTEGER NOT NULL, ACTIVATIONTIME LONGBLOB,
  COMPLETEBY LONGBLOB,
  DURATIONAVG LONGBLOB,
  DURATIONMAX LONGBLOB,
  DURATIONMIN LONGBLOB,
  FAULTSCHEMA LONGBLOB,
  INPUTSCHEMA LONGBLOB,
  NAME VARCHAR(100),
  OUTPUTSCHEMA LONGBLOB,
  POSITIONX LONGBLOB,
  POSITIONY LONGBLOB,
  PRIORITY LONGBLOB,
  QUERYPROPERTY1 LONGBLOB,
  QUERYPROPERTY1NAME VARCHAR(100),
  QUERYPROPERTY2 LONGBLOB,
  QUERYPROPERTY2NAME VARCHAR(100),
  QUERYPROPERTY3 LONGBLOB,
  QUERYPROPERTY3NAME VARCHAR(100),
  QUERYPROPERTY4 LONGBLOB,
  QUERYPROPERTY4NAME VARCHAR(100),
  SKIPABLE LONGBLOB,
  STARTBY LONGBLOB,
  PRIMARY KEY (ID),
  CONSTRAINT unique_HumanTaskModel_Name UNIQUE (name));


-- -----------------------------------------------------
-- Table "LogicalPeopleGroupDef"
-- -----------------------------------------------------

CREATE TABLE LOGICALPEOPLEGROUPDEF (
  ID INTEGER NOT NULL,
  NAME VARCHAR(100),
  PRIMARY KEY (ID),
  CONSTRAINT unique_LogicalPeopleGroupDef_Name UNIQUE (name));


-- -----------------------------------------------------
-- Table LPG_ArgumentDef
-- -----------------------------------------------------

CREATE TABLE LPG_ARGUMENTDEF (
  ID INTEGER NOT NULL,
  NAME VARCHAR(100),
  LPG INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_LPG
  FOREIGN KEY (lpg )
  REFERENCES LogicalPeopleGroupDef (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT unique_LPG_ArgumentDef_Name UNIQUE (name, lpg));

-- -----------------------------------------------------
-- Table "Literal"
-- -----------------------------------------------------

CREATE TABLE LITERAL (ID INTEGER NOT NULL,
  ENTITYIDENTIFIER VARCHAR(100),
  HUMANROLE VARCHAR(100),
  HUMANTASKMODEL_ID INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_Literal_HumanTaskModel
  FOREIGN KEY (HumanTaskModel_id)
  REFERENCES HumanTaskModel (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table PeopleQuery
-- -----------------------------------------------------

CREATE TABLE PEOPLEQUERY (
  ID INTEGER NOT NULL,
  HUMANROLE VARCHAR(100),
  HUMANTASKMODEL_ID INTEGER,
  LPG INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_PeopleQuery
  FOREIGN KEY (lpg )
  REFERENCES LogicalPeopleGroupDef (id )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  CONSTRAINT fk_PeopleQuery_HumanTaskModel
  FOREIGN KEY (HumanTaskModel_id)
  REFERENCES HumanTaskModel (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table PeopleQueryArgument
-- -----------------------------------------------------

CREATE TABLE PEOPLEQUERYARGUMENT (ID INTEGER NOT NULL,
  EXPRESSION LONGBLOB,
  LPGARGUMENT INTEGER, PEOPLEQUERY INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_PeopleQuery_ID
  FOREIGN KEY (peopleQuery )
  REFERENCES PeopleQuery (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT fk_LPG_ArgumentDef
  FOREIGN KEY (lpgArgument )
  REFERENCES LPG_ArgumentDef (id )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table PresentationInformation
-- -----------------------------------------------------

CREATE TABLE PRESENTATIONINFORMATION (
  ID INTEGER NOT NULL,
  DESCRIPTION LONGBLOB,
  SUBJECT VARCHAR(100), TITLE VARCHAR(100),
  HUMANTASKMODEL_ID INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_PresentationInfo_HumanTaskModel
  FOREIGN KEY (HumanTaskModel_id )
  REFERENCES HumanTaskModel (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION);




-- -----------------------------------------------------
-- Table HumanTaskInstance
-- -----------------------------------------------------

CREATE TABLE HUMANTASKINSTANCE (
  ID INTEGER NOT NULL,
  ACTIVATIONTIME TIMESTAMP,
  COMPLETEBY TIMESTAMP,
  CONTEXTID VARCHAR(100),
  CREATEDON TIMESTAMP,
  DURATIONAVG BIGINT,
  DURATIONMAX BIGINT,
  DURATIONMIN BIGINT,
  EXPIRATIONTIME TIMESTAMP,
  FAULTDATA LONGBLOB,
  FAULTNAME VARCHAR(100),
  INPUTDATA LONGBLOB,
  NAME VARCHAR(100),
  OUTPUTDATA LONGBLOB,
  POSITIONX DOUBLE,
  POSITIONY DOUBLE,
  PRESENTATIONDESCRIPTION LONGBLOB,
  PRESENTATIONNAME VARCHAR(100),
  PRESENTATIONSUBJECT VARCHAR(100),
  PRIORITY INTEGER,
  QUERYPROPERTY1 VARCHAR(100),
  QUERYPROPERTY1NAME VARCHAR(100),
  QUERYPROPERTY2 VARCHAR(100),
  QUERYPROPERTY2NAME VARCHAR(100),
  QUERYPROPERTY3 DOUBLE,
  QUERYPROPERTY3NAME VARCHAR(100),
  QUERYPROPERTY4 DOUBLE,
  QUERYPROPERTY4NAME VARCHAR(100),
  SKIPABLE SMALLINT,
  STARTBY TIMESTAMP,
  STATUS VARCHAR(100),
  SUSPENDUNTIL TIMESTAMP,
  TASKPARENTID VARCHAR(100),
  MODELID INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_modelId
  FOREIGN KEY (modelId )
  REFERENCES HumanTaskModel (id )
  ON DELETE SET NULL
  ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table AssignedUser
-- -----------------------------------------------------

CREATE TABLE ASSIGNEDUSER (
  ID INTEGER NOT NULL,
  USERID VARCHAR(100) UNIQUE,
  PRIMARY KEY (ID),
  CONSTRAINT unique_AssignedUserId UNIQUE (userid));


-- -----------------------------------------------------
-- Table Attachment
-- -----------------------------------------------------

CREATE TABLE ATTACHMENT (
  ID INTEGER NOT NULL,
  ACCESSTYPE VARCHAR(100),
  ATTACHEDAT TIMESTAMP,
  CONTENTTYPE VARCHAR(100),
  NAME VARCHAR(100),
  VALUE LONGBLOB,
  TIID INTEGER,
  ATTACHEDBY INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_Attachment_HumanTaskInstance
  FOREIGN KEY (tiid)
  REFERENCES HumanTaskInstance (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT fk_Attachment_Creator
  FOREIGN KEY (attachedBy)
  REFERENCES AssignedUser (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  CONSTRAINT unique_Attachment_Name UNIQUE (name, tiid));



-- -----------------------------------------------------
-- Table CallbackCorrelationProperty
-- -----------------------------------------------------

CREATE TABLE CALLBACKCORRELATIONPROPERTY (
  ID INTEGER NOT NULL,
  NAME VARCHAR(100),
  VALUE LONGBLOB,
  TIID INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_CallbackCorrelation_HumanTaskInstance
  FOREIGN KEY (tiid )
  REFERENCES HumanTaskInstance (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table WorkItem
-- -----------------------------------------------------

CREATE TABLE WORKITEM (
  ID INTEGER NOT NULL,
  CLAIMED SMALLINT,
  CREATIONTIME TIMESTAMP,
  EVERYBODY SMALLINT,
  GENERICHUMANROLE VARCHAR(100),
  TIID INTEGER,
  ASSIGNEE INTEGER,
  PRIMARY KEY (ID),
  CONSTRAINT fk_AssignedUser
  FOREIGN KEY (assignee )
  REFERENCES AssignedUser (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT fk_AssignedTask
  FOREIGN KEY (tiid )
  REFERENCES HumanTaskInstance (id )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table Users
-- -----------------------------------------------------

CREATE TABLE USERS (
  ID INTEGER NOT NULL,
  FIRSTNAME VARCHAR(100),
  LASTNAME VARCHAR(100),
  USERID VARCHAR(100),
  PRIMARY KEY (ID),
  CONSTRAINT unique_User_userid UNIQUE (userid));

-- -----------------------------------------------------
-- Table Groups
-- -----------------------------------------------------

CREATE TABLE ROLES (
  ID        INTEGER NOT NULL,
  GROUPNAME VARCHAR(100),
  PRIMARY KEY (ID),
  CONSTRAINT unique_Group_groupname UNIQUE (GROUPNAME));

-- -----------------------------------------------------
-- Table Users_has_Groups
-- -----------------------------------------------------

CREATE TABLE USERSGROUPS (
  GROUP_ID INTEGER NOT NULL,
  USER_ID INTEGER NOT NULL,
  PRIMARY KEY (GROUP_ID, USER_ID),
  CONSTRAINT fk_User_has_Group
  FOREIGN KEY (User_id)
  REFERENCES Users(id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT fk_Group_has_User
  FOREIGN KEY (Group_id)
  REFERENCES Roles (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- TABLE SEQUENCE : Helper table required for JPA to manage keys
-- -----------------------------------------------------
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50), SEQ_COUNT DECIMAL(15));
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);


-- -----------------------------------------------------
-- VIEW WorkItemTaskView
-- -----------------------------------------------------

create view WorkItemTaskView(
	tiid, wiid, createdOn, assignee, assignedToEverybody, genericHumanRole, isClaimed,
	taskInstanceName, taskModelId, status, priority, isSkipable, taskCreatedOn, activatedOn, expiresOn, startBy, completeBy,
	suspendUntil, presentationName, presentationSubject, presentationDescription,
	inputData, outputData, faultName, faultData)
as select ti.id, wi.id, wi.creationTime, au.userId, wi.everybody, wi.genericHumanRole, wi.claimed, ti.name, ti.modelId, ti.status, ti.priority, ti.skipable, ti.createdOn, ti.activationTime, ti.expirationTime, ti.startBy,ti.completeBy,
ti.suspendUntil, ti.presentationName, ti.presentationSubject, ti.presentationDescription, ti.inputData, ti.outputData, ti.faultName, ti.faultData
from HumanTaskInstance ti, WorkItem wi, Assigneduser au where ti.id = wi.tiid and au.id = wi.assignee;

-- -----------------------------------------------------
-- TABLE STRUCTUREDATA
-- -----------------------------------------------------

CREATE TABLE STRUCTUREDATA (
  ID INTEGER NOT NULL,
  HASCONTROLLEDTASKS BOOLEAN,
  HASSUBTASKS BOOLEAN,
  LOCKCOUNTER INTEGER,
  NAME VARCHAR(100),
  STRUCTURENR INTEGER,
  STRUCTURE_ID INTEGER,
  SUSPENDCOUNTER INTEGER,
  TASK_ID INTEGER,
  MERGETASK_ID INTEGER,
  PARENTTASK_ID INTEGER,
  PRIMARY KEY (ID));
ALTER TABLE STRUCTUREDATA ADD CONSTRAINT FK_STRUCTUREDATA_PARENTTASK_ID FOREIGN KEY (PARENTTASK_ID) REFERENCES STRUCTUREDATA (ID);
ALTER TABLE STRUCTUREDATA ADD CONSTRAINT FK_STRUCTUREDATA_MERGETASK_ID FOREIGN KEY (MERGETASK_ID) REFERENCES STRUCTUREDATA (ID);

-- -----------------------------------------------------
-- TABLE Inputparameter
-- -----------------------------------------------------

CREATE TABLE INPUTPARAMETER (
  ID INTEGER  NOT NULL,
  TIID INTEGER,
  LABEL VARCHAR(100),
  VALUE VARCHAR(100),
  PRIMARY KEY (ID),
  CONSTRAINT fk_inputparameter_tiid
  FOREIGN KEY (tiid )
  REFERENCES HumanTaskInstance(id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION );

-- -----------------------------------------------------
-- TABLE Outputparameter
-- -----------------------------------------------------
CREATE TABLE OUTPUTPARAMETER (
  ID INTEGER  NOT NULL,
  TIID INTEGER,
  LABEL VARCHAR(100),
  VALUE VARCHAR(100),
  PRIMARY KEY (ID),
  CONSTRAINT fk_outputparameter_tiid
  FOREIGN KEY (tiid )
  REFERENCES HumanTaskInstance(id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION );

-- -----------------------------------------------------
-- TABLE TaskType
-- -----------------------------------------------------
CREATE TABLE TASKTYPE (
  ID INTEGER NOT NULL,
  TASKTYPENAME VARCHAR(100),
  PRIMARY KEY (ID),
  CONSTRAINT unique_TaskType_TaskTypename UNIQUE (TASKTYPENAME));

-- -----------------------------------------------------
-- TABLE TaskType_has_Groups
-- -----------------------------------------------------

CREATE TABLE TASKTYPEGROUPS (
  GROUP_ID INTEGER NOT NULL,
  TASKTYPE_ID INTEGER NOT NULL,
  PRIMARY KEY (GROUP_ID, TASKTYPE_ID),
  CONSTRAINT fk_TaskType_has_Group
  FOREIGN KEY (TASKTYPE_ID)
  REFERENCES TaskType(id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT fk_Group_has_TaskType
  FOREIGN KEY (Group_id)
  REFERENCES Roles (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- TABLE TaskType_has_Groups
-- -----------------------------------------------------

CREATE TABLE TASKTYPETASKS (
  TASK_ID INTEGER NOT NULL,
  TASKTYPE_ID INTEGER NOT NULL,
  PRIMARY KEY (TASK_ID, TASKTYPE_ID),
  CONSTRAINT fk_TaskType_has_Task
  FOREIGN KEY (TASKTYPE_ID)
  REFERENCES TaskType(id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
  CONSTRAINT fk_Task_has_TaskType
  FOREIGN KEY (TASK_ID)
  REFERENCES HUMANTASKINSTANCE (id )
  ON DELETE CASCADE
  ON UPDATE NO ACTION);
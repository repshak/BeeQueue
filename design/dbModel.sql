CREATE TABLE NN_HOST (
       HOST_CD VARCHAR(64) NOT NULL
     , HOST_STATE VARCHAR(32)
     , HOST_IP VARCHAR(64)
     , HOST_FQDN VARCHAR(128)
     , CLOUD_CD VARCHAR(32) NOT NULL
     , CONSTRAINT PK_NN_HOST PRIMARY KEY (HOST_CD)
);

CREATE TABLE NN_USER (
       USER_CD VARCHAR(32) NOT NULL
     , AUTH_TOKEN VARCHAR(128)
     , USER_SETTINGS VARCHAR(1024)
     , USER_TYPE VARCHAR(32)
     , CONSTRAINT PK_NN_USER PRIMARY KEY (USER_CD)
);

CREATE TABLE NN_SHA_STORAGE (
       SHA_ID CHAR(41) NOT NULL
     , DATA BLOB(100 M)
     , SWEEPT_ON TIMESTAMP
     , CONSTRAINT PK_NN_SHA_STORAGE PRIMARY KEY (SHA_ID)
);

CREATE TABLE NN_ID_FACTORY (
       TABLE_NAME VARCHAR(64) NOT NULL
     , COUNTER BIGINT
     , CONSTRAINT PK_NN_ID_FACTORY PRIMARY KEY (TABLE_NAME)
);

CREATE TABLE NN_MESSAGE_LATCH (
       MSG_KIND VARCHAR(1000) NOT NULL
     , MSG_LOCATOR VARCHAR(1000) NOT NULL
     , LOCK_TS TIMESTAMP
     , CONSTRAINT PK_NN_MESSAGE_LATCH PRIMARY KEY (MSG_KIND)
);

CREATE TABLE NN_DOMAIN (
       DOMAIN_CD VARCHAR(32) NOT NULL
     , DOMAIN_STATE VARCHAR(32) NOT NULL
     , CONSTRAINT PK_NN_DOMAIN PRIMARY KEY (DOMAIN_CD)
);

CREATE TABLE NN_WORKER (
       WORKER_ID BIGINT NOT NULL
     , HOST_CD VARCHAR(64) NOT NULL
     , PID VARCHAR(32)
     , WORKER_STATE VARCHAR(32)
     , NEXT_BEAT BIGINT
     , CONSTRAINT PK_NN_WORKER PRIMARY KEY (WORKER_ID)
);

CREATE TABLE NN_MESSAGE (
       MSG_ID BIGINT NOT NULL
     , MSG_NAME VARCHAR(32) NOT NULL
     , MSG_STATE VARCHAR(32) NOT NULL
     , MSG_KIND VARCHAR(1000) NOT NULL
     , MSG_LOCATOR VARCHAR(1000) NOT NULL
     , MSG_INFO VARCHAR(4000)
     , USER_CD VARCHAR(32)
     , DOMAIN_CD VARCHAR(32) NOT NULL
     , LOCK_TS TIMESTAMP WITH DEFAULT CURRENT_TIMESTAMP
     , CONSTRAINT PK_NN_MESSAGE PRIMARY KEY (MSG_ID)
);

CREATE TABLE NN_JOB (
       JOB_ID BIGINT NOT NULL
     , MSG_ID BIGINT NOT NULL
     , JOB_STATE VARCHAR(32)
     , RESPONSIBLE CHAR(1) NOT NULL
     , JOB_NAME VARCHAR(32) NOT NULL
     , CONSTRAINT PK_NN_JOB PRIMARY KEY (JOB_ID)
);

CREATE TABLE NN_STAGE (
       STAGE_ID BIGINT NOT NULL
     , JOB_ID BIGINT NOT NULL
     , STAGE_STATE VARCHAR(32)
     , RETRIES_LEFT BIGINT
     , STAGE_NAME VARCHAR(32)
     , CONSTRAINT PK_NN_STAGE PRIMARY KEY (STAGE_ID)
);

CREATE TABLE NN_RUN (
       RUN_ID BIGINT NOT NULL
     , STAGE_ID BIGINT
     , WORKER_ID BIGINT NOT NULL
     , RUN_STATE VARCHAR(32)
     , PID VARCHAR(32)
     , PS_NAME VARCHAR(256)
     , RUN_DIR VARCHAR(256)
     , RUN_CMD VARCHAR(4000)
     , START_TS TIMESTAMP NOT NULL
     , UP_TS TIMESTAMP
     , DOWN_TS TIMESTAMP
     , CONSTRAINT PK_NN_RUN PRIMARY KEY (RUN_ID)
);

CREATE TABLE NN_TREE (
       TREE_CD VARCHAR(32) NOT NULL
     , SHA_ID CHAR(41) NOT NULL
     , CONSTRAINT PK_NN_TREE PRIMARY KEY (TREE_CD)
);

CREATE TABLE NN_HOST_STATISTCS (
       HOST_CD VARCHAR(64) NOT NULL
     , DATA_COLLECTED TIMESTAMP NOT NULL
     , STAT_DATA CLOB(64 K) NOT NULL
     , CONSTRAINT PK_NN_HOST_STATISTCS PRIMARY KEY (HOST_CD, DATA_COLLECTED)
);

CREATE TABLE NN_TREE_HISTORY (
       TREE_CD VARCHAR(32) NOT NULL
     , UPDATED_TS TIMESTAMP NOT NULL
     , SHA_ID CHAR(41) NOT NULL
     , CONSTRAINT PK_NN_TREE_HISTORY PRIMARY KEY (TREE_CD, UPDATED_TS)
);

CREATE TABLE NN_PROCESS (
       RUN_ID BIGINT NOT NULL
     , PID VARCHAR(32) NOT NULL
     , PPID VARCHAR(32)
     , PS_NAME VARCHAR(256)
     , START_TS TIMESTAMP NOT NULL
     , UP_TS TIMESTAMP
     , DOWN_TS TIMESTAMP
     , CONSTRAINT PK_NN_PROCESS PRIMARY KEY (RUN_ID, PID)
);

CREATE TABLE NN_RUN_ARTIFACT (
       RUN_ID BIGINT NOT NULL
     , ARTIFACT_NAME VARCHAR(32) NOT NULL
     , TS TIMESTAMP NOT NULL
     , ARTIFACT_DATA VARCHAR(1024)
     , CONSTRAINT PK_NN_RUN_ARTIFACT PRIMARY KEY (TS, ARTIFACT_NAME, RUN_ID)
);

ALTER TABLE NN_WORKER
  ADD CONSTRAINT FK_NN_WORKER_1
      FOREIGN KEY (HOST_CD)
      REFERENCES NN_HOST (HOST_CD);

ALTER TABLE NN_MESSAGE
  ADD CONSTRAINT FK_NN_MESSAGE_3
      FOREIGN KEY (USER_CD)
      REFERENCES NN_USER (USER_CD);

ALTER TABLE NN_MESSAGE
  ADD CONSTRAINT FK_NN_MESSAGE_5
      FOREIGN KEY (DOMAIN_CD)
      REFERENCES NN_DOMAIN (DOMAIN_CD);

ALTER TABLE NN_MESSAGE
  ADD CONSTRAINT FK_NN_MESSAGE_4
      FOREIGN KEY (MSG_KIND)
      REFERENCES NN_MESSAGE_LATCH (MSG_KIND);

ALTER TABLE NN_JOB
  ADD CONSTRAINT FK_NN_JOB_1
      FOREIGN KEY (MSG_ID)
      REFERENCES NN_MESSAGE (MSG_ID);

ALTER TABLE NN_STAGE
  ADD CONSTRAINT FK_NN_STAGE_1
      FOREIGN KEY (JOB_ID)
      REFERENCES NN_JOB (JOB_ID);

ALTER TABLE NN_RUN
  ADD CONSTRAINT FK_NN_RUN_1
      FOREIGN KEY (STAGE_ID)
      REFERENCES NN_STAGE (STAGE_ID);

ALTER TABLE NN_RUN
  ADD CONSTRAINT FK_NN_RUN_2
      FOREIGN KEY (WORKER_ID)
      REFERENCES NN_WORKER (WORKER_ID);

ALTER TABLE NN_TREE
  ADD CONSTRAINT FK_NN_TREE_1
      FOREIGN KEY (SHA_ID)
      REFERENCES NN_SHA_STORAGE (SHA_ID);

ALTER TABLE NN_HOST_STATISTCS
  ADD CONSTRAINT FK_NN_HOST_STATISTCS_1
      FOREIGN KEY (HOST_CD)
      REFERENCES NN_HOST (HOST_CD);

ALTER TABLE NN_TREE_HISTORY
  ADD CONSTRAINT FK_NN_CONFIG_VERSION_1
      FOREIGN KEY (SHA_ID)
      REFERENCES NN_SHA_STORAGE (SHA_ID);

ALTER TABLE NN_TREE_HISTORY
  ADD CONSTRAINT FK_NN_CONFIG_VERSION_2
      FOREIGN KEY (TREE_CD)
      REFERENCES NN_TREE (TREE_CD);

ALTER TABLE NN_PROCESS
  ADD CONSTRAINT FK_NN_CHILD_RUN_1
      FOREIGN KEY (RUN_ID)
      REFERENCES NN_RUN (RUN_ID);

ALTER TABLE NN_RUN_ARTIFACT
  ADD CONSTRAINT FK_NN_RUN_ARTIFACT_1
      FOREIGN KEY (RUN_ID)
      REFERENCES NN_RUN (RUN_ID);


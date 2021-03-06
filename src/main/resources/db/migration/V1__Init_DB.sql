CREATE TABLE SIMBLOG_USER (
  ID BIGINT NOT NULL,
  USERNAME VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL,
  CONSTRAINT SIMBLOG_USER_PK PRIMARY KEY (ID),
  CONSTRAINT SIMBLOG_USER_USERNAME_UN UNIQUE (USERNAME)
);

CREATE SEQUENCE SIMBLOG_USER_ID_SEQ OWNED BY SIMBLOG_USER.ID;

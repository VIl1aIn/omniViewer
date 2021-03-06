-- Create table for dev

create schema if not exists alerts AUTHORIZATION sa;

create table if not exists alerts.status (
Acknowledged INT,
Serial INT primary key auto_increment (4),
Grade INT,
FirstOccurrence BIGINT,
LastOccurrence BIGINT,
StateChange BIGINT,
Identifier VARCHAR(255),
Agent VARCHAR(255),
Manager VARCHAR(255),
BSM_Identity VARCHAR(255),
BSM_SubIdentity VARCHAR(255),
AlertKey VARCHAR(255),
AlertGroup VARCHAR(255),
Node VARCHAR(255),
NodeAlias VARCHAR(255),
Summary VARCHAR(255),
ExtendedAttr VARCHAR(1024),
EventId VARCHAR(255),
ServerName VARCHAR(255),
URL VARCHAR(255),
Location VARCHAR(255),
Customer VARCHAR(255),
Service VARCHAR(255),
TECHostname VARCHAR(255),
Class INT,
Type INT,
Severity INT,
ExpireTime INT,
Tally INT,
LocalNodeAlias VARCHAR(255),
LocalPriObj VARCHAR(255),
LocalRootObj VARCHAR(255),
LocalSecObj VARCHAR(255),
ITMApplLabel VARCHAR(255),
RAD_ServiceName VARCHAR(255)
);

create table if not exists alerts.details (
KeyField VARCHAR(255),
Identifier VARCHAR(255),
AttrVal INT,
Sequence INT,
Name VARCHAR(255),
Detail VARCHAR(255)
);
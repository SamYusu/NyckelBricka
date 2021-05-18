

CREATE TABLE users (
id					INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
username			VARCHAR(100),
password			TEXT,
email				VARCHAR(100));

CREATE TABLE badges (
id					INT(11) NOT NULL,
lat					VARCHAR(100),
lon					VARCHAR(100),
date 				VARCHAR(100));

/* Notera att du kan sätta default värderna av `lat`, `lon` och `date` till NULL. */
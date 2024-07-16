CREATE TABLE `beacon` (
   `id` integer NOT NULL,
   PRIMARY KEY (`id`),
   CONSTRAINT FK_UUID FOREIGN KEY (id)
   REFERENCES `beacon_locations` (`location`)
);

CREATE TABLE `beacon_locations` (
  `location` string NOT NULL,
   PRIMARY KEY (`location`)
);
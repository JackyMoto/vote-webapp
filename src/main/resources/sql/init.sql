CREATE TABLE "vote_object" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "vname" varchar(20) DEFAULT NULL,
  "vdesc" varchar(50) DEFAULT NULL,
  "imgPic" varchar(30) DEFAULT NULL,
  "qrPic" varchar(30) DEFAULT NULL,
  "currentRank" int(5) DEFAULT NULL,
  "currentVote" int(20) DEFAULT NULL,
  "deleted" int(4) DEFAULT NULL,
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE "vote_record" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "voteId" int(11) DEFAULT NULL,
  "voteIP" varchar(20) DEFAULT NULL,
  "voteTime" timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;



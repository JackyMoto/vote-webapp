SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `vote_category`
-- ----------------------------
DROP TABLE IF EXISTS `vote_category`;
CREATE TABLE "vote_category" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "cname" varchar(20) DEFAULT NULL,
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vote_category
-- ----------------------------
INSERT INTO `vote_category` VALUES ('1', '十佳人气游戏奖');
INSERT INTO `vote_category` VALUES ('2', '十佳最受期待游戏奖');
INSERT INTO `vote_category` VALUES ('3', '十佳单机游戏奖');
INSERT INTO `vote_category` VALUES ('4', '十佳网络游戏奖');
INSERT INTO `vote_category` VALUES ('5', '十佳IP奖(十佳剧情)');

-- ----------------------------
-- Table structure for `vote_object`
-- ----------------------------
DROP TABLE IF EXISTS `vote_object`;
CREATE TABLE "vote_object" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "cid" int(11) DEFAULT NULL,
  "vname" varchar(20) DEFAULT NULL,
  "imgPic" varchar(30) DEFAULT NULL,
  "qrPic" varchar(30) DEFAULT NULL,
  "currentRank" int(5) DEFAULT NULL,
  "currentVote" int(20) DEFAULT NULL,
  "deleted" int(4) DEFAULT NULL,
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vote_object
-- ----------------------------
INSERT INTO `vote_object` VALUES ('1', '1', '3D坦克争霸', '3Dtkzb.png', '3Dtkzb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('2', '1', '新仙剑奇侠传', 'xxjqxz.png', 'xxjqxz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('3', '1', 'PopStar消灭星星', 'PopStarxmxx.png', 'PopStarxmxx.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('4', '1', '保卫萝卜', 'bwlb.png', 'bwlb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('5', '1', '保卫萝卜2', 'bwlb2.png', 'bwlb2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('6', '1', '博雅斗地主', 'byddz.png', 'byddz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('7', '1', '捕鱼达人3', 'bydr3.png', 'bydr3.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('8', '1', '刀塔传奇', 'dtcq.png', 'dtcq.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('9', '1', '地铁跑酷', 'dtpk.png', 'dtpk.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('10', '1', '割绳子2', 'gsz2.png', 'gsz2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('11', '1', '海岛奇兵', 'hdqb.png', 'hdqb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('12', '1', '滑雪大冒险', 'hxdmx.png', 'hxdmx.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('13', '1', '自由之战', 'zyzz.png', 'zyzz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('14', '1', '开心消消乐', 'kxxxl.png', 'kxxxl.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('15', '1', '乱斗西游', 'ldxy.png', 'ldxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('16', '1', '梦幻西游', 'mhxy.png', 'mhxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('17', '1', '去吧皮卡丘', 'qbpkq.png', 'qbpkq.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('18', '1', '全民枪战', 'qmqz.png', 'qmqz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('19', '1', '神庙逃亡2', 'smtw2.png', 'smtw2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('20', '1', '神偷奶爸：小黄人快跑', 'stnbxhrkp.png', 'stnbxhrkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('21', '1', '时空猎人', 'sklr.png', 'sklr.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('22', '1', '太极熊猫', 'tjxm.png', 'tjxm.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('23', '1', '天龙八部3D', 'tlbb3D.png', 'tlbb3D.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('24', '1', '天天炫舞', 'ttxw.png', 'ttxw.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('25', '1', '消灭星星2015', 'xmxx2015.png', 'xmxx2015.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('26', '1', '熊出没之熊大快跑', 'xcmzxdkp.png', 'xcmzxdkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('27', '1', '战舰帝国', 'zjdg.png', 'zjdg.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('28', '1', '九阴真经', 'jyzj.png', 'jyzj.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('29', '1', '饥饿鲨进化', 'jesjh.png', 'jesjh.png', '0', '0', '0');

INSERT INTO `vote_object` VALUES ('30', '2', '风云', 'fy.png', 'fy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('31', '2', '横行冒险王', 'hxmxw.png', 'hxmxw.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('32', '2', '金庸群侠传', 'jyqxz.png', 'jyqxz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('33', '2', '武极天下', 'wjtx.png', 'wjtx.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('34', '2', '雷霆海战', 'lthz.png', 'lthz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('35', '2', '魔力骑士团', 'mlqst.png', 'mlqst.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('36', '2', '神龙部落2', 'slbl2.png', 'slbl2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('37', '2', '神曲世界', 'sqsj.png', 'sqsj.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('38', '2', '太极熊猫2', 'tjxm2.png', 'tjxm2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('39', '2', '泰迪酷跑', 'tdkp.png', 'tdkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('40', '2', '心之城堡', 'xzcb.png', 'xzcb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('41', '2', '樱花三国', 'yhsg.png', 'yhsg.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('42', '2', '乱弹三国志', 'ltsgz.png', 'ltsgz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('43', '2', '芈月传', 'hyz.png', 'hyz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('44', '2', '蜀山战纪', 'sszj.png', 'sszj.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('45', '2', '冒险与挖矿', 'mxywk.png', 'mxywk.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('46', '2', '骑士法则', 'qsfz.png', 'qsfz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('47', '2', '仙境传奇', 'xjcq.png', 'xjcq.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('48', '2', '赛尔号-超级英雄', 'sehcjyx.png', 'sehcjyx.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('49', '2', '武林外传电影手游', 'wlwzdysy.png', 'wlwzdysy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('50', '2', '夏目的美丽日记', 'xmdmlrj.png', 'xmdmlrj.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('51', '2', '仙剑奇侠传五前传', 'xjqxzwqz.png', 'xjqxzwqz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('52', '2', '像三国', 'xsg.png', 'xsg.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('53', '2', '一代好功夫', 'ydhgf.png', 'ydhgf.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('54', '2', '大皇帝OL', 'dhdOL.png', 'dhdOL.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('55', '2', '航海王者强者之路', 'hhwzqzzl.png', 'hhwzqzzl.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('56', '2', '倚天屠龙记', 'yttlj.png', 'yttlj.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('57', '2', '鬼吹灯', 'gcd.png', 'gcd.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('58', '2', '星球大战指挥官', 'xqdzzhg.png', 'xqdzzhg.png', '0', '0', '0');

INSERT INTO `vote_object` VALUES ('59', '3', '3D暴力摩托-狂野飙车', '3Dblmtkybc.png', '3Dblmtkybc.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('60', '3', 'PopStar消灭星星', 'PopStarxmxx.png', 'PopStarxmxx.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('61', '3', '保卫萝卜', 'bwlb.png', 'bwlb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('62', '3', '消灭星星3', 'xmxx3.png', 'xmxx3.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('63', '3', '捕鱼达人3', 'bydr3.png', 'bydr3.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('64', '3', '地铁酷跑', 'dtkp.png', 'dtkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('65', '3', '滑雪大冒险', 'hxdmx.png', 'hxdmx.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('66', '3', '愤怒的小鸟2', 'fndxn2.png', 'fndxn2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('67', '3', '饥饿鲨进化', 'jesjh.png', 'jesjh.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('68', '3', '熊出没2', 'xcm2.png', 'xcm2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('69', '3', '开心消消乐', 'kxxxl.png', 'kxxxl.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('70', '3', '愤怒的小鸟泡泡大战', 'fndxnppdz.png', 'fndxnppdz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('71', '3', '猫和老鼠官方手游', 'mhlsgfsy.png', 'mhlsgfsy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('72', '3', '泡泡龙亚特兰蒂斯', 'pplytlds.png', 'pplytlds.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('73', '3', '神偷奶爸：小黄人快跑', 'stnbxhrkp.png', 'stnbxhrkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('74', '3', '喜羊羊快跑', 'xyykp.png', 'xyykp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('75', '3', '燃烧的蔬菜4新鲜战队', 'rsdsc4xxzd.png', 'rsdsc4xxzd.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('76', '3', '熊出没之熊大快跑', 'xcmzxdkp.png', 'xcmzxdkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('77', '3', '植物大战僵尸', 'zwdzjs.png', 'zwdzjs.png', '0', '0', '0');

INSERT INTO `vote_object` VALUES ('78', '4', '3D坦克争霸', '3Dtkzb.png', '3Dtkzb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('79', '4', 'JJ斗地主', 'JJddz.png', 'JJddz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('80', '4', '博雅斗地主', 'byddz.png', 'byddz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('81', '4', '刀塔传奇', 'dtcq.png', 'dtcq.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('82', '4', '崩坏学园', 'bhxy.png', 'bhxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('83', '4', '全民奇迹', 'qmqj.png', 'qmqj.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('84', '4', '海岛奇兵', 'hdqb.png', 'hdqb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('85', '4', '节奏大师', 'jzds.png', 'jzds.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('86', '4', '乱斗西游', 'ldxy.png', 'ldxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('87', '4', '梦幻神域', 'mhsy.png', 'mhsy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('88', '4', '红警4', 'hj4.png', 'hj4.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('89', '4', '全民枪战', 'qmqz.png', 'qmqz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('90', '4', '少年三国志', 'snsgz.png', 'snsgz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('91', '4', '时空猎人', 'sklr.png', 'sklr.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('92', '4', '太极熊猫', 'tjxm.png', 'tjxm.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('93', '4', '天龙八部3D', 'tlbb3D.png', 'tlbb3D.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('94', '4', '暗黑黎明', 'ahlm.png', 'ahlm.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('95', '4', '战舰帝国', 'zjdg.png', 'zjdg.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('96', '4', '梦幻西游', 'mhxy.png', 'mhxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('97', '4', '自由之战', 'zyzz.png', 'zyzz.png', '0', '0', '0');

INSERT INTO `vote_object` VALUES ('98', '5', '爸爸去哪儿', 'bbqne.png', 'bbqne.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('99', '5', '琅琊榜', 'lyb.png', 'lyb.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('100', '5', '刀塔传奇', 'dtcq.png', 'dtcq.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('101', '5', '果宝三国', 'gbsg.png', 'gbsg.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('102', '5', '花千骨', 'hqg.png', 'hqg.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('103', '5', '火柴人联盟', 'hcrlm.png', 'hcrlm.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('104', '5', '铠甲勇士之英雄传说', 'kjyszyxcs.png', 'kjyszyxcs.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('105', '5', '乱斗西游', 'ldxy.png', 'ldxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('106', '5', '奥特曼', 'atm.png', 'atm.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('107', '5', '梦幻西游', 'mhxy.png', 'mhxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('108', '5', '泡泡龙亚特兰蒂斯', 'pplytlds.png', 'pplytlds.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('109', '5', '熊出没之熊大快跑', 'xcmzxdkp.png', 'xcmzxdkp.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('110', '5', '少年三国志', 'snsgz.png', 'snsgz.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('111', '5', '爸爸去哪儿2', 'bbqne2.png', 'bbqne2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('112', '5', '天龙八部3D', 'tlbb3D.png', 'tlbb3D.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('113', '5', '大征途', 'dzt.png', 'dzt.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('114', '5', '大话西游', 'dhxy.png', 'dhxy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('115', '5', '熊出没2', 'xcm2.png', 'xcm2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('116', '5', '世界2', 'sj2.png', 'sj2.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('117', '5', '植物大战僵尸无尽版', 'zwdzjswjb.png', 'zwdzjswjb.png', '0', '0', '0');

INSERT INTO `vote_object` VALUES ('118', '1', '神魔', 'sm.png', 'sm.png', '0', '0', '0');

INSERT INTO `vote_object` VALUES ('119', '2', '锂', 'l.png', 'l.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('120', '2', '剑侠情缘', 'jxqy.png', 'jxqy.png', '0', '0', '0');
INSERT INTO `vote_object` VALUES ('121', '2', '无间狱', 'wjy.png', 'wjy.png', '0', '0', '0');
-- ----------------------------
-- Table structure for `vote_record`
-- ----------------------------
DROP TABLE IF EXISTS `vote_record`;
CREATE TABLE "vote_record" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "cid" int(11) DEFAULT NULL,
  "voteId" int(11) DEFAULT NULL,
  "voteIP" varchar(20) DEFAULT NULL,
  "voteTime" timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for passenger
-- ----------------------------
CREATE TABLE `passenger`  (
  `PCardID` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `PName` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Sex` tinyint(1) NULL DEFAULT NULL,
  `Age` smallint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`PCardID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for station
-- ----------------------------
CREATE TABLE `station`  (
  `SID` int(0) NOT NULL,
  `SName` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CityName` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for train
-- ----------------------------
CREATE TABLE `train`  (
  `TID` int(0) NOT NULL,
  `SDate` date NULL DEFAULT NULL,
  `TName` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SStationID` int(0) NULL DEFAULT NULL,
  `AStationID` int(0) NULL DEFAULT NULL,
  `STime` datetime(0) NULL DEFAULT NULL,
  `ATime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`TID`) USING BTREE,
  INDEX `trainfk1`(`SStationID`) USING BTREE,
  INDEX `trainfk2`(`AStationID`) USING BTREE,
  CONSTRAINT `trainfk1` FOREIGN KEY (`SStationID`) REFERENCES `station` (`SID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `trainfk2` FOREIGN KEY (`AStationID`) REFERENCES `station` (`SID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- 验证在建立外码时是否一定要参考被参照关系的主码
-- ALTER TABLE `trainpass` ADD FOREIGN KEY(`SID`) REFERENCES `train` (`SStationID`);
ALTER TABLE `trainpass` ADD FOREIGN KEY(`SID`) REFERENCES `station` (`SID`);
-- ----------------------------
-- Table structure for trainpass
-- ----------------------------
CREATE TABLE `trainpass`  (
  `TID` int(0) NOT NULL,
  `SNo` smallint(0) NOT NULL,
  `SID` int(0) NULL DEFAULT NULL,
  `STime` datetime(0) NULL DEFAULT NULL,
  `ATime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`TID`, `SNo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- 乘车记录表【记录编号，乘客身份证号，列车流水号，出发站编号，到达站编号，车厢号，席位排号，席位编号，席位状态】
-- TakeTrainRecord (RID int, PCardID char(18), TID int, SStationID int, AStationID int, CarrigeID smallint, SeatRow smallint，SeatNo char(1)，SStatus int)
-- 其中，主码、外码请依据应用背景合理定义。
-- CarrigeID若为空，则表示“无座”；
-- SeatNo只能取值为’A’、’B’、’C’、’E’、’F’，或为空值；
-- SStatus只能取值’0’（退票）、’1’（正常）、’2’（乘客没上车）。
-- 
drop table if exists taketrainrecord;
create table taketrainrecord(
	RID int PRIMARY KEY,
	PCardID char(18),
	TID int, 
	SStationID int, 
	AStationID int, 
	CarrigeID smallint,
	SeatRow smallint,
	SeatNo char(1),
	SStatus int NOT NULL,
	constraint fk_tr_pcardid foreign key(PCardID) REFERENCES passenger(PCardID),
	constraint fk_tr_tid foreign key(TID) REFERENCES train(TID),
	constraint fk_tr_ss foreign key(SStationID) REFERENCES station(SID),
	constraint fk_tr_as foreign key(AStationID) REFERENCES station(SID),
	constraint ck_seatno CHECK(SeatNo in('A','B','C','E','F')),
	constraint ck_sstatu CHECK(SStatus in(0,1,2))
);

-- --test
-- INSERT INTO `taketrainrecord` VALUES (1, '110101193412052028',1,1599,1621,18,5,'A',1);
-- INSERT INTO `taketrainrecord` VALUES (2, '110101193412052028',1,1599,1621,18,5,NULL,1);
-- INSERT INTO `taketrainrecord` VALUES (4, '110101193412052028',1,1599,1621,18,5,'K',1);
-- INSERT INTO `taketrainrecord` VALUES (3, '110101193412052028',1,1599,1621,18,5,'A',3);
-- ;
-- 
 


-- 诊断表【诊断编号，病人身份证号，诊断日期，诊断结果，发病日期】
-- DiagnoseRecord (DID int, PCardID char(18), DDay date, DStatus smallint, FDay date)
-- 其中，主码为DID；DStatus包括：1：新冠确诊；2：新冠疑似；3：排除新冠

drop table if exists DiagnoseRecord;
create table DiagnoseRecord (
	DID int primary key,  
	PCardID char(18), 
	DDay date, 
	DStatus smallint, 
	FDay date,
	CONSTRAINT fk_pcard foreign key(PCardID) references passenger(PCardID),
	constraint ck_Diag_status CHECK(DStatus in(1,2,3))
	);
	
-- select concat( CONCAT(FLOOR(2015 + (RAND() * 1)),'-',LPAD(FLOOR(10 + (RAND() * 2)),2,0),'-',LPAD(FLOOR(1 + (RAND() * 25)),2,0)))
-- 结果：2015-11-15
-- select CONCAT(LPAD(FLOOR(0 + (RAND() * 23)),2,0),':',LPAD(FLOOR(0 + (RAND() * 59)),2,0),':',LPAD(FLOOR(0 + (RAND() * 59)),2,0))
	
-- 结果：20:57:10

delimiter $$
drop procedure if exists filldiag;
create procedure filldiag()
begin
	declare PCardid1 char(18);
	declare didcount int DEFAULT 0;
	declare dstatus1 int DEFAULT 3;
	DECLARE diagdate,fdate,begindate date;
	declare done1 boolean default 0;
	declare cur1 cursor for select PCardID from passenger ORDER BY PCardID;
	DECLARE CONTINUE HANDLER FOR NOT FOUND set done1 =1;
	open cur1;
	FETCH cur1 into PCardid1;
	while not done1 do
		set begindate='2019-12-02';
-- 		设置时间差，开始+三个月随机时间差=诊断日   如果确诊，诊断日- 0到14 发病日期
		set diagdate=date_add(begindate,interval 0+FLOOR(RAND() * 90) day);
		set dstatus1=FLOOR(1+ (RAND() * 3));
-- 		诊断状态 1 2 3
		if dstatus1!=1 
		then set fdate=null;
		else set fdate=   DATE_SUB(diagdate, interval 4+FLOOR (RAND() * 10) day);
-- 		4到14天前发病		
		end if;
-- select char(if(floor(rand()*2)=0,65+floor(rand()*26),48+floor(rand()*9))); 随机日期
		set didcount=didcount+1;
		INSERT INTO DiagnoseRecord VALUES(didcount,PCardid1,diagdate,dstatus1,fdate);
		FETCH cur1 into PCardid1;
	END WHILE;
	CLOSE cur1;
end$$;
delimiter;

delete from DiagnoseRecord;
call filldiag();


-- 乘客紧密接触者表【接触日期, 被接触者身份证号，状态，病患身份证号】
-- TrainContactor (CDate date, CCardID char(18), DStatus smallint, PCardID char(18))
-- 其中，主码为全码。DStatus包括：1：新冠确诊；2：新冠疑似；3：排除新冠
drop table if exists TrainContactor;
create table TrainContactor (
	CDate date,
	CCardID char(18),
	DStatus smallint, 
	PCardID char(18),
	constraint ck_contac_status CHECK(DStatus in(1,2,3)),
	CONSTRAINT fk_ccard foreign key(CCardID) references passenger(PCardID),
	CONSTRAINT fk_pcard2 foreign key(PCardID) references passenger(PCardID),
	CONSTRAINT pk_contactor PRIMARY key(CDate, CCardID, DStatus, PCardID)
	);
	
ALTER TABLE traincontactor drop PRIMARY KEY;
ALTER TABLE traincontactor ADD CONSTRAINT pk_contactor PRIMARY key(CDate, CCardID, DStatus, PCardID);

ALTER TABLE 



-- -- 遇到某个游标的筛选条件来自于 先前语句运行的结果，比较常见的方式是 再写一个存储过程，通过调用来完成 动态参数的配置，或者使用 动态sql的功能
-- -- 接触表
--  嵌套游标，内层用到了外层结果，但是游标不能再内层声明。
-- delimiter $$
-- drop procedure if exists fillcontact;
-- create procedure fillcontact()
-- begin
-- 	declare patientid char(18);
-- 	declare contactorid char(18);
-- 	DECLARE contactdate date;
-- 	declare temsno1,temsno2 smallint;
-- 	DECLARE temtid1,sstation1,astation1,carrigeid1 int;
-- 	declare done1 boolean default 0;
-- 	declare cur1 cursor for select PCardID from DiagnoseRecord where DStatus=1 ORDER BY PCardID;
-- 	declare done2 boolean default 0;
-- 	declare cur2 cursor for select PCardID from contacttmp;
-- -- 	不能重复声明变量，循环里不能。。
-- 	DECLARE CONTINUE HANDLER FOR NOT FOUND 
-- 	begin
-- -- 	到了尾部
-- 		set done2 =1; 
-- 		set done1 =1;
-- 	END;
-- 	
-- 	open cur1;
-- 	FETCH cur1 into patientid;
-- 	while not done1 do
-- 		set done2=0;
-- 		select TID,SStationID,AStationID,CarrigeID into temtid1,sstation1,astation1,carrigeid1
-- 		from taketrainrecord
-- 		WHERE PCardID=patientid;
-- 		
-- 		SELECT SDate into contactdate from train WHERE train.TID=temtid1;
-- 		
-- 		drop table if exists tmp1;
-- 		CREATE TEMPORARY TABLE tmp1 AS (SELECT SNo,SID  FROM trainpass WHERE trainpass.TID=temtid1);
-- 		
-- 		SELECT SNo into temsno1 FROM tmp1 WHERE tmp1.SID=sstation1;
-- 		SELECT SNo into temsno2 FROM tmp1 WHERE tmp1.SID=astation1;
-- -- 		SELECT SID FROM tmp1 WHERE  trainpass.SNo BETWEEN temsno1 and temsno2;
-- -- mysql 不支持 Select .. into newtable
-- 		drop table if exists contacttmp;
-- 		CREATE TEMPORARY TABLE contacttmp AS
-- 		(			select	PCardID 
-- 					from taketrainrecord
-- 					where 	TID=temtid1 
-- 								and CarrigeID=carrigeid1
-- 								and (SStationID BETWEEN temsno1 and temsno2 or  AStationID BETWEEN temsno1 and temsno2));
-- 
-- 		open cur2;
-- 		FETCH cur2 into contactorid;
-- 		while not done2 do
-- 			INSERT INTO  VALUES(contactdate,contactorid,1+FLOOR (RAND() * 3),patientid);
-- 			FETCH cur2 into contactorid;
-- 		end while;
-- 		set done1=0;
-- -- 		文件尾外循环连带设置1了，改回
-- 		FETCH cur1 into patientid;
-- 	END WHILE;
-- 	CLOSE cur1;
-- end$$;
-- delimiter;
-- 
-- delete from traincontactor;
-- call fillcontact();


delimiter $$
drop procedure if exists innerfillcontact;
create procedure innerfillcontact(in contactdate date,in patientid char(18))
begin
##利用concat拼接sql,execute中执行拼接的sql
		declare done2 boolean default 0;
		declare contactorid char(18);
-- 		declare cur2 cursor for select PCardID from contacttmp;
-- 		set @sqlStr=concat('declare cur2 cursor for select PCardID from',tbNm);
--     PREPARE stmt from @sqlStr;
--     EXECUTE stmt;
		declare cur2 cursor for select PCardID from contacttmp;
		DECLARE CONTINUE HANDLER FOR NOT FOUND set done2=1;
		open cur2;
		FETCH cur2 into contactorid;
		while not done2 do
			INSERT INTO  traincontactor VALUES(contactdate,contactorid,1+FLOOR (RAND() * 3),patientid);
			FETCH cur2 into contactorid;
		end while;
	CLOSE cur2;		
end$$;
delimiter;


delimiter $$
drop procedure if exists fillcontact;
create procedure fillcontact()
begin
	declare patientid char(18);
	declare contactorid char(18);
	DECLARE contactdate date;
	declare temsno1,temsno2 smallint;
	DECLARE temtid1,sstation1,astation1,carrigeid1 int;
	declare done1 boolean default 0;
	declare cur1 cursor for select PCardID from DiagnoseRecord where DStatus=1 ORDER BY PCardID;

-- 	不能重复声明变量，循环里不能。。
	DECLARE CONTINUE HANDLER FOR NOT FOUND set done1 =1;
	open cur1;
	FETCH cur1 into patientid;
	while not done1 do
		select TID,SStationID,AStationID,CarrigeID into temtid1,sstation1,astation1,carrigeid1
		from taketrainrecord
		WHERE PCardID=patientid;
		
		SELECT SDate into contactdate from train WHERE train.TID=temtid1;
		
		drop table if exists tmp1;
		CREATE TEMPORARY TABLE tmp1 AS (SELECT SNo,SID  FROM trainpass WHERE trainpass.TID=temtid1);
		
		SELECT SNo into temsno1 FROM tmp1 WHERE tmp1.SID=sstation1;
		SELECT SNo into temsno2 FROM tmp1 WHERE tmp1.SID=astation1;
-- 		SELECT SID FROM tmp1  WHERE  trainpass.SNo BETWEEN temsno1 and temsno2;
-- mysql 不支持 Select .. into newtable
-- (下面这步骤分了两个，性能30倍。。。。)
-- 		drop table if exists contacttmp;
-- 		CREATE  TEMPORARY TABLE contacttmp AS
-- 		(		
-- 					select	PCardID
-- 					from taketrainrecord,trainpass as trp1,trainpass as trp2
-- 					where 	taketrainrecord.TID=temtid1
-- 							and taketrainrecord.PCardID!=patientid
-- 							and CarrigeID=carrigeid1	
-- 							and  trp1.TID=temtid1
-- 							and  trp2.TID=temtid1
-- 							and  trp1.SID=SStationID 
--  							and  trp2.SID=AStationID 
-- 							and (trp1.SNo BETWEEN temsno1 and temsno2 or  trp2.SNo BETWEEN temsno1 and temsno2)
-- 		);
		
		drop table if exists contacttmp0;
		CREATE  TEMPORARY TABLE contacttmp0 AS
		(		
					select	PCardID,SStationID,AStationID
					from taketrainrecord
					where 	taketrainrecord.TID=temtid1
							and taketrainrecord.PCardID!=patientid
							and CarrigeID=carrigeid1	
		);
		drop table if exists contacttmp;
		CREATE  TEMPORARY TABLE contacttmp AS
		(		
					select	PCardID
					from contacttmp0,trainpass as trp1,trainpass as trp2
					where 	
							 trp1.TID=temtid1
							and  trp2.TID=temtid1
							and  trp1.SID=SStationID 
 							and  trp2.SID=AStationID 
							and (trp1.SNo BETWEEN temsno1 and temsno2 or  trp2.SNo BETWEEN temsno1 and temsno2)
		);
		call innerfillcontact(contactdate,patientid);
		FETCH cur1 into patientid;
	END WHILE;
	CLOSE cur1;
end$$;
delimiter;

delete from traincontactor;
call fillcontact();

-- 验证接触表
select * from taketrainrecord WHERE PCardID='230231197505243594'
UNION
select * from taketrainrecord WHERE PCardID='330803200111012166';

select * from taketrainrecord WHERE PCardID='110112194507080930'
UNION
select * from taketrainrecord WHERE PCardID='140311195809071233';




-- 2.2数据更新
2.21
select count(*) from taketrainrecord;
增
INSERT into taketrainrecord VALUES(99999,130623195203211991,10418,1648,1648,1,2,'A',1);
SELECT * FROM taketrainrecord  where RID=99999;
改
UPDATE taketrainrecord set SeatNo='B' WHERE   RID=99999;
SELECT * FROM taketrainrecord  where RID=99999;
删
DELETE from  taketrainrecord  where RID=99999;  
SELECT * FROM taketrainrecord  where RID=99999;
2.22
-- create table wuhantake like data_mgr;
-- insert into wuhantake select *from data_mgr;
drop table if exists wuhantake;
CREATE table wuhantake as(
	SELECT *
	FROM taketrainrecord
	WHERE taketrainrecord.SStationID IN( SELECT SID
																				FROM station
																				WHERE station.SName='武汉'
																			)
);

2.24
drop table if exists test2_2_4;
CREATE table test2_2_4(
	col1 int,
	col2 int
);
INSERT into test2_2_4 VALUES(2,3);
INSERT into test2_2_4 VALUES(2,4);
INSERT into test2_2_4 VALUES(5,7);
INSERT into test2_2_4 VALUES(5,7);
SELECT * from test2_2_4;
DELETE FROM test2_2_4 WHERE col1=2;
UPDATE test2_2_4 SET col1=6 WHERE col2=7;
SELECT  * from test2_2_4;

2.25
drop view patientrecord;
create view patientrecord as(
SELECT taketrainrecord.PCardID,passenger.PName,passenger.Age,
				train.TName,train.SDate,CarrigeID,taketrainrecord.SeatRow,SeatNo
FROM passenger,taketrainrecord,train
WHERE taketrainrecord.SStatus=1
	and taketrainrecord.PCardID=passenger.PCardID
	and taketrainrecord.TID=train.TID
ORDER BY taketrainrecord.PCardID ASC , train.SDate DESC
);
2.26
DROP TRIGGER IF EXISTS DiagnoseT;
CREATE TRIGGER DiagnoseT
AFTER INSERT ON diagnoserecord
FOR EACH ROW
BEGIN
	IF new.DStatus = 1 -- 确诊
	THEN 
     INSERT INTO TrainContactor(CDate,CCardID,DStatus,PCardID)
		 SELECT TRO.SDate,TRO.PCardID,2,new.PCardID -- 疑似
		 FROM (SELECT new.PCardID,taketrainrecord.TID ,taketrainrecord.CarrigeID,taketrainrecord.SeatRow,train.SDate   -- 病人14天内乘车记录的临时表
                                 FROM taketrainrecord,train   -- 存储病人ID，车次号，车厢号，座位排号，发车日期
                                 WHERE taketrainrecord.PCardID = new.PCardID -- 病人的记录
                                 AND train.TID = taketrainrecord.TID
                                 AND taketrainrecord.SStatus = 1 -已乘车                                 
                                 AND DATE_FORMAT(new.FDay,'%Y-%m-%d') >= Train.SDate  -- 14天之内                                
                                 AND DATE_FORMAT(new.FDay,'%Y-%m-%d') < Train.SDate + 14)AS TRP(PCardID,TID,CarrigeID, SeatRow,SDate),
           (SELECT taketrainrecord.PCardID,taketrainrecord.TID ,taketrainrecord.CarrigeID,taketrainrecord.SeatRow,train.SDate   -- 除病人外其他人14天内乘车记录的临时表
                                 FROM taketrainrecord,train   -- 存储病人ID，车次号，车厢号，座位排号，发车日期
                                 WHERE taketrainrecord.PCardID!=new.PCardID -- 不是病人的记录
                                 AND train.TID = taketrainrecord.TID
                                 AND taketrainrecord.SStatus = 1 --    已乘车                              
                                 AND DATE_FORMAT(new.FDay,'%Y-%m-%d') >= Train.SDate  -- 14天之内                                
                                 AND DATE_FORMAT(new.FDay,'%Y-%m-%d') < Train.SDate + 14)
						AS TRO(PCardID,TID,CarrigeID, SeatRow,SDate)
		 WHERE (
            (TRO.SeatRow = TRP.SeatRow ) OR -- 同一行
						(TRO.SeatRow - 1 = TRP.SeatRow)OR  -- 前一行
						(TRO.SeatRow + 1 = TRP.SeatRow ))  -- 后一行  
             AND TRO.CarrigeID = TRP.CarrigeID   -- 同一车厢 
             AND TRO.TID = TRP.TID  -- 同一车次
             AND TRO.SDate = TRP.SDate;	-- 同一天
    
      UPDATE TrainContactor 
      SET DStatus = 1
	    WHERE TrainContactor.CCardID = new.PCardID
            OR traincontactor.CCardID IN(SELECT PCardID FROM diagnoserecord WHERE diagnoserecord.DStatus = 1) ;	-- 注意，为避免重复加入的情况，会检查诊断表是否已确诊
   -- 更新密切接触者中感染者信息	    	 			 			 
	END IF;
END;

2.26
-- 存储函数或触发器中不允许显式或隐式提交mysql隐式提交的sql语句“drop...”，"truncate table ..."
-- ***mysql 不能同时update or insert***
delimiter //
drop trigger if exists contactriger;
CREATE TRIGGER contactriger AFTER INSERT  ON DiagnoseRecord FOR EACH ROW 
	BEGIN
			if  DiagnoseRecord.DStatus=1
			THEN 	CALL innner_contactriger(new.PCardID,new.FDay);					
			end if;
	END;//
	delimiter;
delimiter //
drop trigger if exists contactriger2;
CREATE TRIGGER contactriger2 AFTER UPDATE  ON DiagnoseRecord FOR EACH ROW
	BEGIN
			if  new.DStatus=1
			THEN 	CALL innner_contactriger(new.PCardID,new.FDay);					
			end if;
	END;//
delimiter;
	
触发器测试
诊断表有一条不得病数据
4	110101198304063574	2020-02-21	3	
让他得病
UPDATE  DiagnoseRecord  set DStatus=1,FDay='2020-02-16' WHERE PCardID='110101198304063574';
查他乘车记录
SELECT * FROM taketrainrecord WHERE PCardID=110101198304063574;
4	110101198304063574	34	1620	1621	1	4	E	0
查同车次 同车厢记录
SELECT * FROM taketrainrecord WHERE TID=34 and CarrigeID=1;
没有其他记录..数据少了

SELECT count(PCardID),TID ,CarrigeID FROM taketrainrecord GROUP BY TID ,CarrigeID  LIMIT 1000;
找到一个同车次同车厢 两个人的，， 39238车7车厢（乘车记录25k小了，车厢太空，随便搞不一定有）
SELECT * FROM taketrainrecord WHERE TID=39238 and CarrigeID=7;
21	11010519470926382X	39238	1615	1572	7	10	F	0
3841	210421193405023013	39238	1571	1573	7	7	A	1
查这俩人诊断表
SELECT * FROM DiagnoseRecord WHERE PCardID='11010519470926382X' or PCardID='210421193405023013';
21	11010519470926382X	2020-01-29	2	
3841	210421193405023013	2020-02-29	3	
都没病，让第一个得病 发病时间乘车时间后一点儿
SELECT train.SDate from train WHERE TID=39238;
2019-12-18
UPDATE  diagnoserecord  set DStatus=1,FDay='2019-12-24' WHERE PCardID='11010519470926382X';
SELECT count(*) FROM  diagnoserecord ;
查第是否进入接触表
SELECT * FROM traincontactor WHERE traincontactor.CDate='2019-12-24';
其中有
2019-12-24	210421193405023013	2	11010519470926382X
表示触发器正确


SELECT FROM train WHERE TID=39238
SELECT * FROM DiagnoseRecord LIMIT 10;
SELECT * FROM taketrainrecord WHERE PCardID='110101194701201338';


delimiter $$
drop procedure if exists innner_contactriger;
create procedure innner_contactriger(in patientid char(18),in Fday0 date)
begin
	declare contactorid char(18);
	DECLARE contactdate date;
	declare temsno1,temsno2 smallint;
	DECLARE temtid1,sstation1,astation1,carrigeid1 int;
	declare done1 boolean default 0;
	declare cur1 cursor for 	
		 SELECT taketrainrecord.TID,taketrainrecord.CarrigeID,taketrainrecord.SStationID,taketrainrecord.AStationID
		 from taketrainrecord,train
		 WHERE taketrainrecord.PCardID=patientid
				and taketrainrecord.TID=train.TID
				and (train.SDate BETWEEN  DATE_SUB(FDay0,INTERVAL 14 day) and  FDay0 );
	DECLARE CONTINUE HANDLER FOR NOT FOUND set done1 =1;
	open cur1;
	FETCH cur1 into temtid1,carrigeid1,sstation1,astation1;
	while not done1 do		
		SELECT SDate into contactdate from train WHERE train.TID=temtid1;
		drop table if exists tmp1;
		CREATE TEMPORARY TABLE tmp1 AS (SELECT SNo,SID  FROM trainpass WHERE trainpass.TID=temtid1);
		
		SELECT SNo into temsno1 FROM tmp1 WHERE tmp1.SID=sstation1;
		SELECT SNo into temsno2 FROM tmp1 WHERE tmp1.SID=astation1;

		drop table if exists contacttemp0;
		CREATE  TEMPORARY TABLE contacttemp0 AS
		(		
					select	PCardID,SStationID,AStationID
					from taketrainrecord
					where 	taketrainrecord.TID=temtid1
							and taketrainrecord.PCardID!=patientid
							and CarrigeID=carrigeid1	
		);
		drop table if exists contacttemp;
		CREATE TEMPORARY TABLE contacttemp AS
		(		
					select	PCardID
					from contacttmp0,trainpass as trp1,trainpass as trp2
					where 	
							 trp1.TID=temtid1
							and  trp2.TID=temtid1
							and  trp1.SID=SStationID 
 							and  trp2.SID=AStationID 
							and (trp1.SNo BETWEEN temsno1 and temsno2 or  trp2.SNo BETWEEN temsno1 and temsno2)
		);
		call innerfillcontact2(contactdate,patientid);
		FETCH cur1 into temtid1,carrigeid1,sstation1,astation1;
	END WHILE;
	CLOSE cur1;
end$$;
delimiter;

delimiter $$
drop procedure if exists innerfillcontact2;
create procedure innerfillcontact2(in contactdate date,in patientid char(18))
begin
##利用concat拼接sql,execute中执行拼接的sql
		declare done2 boolean default 0;
		declare contactorid char(18);
-- 		declare cur2 cursor for select PCardID from contacttmp;
-- 		set @sqlStr=concat('declare cur2 cursor for select PCardID from',tbNm);
--     PREPARE stmt from @sqlStr;
--     EXECUTE stmt;
		declare cur2 cursor for select PCardID from contacttemp;
		DECLARE CONTINUE HANDLER FOR NOT FOUND set done2=1;
		open cur2;
		FETCH cur2 into contactorid;
		while not done2 do
			INSERT INTO  traincontactor VALUES(contactdate,contactorid,1+FLOOR (RAND() * 3),patientid);
			FETCH cur2 into contactorid;
		end while;
	CLOSE cur2;		
end$$;
delimiter;

2.3
1）查询确诊者“张三”的在发病前14天内的乘车记录；
找一个发病的 李玉兰
SELECT * FROM passenger,diagnoserecord WHERE diagnoserecord.DStatus=1 and passenger.PCardID=diagnoserecord.PCardID LIMIT 10;
李玉兰周志颖王小范胡冬松付强大郑文坚刘丹星莫梦莹郭宇刘珩
110101193412052028	李玉兰	0	86	1	110101193412052028	2019-12-14	1	2019-12-04

SELECT * FROM taketrainrecord 
WHERE PCardID in(SELECT PCardID FROM passenger WHERE passenger.PName='刘丹星')
			AND TID in(SELECT train.TID FROM passenger,diagnoserecord,train
												WHERE passenger.PName='刘丹星'
													and diagnoserecord.PCardID=passenger.PCardID
													and train.SDate BETWEEN DATE_SUB(diagnoserecord.FDay,INTERVAL 14 day) and diagnoserecord.FDay   );
-- 用户变量查要快一倍						
SELECT * FROM taketrainrecord 
WHERE PCardID in(SELECT @pcardid1:=PCardID FROM passenger WHERE passenger.PName='刘丹星') 
			AND TID in(SELECT train.TID
									FROM train,(SELECT @tmpday1:=FDay FROM diagnoserecord WHERE diagnoserecord.PCardID=@pcardid1) as zzz1
									WHERE  train.SDate BETWEEN  DATE_SUB(@tmpday1 ,INTERVAL 100 day)and '@tmpday1');

2）查询所有从城市“武汉”出发的乘客乘列车所到达的城市名；
SELECT @tmpwuhansid:=station.SID FROM station WHERE station.SName='武汉';
SELECT station.CityName FROM station
WHERE station.SID in(SELECT taketrainrecord.AStationID FROM taketrainrecord WHERE taketrainrecord.SStationID=@tmpwuhansid );
3）计算每位新冠患者从发病到确诊的时间间隔（天数）及患者身份信息，并将结果按照发病时间天数的降序排列；

select DiagnoseRecord.PCardID,PName,Sex,Age,DATEDIFF(DDay,FDay) as confirmday 
from DiagnoseRecord,Passenger
where DiagnoseRecord.PCardID=Passenger.PCardID and DStatus=1
ORDER BY  confirmday desc;

4）查询“2020-01-22”从“武汉”发出的所有列车；
select TName
from Station,Train
where Station.SID=Train.SStationID and Station.CityName='武汉' and Train.SDate='2020-01-22';
5）查询“2020-01-22”途经“武汉”的所有列车；
select distinct TName
from Train,TrainPass,Station
where Train.TID=TrainPass.TID and TrainPass.SID=Station.SID and Station.CityName='武汉'
and TrainPass.STime is not null and TrainPass.ATime is not null 
and (DATE_FORMAT(TrainPass.ATime,'%Y-%m-%d')='2020-01-22' or  DATE_FORMAT(TrainPass.STime,'%Y-%m-%d')='2020-01-22');
6）查询“2020-01-22”从武汉离开的所有乘客的身份证号、所到达的城市、到达日期；  
-- a1 b1 武汉那天出发  a2 b2选出来到达的城市日期
SELECT PCardID,b2.CityName,DATE_FORMAT(a2.STime,'%Y-%m-%d')
FROM taketrainrecord,trainpass as a1,trainpass as a2,station as b1,station as b2
WHERE taketrainrecord.TID=a1.TID and taketrainrecord.SStationID=a1.SID 
and DATE_FORMAT(a1.ATime,'%Y-%m-%d')='2020-01-22' and a1.SID=b1.SID and b1.SName='武汉'
and   taketrainrecord.TID=a2.TID and taketrainrecord.AStationID=a2.SID and a2.SID=b2.SID  ;
7）统计“2020-01-22” 从武汉离开的所有乘客所到达的城市及达到各个城市的武汉人员数。
-- 同6一致 加入selct count
SELECT b2.CityName,count(DISTINCT PCardID)
FROM taketrainrecord,trainpass as a1,trainpass as a2,station as b1,station as b2
WHERE taketrainrecord.TID=a1.TID and taketrainrecord.SStationID=a1.SID 
and DATE_FORMAT(a1.ATime,'%Y-%m-%d')='2020-01-22' and a1.SID=b1.SID and b1.SName='武汉'
and   taketrainrecord.TID=a2.TID and taketrainrecord.AStationID=a2.SID and a2.SID=b2.SID  
GROUP BY  b2.CityName;
8）查询2020年1月到达武汉的所有人员；
-- 主要 武汉乘车占 时间在1月  between and 
select Passenger.PCardID,PName,Sex,Age
from Passenger,TakeTrainRecord,TrainPass,Station
where Passenger.PCardID=TakeTrainRecord.PCardID 
and TakeTrainRecord.AStationID=Station.SID 
and TakeTrainRecord.TID=TrainPass.TID
and TakeTrainRecord.AStationID=TrainPass.SID 
and Station.CityName='武汉' 
and DATE_FORMAT(TrainPass.STime,'%Y-%m-%d') between '2020-01-01' and '2020-01-31';

9） 查询2020年1月乘车途径武汉的外地人员（身份证非“420”开头）；

-- 3选武汉选时间  1 上车站信息  2 下车站信息   taketrainrecord贯穿   加distinct 否则经过几站有几次
-- 正则匹配  left(passenger.PCardID, 3)<>'420'
SELECT DISTINCT Passenger.PCardID,PName,Sex,Age,taketrainrecord.RID
FROM passenger,taketrainrecord,trainpass as tr1,trainpass as tr2,trainpass as tr3,station
	where taketrainrecord.TID=tr1.TID and taketrainrecord.TID=tr2.TID  and taketrainrecord.TID=tr3.TID 
		and  tr3.SID=station.SID and station.CityName='武汉' 
		and DATE_FORMAT(tr3.Atime,'%Y-%m-%d') BETWEEN '2020-01-01' and '2020-01-31'
		and  taketrainrecord.SStationID=tr1.SID and taketrainrecord.AStationID=tr2.SID
		and  tr3.SNo BETWEEN tr1.SNo AND tr2.SNo
		and  left(passenger.PCardID, 3)<>'420' and taketrainrecord.PCardID=passenger.PCardID;

前两条
110101200504220677	王连重	1	15	9rid
110114195202010533	邓汉源	1	68	74rid
SELECT @tmpwuhansid:=station.SID FROM station WHERE station.SName='武汉';
1597
SELECT * FROM taketrainrecord WHERE rid=9;
9	110101200504220677	6249	1542	1535	4	4	C	0
SELECT * FROM trainpass WHERE tid=6249
过武汉，时间对
SELECT * FROM train WHERE tid=6249
6249	2020-01-31	D3251	3997	2127	2020-01-31 09:26:00	2020-01-31 19:37:00

10）统计“2020-01-22”乘坐过‘G007’号列车的新冠患者在火车上的密切接触乘客人数（每位新冠患者的同车厢人员都算同车密切接触）。
-- 接触表接触日期 2020-01-22 ，确诊者身份id-乘车记录-列车 限制tid而限制G007
-- 病人记录视图没下车占。。。  不好找，即使不限制g007也只有几个
SELECT traincontactor.PCardID,count(take2.PCardID)
FROM taketrainrecord as take1,taketrainrecord as take2,traincontactor,train
WHERE traincontactor.CDate='2020-01-22'
and traincontactor.PCardID=take1.PCardID and take1.PCardID<>take2.PCardID
and take1.TID=train.TID and train.TName='G007'
and take1.TID=take2.TID and take1.CarrigeID=take2.CarrigeID
GROUP BY take1.PCardID

11）查询一趟列车的一节车厢中有3人及以上乘客被确认患上新冠的列车名、出发日期，车厢号； 
					 						  
SELECT TName,Train.SDate,CarrigeID
FROM Train,TakeTrainRecord,DiagnoseRecord
WHERE DiagnoseRecord.DStatus = 1 -- 已确诊
      AND TakeTrainRecord.PCardID = DiagnoseRecord.PCardID -- 乘客已确诊
      AND TakeTrainRecord.TID = Train.TID -- 车次
      AND TakeTrainRecord.SStatus = 1 -- 已乘坐
GROUP BY CarrigeID,TName,Train.SDate
HAVING COUNT(*)>=3;

12）查询没有感染任何周边乘客的新冠乘客的身份证号、姓名、乘车日期； 
-- 需要考虑这个乘客的乘车区间，在这个乘车区间内所有周边乘客是否感染？

SELECT DISTINCT passenger.PCardID,passenger.PName,train.SDate 
FROM train,TakeTrainRecord,DiagnoseRecord,traincontactor,passenger
WHERE train.TID = TakeTrainRecord.TID -- 车号统一
	    AND TakeTrainRecord.PCardID = DiagnoseRecord.PCardID -- 被确诊的乘客在乘车记录中
      AND taketrainrecord.SStatus = 1 -- 已上车
	    AND passenger.PCardID = TakeTrainRecord.PCardID -- 被确诊的乘客在乘客信息中
	    AND DiagnoseRecord.DStatus = 1 ;-- 确诊
	    AND diagnoserecord.DID NOT IN(SELECT traincontactor.PCardID  -- 筛出周边旅客被感染的
                                    FROM traincontactor
                                    WHERE traincontactor.DStatus = 1 );

13）查询到达 “北京”、或“上海”，或“广州”（即终点站）的列车名，
要求where子句中除了连接条件只能有一个条件表达式； 利用 in
SELECT DISTINCT train.TName
FROM station,train
WHERE station.SID=train.AStationID
and station.CityName IN('广州','上海','北京');
14）查询“2020-01-22”从“武汉站”出发，然后当天换乘另一趟车的乘客身份证号和首乘车次号，
		结果按照首乘车次号降序排列，同车次则按照乘客身份证号升序排列；
--  换乘车 当天第二个车上车站和第一趟车下车占相同  根据两个tid sid 限制时间同一天 晚于第一个
--  SELECT @tmpwuhansid:=station.SID FROM station WHERE station.SName='武汉'; 
-- 多一句吧，简单 或者直接用武汉的1597
SELECT a1.TID
FROM taketrainrecord as tak1,taketrainrecord as tak2,trainpass as a1,trainpass as a2,station as b1,station as b2
WHERE tak1.TID=a1.TID and tak1.SStationID=1957 and tak1.AStationID=a1.SID 
			and tak1.AStationID=tak2.SStationID and tak2.TID=a2.TID and tak2.SStationID=a2.sid
			and DATE_FORMAT(a1.STime,'%Y-%m-%d')=DATE_FORMAT(a2.ATime,'%Y-%m-%d')
			and a1.STime<a2.ATime
ORDER BY a1.TID
			;
15）查询所有新冠患者的身份证号，姓名及其2020年以来所乘坐过的列车名、发车日期，要求即使该患者未乘坐过任何列车也要列出来；
SELECT diagnoserecord.PCardID,passenger.PName,train.TName,train.SDate
FROM diagnoserecord,train,taketrainrecord,passenger
WHERE diagnoserecord.DStatus=1
	and diagnoserecord.PCardID=taketrainrecord.PCardID
	and taketrainrecord.TID=train.TID
	and train.SDate>'2019-12-30'
	and diagnoserecord.PCardID=passenger.PCardID;

16）查询所有发病日期相同而且确诊日期相同的病患统计信息，包括：发病日期、确诊日期和患者人数，结果按照发病日期降序排列的前提下再按照确诊日期降序排列。

SELECT diagnoserecord.FDay,diagnoserecord.DDay,count(diagnoserecord.PCardID)
FROM diagnoserecord
GROUP BY diagnoserecord.DDay,diagnoserecord.FDay
ORDER BY diagnoserecord.FDay DESC ,diagnoserecord.DDay DESC



CREATE table zz1(fday date);
INSERT into zz1 VALUES ('2020.1.29');
SELECT DATE_SUB(zz2.fday,INTERVAL 100 day) aa1
SELECT now() aa2;
ALTER TABLE zz1 RENAME to zz2;


SET FOREIGN_KEY_CHECKS = 1;
set time_zone = '+8:00';
set global time_zone='+08:00';
set @@global.auto_increment_increment = 1;
set @@auto_increment_increment =1;


--  mysql不支持语句级别触发器
-- 触发器不用声明直接使用加@的变量
-- 自增的insert 的列用0或null

1、职员表【姓名，工号，职位】
工号为主码
职务包括 审核人员 销售人员 采购人员

CREATE DATABASE if NOT EXISTS StorageManagement;
USE StorageManagement;


CREATE TABLE staff(
		sname char(10),
		sno char(6) PRIMARY KEY,
		position char(4)
)
INSERT into staff VALUES
('李采购','c00001','采购'),
('王采购','c00002','采购'),
('章审计','s00001','审计'),
('孙销售','x00001','销售'),
('林销售','x00002','销售');

2、账户表【账号（同工号），密码】
账号为主码，参考外键职员表工号
CREATE TABLE account(
	sno char(4) PRIMARY KEY,
	spswd char(6),
	FOREIGN KEY (sno) REFERENCES staff(sno)
);

3 库存表【货号，货名，存放地，库存数量，生产厂家】货号为主码
DROP TABLE IF EXISTS  inventory;
CREATE TABLE inventory(
	ino int PRIMARY KEY,
	iname CHAR(10) ,
	ilocation char(10),
	inum int,
	iproducer char(10),
	CHECK(inum>=0)
);

INSERT into inventory VALUES
(1,'可口可乐','武汉1仓',100,'某饮料厂'),
(2,'怡宝','武汉1仓',200,'某饮料厂'),
(3,'农夫山泉','武汉1仓',300,'某饮料厂'),
(4,'脉动','武汉1仓',200,'某饮料厂'),
(5,'冰红茶','武汉1仓',100,'某饮料厂'),
(6,'绿茶','武汉1仓',100,'某饮料厂');

3、进货单【单号，货号，货名，下单时间，结单时间，完成状态，采购人工号，审核人工号】
单号为主码
货号外键参考库存表货号，采购人，审核人外键参考职员表。
完成状态 0，1，0代表待审核，1代表完成，完成的订单不可修改

-- mysql> set @@global.auto_increment_increment = 1;
-- mysql> set @@auto_increment_increment =1;
-- 
drop TABLE IF EXISTS buy;
CREATE TABLE buy (
	bno int PRIMARY KEY auto_increment,  -- 主键自增 序号
	bgno int ,
	bgname char(10) ,
	bnum int,
	bstatus smallint, -- 完成状态 0，1，0代表待审核，1代表完成，
	bstime datetime,
	betime datetime,
	bbuyer char(6) ,
	bchecker char(6),
	FOREIGN KEY (bgno) REFERENCES inventory(ino),
  FOREIGN KEY (bbuyer) REFERENCES staff(sno),
  FOREIGN KEY (bchecker) REFERENCES staff(sno),
	CHECK(bnum>0)
)


4、
单号为主码
货号外键参考库存表货号，销售人，审核人外键参考职员表。
完成状态 0或1或2，0代表待审核，1代表完成，2代表审核完成但是缺货。	完成的订单不可修改，当补货后达到所需出货数量时自动出货，完成状态变为1。

出货单【单号，货号，货名，下单时间，结单时间，完成状态，销售工号，审核人工号】
drop TABLE IF EXISTS sell;
CREATE TABLE sell(
	sno int PRIMARY KEY auto_increment, -- 主键自增 序号
	sgno int ,
	sgname char(10) ,
	snum int,
	sstatus smallint,  -- 完成状态 0，1，2
	-- 0代表待审核，1代表完成，2代表审核完成但是缺货，只有0状态可被审核
	sstime datetime,
	setime datetime,
	ssellor char(6) ,
	schecker char(6),
	FOREIGN KEY (sgno) REFERENCES inventory(ino), -- 外键参照库存商品号
  FOREIGN KEY (ssellor) REFERENCES staff(sno),  -- 外键参照职员工号
  FOREIGN KEY (schecker) REFERENCES staff(sno),
	CHECK(snum>0)  -- 自定义约束
)

5缺货表【缺货货号，缺货数量】
货号为主键，参考库存表货号。
drop table if EXISTS lack; 
CREATE TABLE lack(
	lno int,
	lname char(10),
	lnum int,
	PRIMARY KEY (lno),
	FOREIGN KEY(lno) REFERENCES inventory(ino)
)

-- mysql不支持语句级别触发器
-- 采购通过审核后。判断是否缺货，处理库存和缺货
DROP TRIGGER IF EXISTS TrigerBuy;
CREATE TRIGGER TrigerBuy
AFTER UPDATE ON buy
FOR EACH ROW
BEGIN
	IF  old.bstatus=0 AND new.bstatus=1  -- 审核通过
	THEN 
		set @lacknum=(SELECT IFNULL(lnum,0) FROM lack WHERE lno=new.bgno); -- 查缺货数量，不缺为0
		if @lacknum=0 
				 then UPDATE inventory set inum=inum+new.bnum WHERE ino=new.bgno;  -- 不缺货 增库存
		else set @changenum=new.bnum-@lacknum;
				 if @changenum>=0  -- 缺货 新货凑够发货 更新库存，删缺货表记录，缺货单状态改为1完成
						then 
									UPDATE inventory set inum=@changenum WHERE ino=new.bgno;
								  UPDATE sell set sstatus=1 WHERE sgno=new.bgno and sstatus=2;
								  DELETE FROM lack WHERE lno=new.bgno;
				 else UPDATE lack set lnum=-@changenum WHERE lno=new.bgno; -- 缺货还不够发货，增库存 减缺货量
						  UPDATE inventory set inum=inum+new.bnum WHERE ino=new.bgno;
				 end if;
		end if;	
	END IF;
END;	

-- INSERT INTO buy 	VALUES(7,2,'怡宝',30,0,now(),now(),'x00001','s00001');
-- UPDATE buy set bstatus=1 WHERE bno=7;
-- INSERT INTO buy 	VALUES(8,1,'可口可乐',40,0,now(),now(),'x00001','s00001');
-- UPDATE buy set bstatus=1 WHERE bno=8;
-- INSERT INTO buy 	VALUES(6,1,'可口可乐',500,0,now(),now(),'x00001','s00001');
-- UPDATE buy set bstatus=1 WHERE bno=6;
-- 
-- UPDATE sell set sstatus=2 WHERE sgno=1 and sstatus=1;
-- DELETE FROM lack WHERE lno=1;

-- 销售触发 审核后销售订单状态变1 ，如果货足，发货库存减，否则订单状态改为2，填缺货表
DROP TRIGGER IF EXISTS trigerSell;
CREATE TRIGGER TrigerSell
BEFORE UPDATE ON sell
for EACH ROW
BEGIN
	IF old.sstatus=0 AND new.sstatus=1 -- 由缺货变发货 由另一个触发器处理
	then
		SET @tempinum=(SELECT inum FROM inventory WHERE ino=new.sgno);
		IF new.snum<=@tempinum
			then UPDATE inventory set inum=inum-new.snum WHERE ino=new.sgno;
		ELSEIF new.snum>@tempinum
			then set new.sstatus=2; -- 取货更新状态1变为2，修改这个必修before update
					 INSERT INTO lack 	VALUES (new.sgno,new.sgname,new.snum-@tempinum);
		end if;
	end if;
end;



-- INSERT INTO sell 	VALUES(0,2,'怡宝',20,0,now(),now(),'x00001','s00001');
-- INSERT INTO sell 	VALUES(0,1,'可口可乐',300,0,now(),now(),'x00001','s00001');
-- UPDATE sell set sstatus=1 WHERE sno=4;
-- UPDATE sell set sstatus=1 WHERE sno=5;
-- 

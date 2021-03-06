ALTER SESSION SET CURRENT_SCHEMA=CORPORATIVO;
GRANT SELECT ON CORPORATIVO.CP_MARCA_SEQ TO SIGASR;

ALTER SESSION SET CURRENT_SCHEMA=SIGASR;
CREATE TABLE "SIGASR"."SR_DISPONIBILIDADE" 
   (	
   "ID_DISPONIBILIDADE" NUMBER(19,0), 
	"HIS_ID_INI" NUMBER(19,0), 
	"HIS_DT_INI" DATE NOT NULL ENABLE, 
	"HIS_DT_FIM" DATE, 
	"DT_HR_INI" TIMESTAMP (6), 
	"DT_HR_FIM" TIMESTAMP (6), 
	"ID_ITEM_CONFIGURACAO" NUMBER(19,0) NOT NULL ENABLE, 
	"ID_ORGAO_USU" NUMBER(19,0) NOT NULL ENABLE, 
	"TP_DISPONIBILIDADE" NUMBER(1,0), 
	"MSG" VARCHAR2(255 BYTE), 
	"DET_TECNICO" VARCHAR2(255 BYTE), 
	 PRIMARY KEY ("ID_DISPONIBILIDADE"), 
	 FOREIGN KEY ("HIS_ID_INI")
	  REFERENCES "SIGASR"."SR_DISPONIBILIDADE" ("ID_DISPONIBILIDADE") ENABLE, 
	 FOREIGN KEY ("ID_ITEM_CONFIGURACAO")
	  REFERENCES "SIGASR"."SR_ITEM_CONFIGURACAO" ("ID_ITEM_CONFIGURACAO") ENABLE
 );
 
 CREATE SEQUENCE  "SIGASR"."SR_DISPONIBILIDADE_SEQ"
  MINVALUE 1 
  MAXVALUE 9999999999999999999999999999 
  INCREMENT BY 1 
  START WITH 1;
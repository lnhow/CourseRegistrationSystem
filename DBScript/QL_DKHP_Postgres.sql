--IMPORTANT!!!
--RUN THESE BEFORE OPENNING CONNECTION TO DB AND RUN THE REST
DROP DATABASE IF EXISTS "QL_DKHP";

CREATE DATABASE "QL_DKHP";
--RUN THESE BEFORE OPENNING CONNECTION TO DB AND RUN THE REST


CREATE FUNCTION TABLE_READ_ONLY() RETURNS TRIGGER
AS $$
BEGIN
	RAISE EXCEPTION 'TABLE READ ONLY. CANNOT MODIFY THIS TABLE';
END;
$$ LANGUAGE plpgsql;


--Read-only support tables---------------------------------------
--To change:
--  ALTER TABLE [TABLE NAME] DISABLE TRIGGER [TABLE NAME]_Read_only
--  MAKE CHANGES
--  ALTER TABLE [TABLE NAME] ENABLE TRIGGER [TABLE NAME]_Read_only

--Days in week
CREATE TABLE Weekdays(
	WeekdayID SERIAL PRIMARY KEY,
	WeekdayName VARCHAR(9) UNIQUE NOT NULL
);

INSERT INTO Weekdays (WeekdayName)
VALUES
	('T2'),('T3'),('T4'),('T5'),('T6'),('T7'),('CN');
	

CREATE TRIGGER Weekdays_Read_only
	BEFORE INSERT OR UPDATE OR DELETE ON Weekdays
	EXECUTE PROCEDURE TABLE_READ_ONLY();
--Days in week	

--Shifts
CREATE TABLE Shifts(
	ShiftID SERIAL PRIMARY KEY,
	ShiftStart TIME,
	ShiftEnd TIME
);

INSERT INTO Shifts (ShiftStart, ShiftEnd)
VALUES
	('7:30','9:30'),
	('9:30','11:30'),
	('13:30','15:30'),
	('15:30','17:30');

CREATE TRIGGER Shifts_Read_only
	BEFORE INSERT OR UPDATE OR DELETE ON Shifts
	EXECUTE PROCEDURE TABLE_READ_ONLY();
--Shifts

--Course Register Status
CREATE TABLE RegisterStatus(
	StatusID SERIAL PRIMARY KEY,
	StatusDesc VARCHAR(40)
);

INSERT INTO RegisterStatus (StatusDesc)
VALUES
	('Chờ duyệt'),
	('Đã duyệt'),
	('Đã hủy'),
	('Hủy bởi SV');

CREATE TRIGGER RegisterStatus_Read_only
	BEFORE INSERT OR UPDATE OR DELETE ON RegisterStatus
	EXECUTE PROCEDURE TABLE_READ_ONLY();
--Course Register Status
--Read-only support tables---------------------------------------

CREATE TABLE Account(
	AccountID BIGSERIAL PRIMARY KEY,
	Username VARCHAR(20) UNIQUE NOT NULL,
	Password VARCHAR(50) NOT NULL,
	AccountType INT DEFAULT 0	--0: student, 1: staff
	
	CONSTRAINT Account_AccountType
	CHECK (AccountType IN (0,1))
);

CREATE TABLE Subject(
	SubjectID SERIAL PRIMARY KEY,
	SubjectShort VARCHAR(10) NOT NULL,
	SubjectName VARCHAR(50),
	NumCredit INT
);

CREATE TABLE Semester(
	SemesterID SERIAL PRIMARY KEY,
	SemesterName VARCHAR(15),
	SemesterYear INT,
	SemesterStart DATE,
	SemesterEnd DATE
	
	CONSTRAINT SEMESTER_START_END
	CHECK (SemesterStart < SemesterEnd)
);

CREATE TABLE ClassInfo(
	ClassID SERIAL PRIMARY KEY,
	ClassName VARCHAR(10) UNIQUE
);


--Table Student---------------------
CREATE TABLE Student(
    StudentNo BIGSERIAL PRIMARY KEY,
    StudentID VARCHAR(10) UNIQUE,
    StudentName VARCHAR(50),
    Male BOOLEAN,
    ClassID INT,
    AccountID BIGINT UNIQUE
);

ALTER TABLE Student
ADD CONSTRAINT FK_Student_ClassInfo 
	FOREIGN KEY(ClassID) 
	REFERENCES ClassInfo(ClassID);
ALTER TABLE Student
ADD CONSTRAINT FK_Student_Account 
	FOREIGN KEY(AccountID) 
	REFERENCES Account(AccountID)
	ON DELETE CASCADE;
--Table Student---------------------

--Table Staff-----------------------
CREATE TABLE Staff(
    StaffID BIGSERIAL PRIMARY KEY,
    StaffName VARCHAR(50),
    AccountID BIGINT UNIQUE
);

ALTER TABLE Staff
ADD CONSTRAINT FK_Staff_Account
	FOREIGN KEY(AccountID)
	REFERENCES Account(AccountID)
	ON DELETE CASCADE;
--Table Staff-----------------------

--Table Course----------------------
CREATE TABLE Course(
	CourseID BIGSERIAL PRIMARY KEY,
	RoomName VARCHAR(15),
	TeacherName VARCHAR(50),
	MaxSlot INT DEFAULT 0,
	Subject INT,
	ClassID INT,
	Weekday INT,
	Shift INT,
	SemesterID INT
);

ALTER TABLE Course
ADD CONSTRAINT FK_Course_Subject
	FOREIGN KEY (Subject)
	REFERENCES Subject(SubjectID);

ALTER TABLE Course
ADD CONSTRAINT FK_Course_Class
	FOREIGN KEY (ClassID)
	REFERENCES ClassInfo(ClassID);

ALTER TABLE Course
ADD CONSTRAINT FK_Course_Weekday
	FOREIGN KEY (Weekday)
	REFERENCES Weekdays(WeekdayID);

ALTER TABLE Course
ADD CONSTRAINT FK_Course_Shift
	FOREIGN KEY (Shift)
	REFERENCES Shifts(shiftID);

ALTER TABLE Course
ADD CONSTRAINT FK_Course_Semester
	FOREIGN KEY (SemesterID)
	REFERENCES Semester(SemesterID)
	ON DELETE CASCADE;

	
--Table Course----------------------

--Table Registration Info-----------
CREATE TABLE RegistrationInfo(
	StudentID BIGINT REFERENCES Student(StudentNo) ON DELETE CASCADE,
	CourseID BIGINT REFERENCES Course(CourseID) ON DELETE CASCADE,
	RegisterTime TIMESTAMP DEFAULT now(),
	Status INT DEFAULT 1,
	Notes VARCHAR(30),
	
	PRIMARY KEY (StudentID, CourseID)
);

ALTER TABLE RegistrationInfo
ADD CONSTRAINT FK_Register_Status
	FOREIGN KEY (Status)
	REFERENCES Registerstatus(StatusID);
--Table Registration Info-----------

--RegistrationSession---------------
CREATE TABLE RegistrationSession(
	SessionID BIGSERIAL PRIMARY KEY,
	SemesterID INT NOT NULL,
	SessionStart TIMESTAMP,
	SessionEnd TIMESTAMP,
	CreatedAt TIMESTAMP DEFAULT now(),
	CreatedBy BIGINT
	
	CONSTRAINT REGISTRATION_START_END
	CHECK (SessionStart < SessionEnd)
);

ALTER TABLE RegistrationSession
ADD CONSTRAINT FK_SESSION_SEMESTER
	FOREIGN KEY (SemesterID)
	REFERENCES Semester(SemesterID)
	ON DELETE CASCADE;
	
ALTER TABLE RegistrationSession
ADD CONSTRAINT FK_SESSION_Staff
	FOREIGN KEY (CreatedBy)
	REFERENCES Staff(StaffID)
	ON DELETE SET NULL;
--RegistrationSession---------------

INSERT INTO Account(Username, Password, AccountType)
VALUES
	('admin','admin',1),
	('student','student',0);
INSERT INTO Staff(StaffName, AccountID)
VALUES
	('Admin',1);
INSERT INTO ClassInfo(ClassName)
VALUES
	('18CTT3');
INSERT INTO Student(StudentID, StudentName, Male, ClassID, AccountID)
VALUES
	('18120000','Student',TRUE,1,2);
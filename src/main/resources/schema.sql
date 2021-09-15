DROP TABLE IF EXISTS student;
CREATE TABLE student (
    id INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR UNIQUE NOT NULL,
    birthday DATE NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS course;
CREATE TABLE course (
    id INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR NOT NULL,
    available BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS enrollment;
CREATE TABLE enrollment (
    id INTEGER AUTO_INCREMENT NOT NULL,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    enrollment_date TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);

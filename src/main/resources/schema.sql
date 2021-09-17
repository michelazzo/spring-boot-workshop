DROP TABLE IF EXISTS student;
CREATE TABLE student (
    id LONG AUTO_INCREMENT NOT NULL,
    name VARCHAR NOT NULL,
    birthday DATE NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS course;
CREATE TABLE course (
    id LONG AUTO_INCREMENT NOT NULL,
    name VARCHAR NOT NULL,
    available BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS enrollment;
CREATE TABLE enrollment (
--    id LONG AUTO_INCREMENT NOT NULL,
    student_id LONG NOT NULL,
    course_id LONG NOT NULL,
    enrollment_date TIMESTAMP NOT NULL,
--    active BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);

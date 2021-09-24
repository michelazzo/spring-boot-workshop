CREATE TABLE student (
    id SERIAL NOT NULL,
    name VARCHAR NOT NULL,
    birthday DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE course (
    id SERIAL NOT NULL,
    name VARCHAR NOT NULL,
    available BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

CREATE TABLE enrollment (
--    id SERIAL NOT NULL,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    enrollment_date TIMESTAMP NOT NULL,
--    active BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);

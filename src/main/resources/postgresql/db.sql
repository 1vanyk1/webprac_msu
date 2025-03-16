BEGIN;

CREATE TABLE IF NOT EXISTS public.students
(
    student_id integer PRIMARY KEY,
    student_name character varying(20) NOT NULL,
    student_surname character varying(40) NOT NULL,
    student_patronymic character varying(30),
    student_birth_date date NOT NULL
);

CREATE TABLE IF NOT EXISTS public.courses
(
    course_id integer PRIMARY KEY,
    course_duration integer NOT NULL,
    course_name text NOT NULL,
    course_description text,
    course_ended boolean NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS public.students_courses
(
    students_student_id integer REFERENCES public.students (student_id) MATCH SIMPLE,
    course_course_id integer REFERENCES public.courses (course_id) MATCH SIMPLE,
	PRIMARY KEY (students_student_id, course_course_id)
);

CREATE TABLE IF NOT EXISTS public.universities
(
    university_id integer PRIMARY KEY,
    university_name text NOT NULL,
    university_address text NOT NULL
);


CREATE TABLE IF NOT EXISTS public.professors
(
    professor_id integer PRIMARY KEY,
    university_id integer NOT NULL REFERENCES public.universities (university_id) MATCH SIMPLE,
    professor_name character varying(20) NOT NULL,
    professor_surname character varying(40) NOT NULL,
    professor_patronymic character varying(30),
    professor_birth_date date NOT NULL
);


CREATE TABLE IF NOT EXISTS public.lecture
(
    lecture_id integer PRIMARY KEY,
    course_id integer NOT NULL REFERENCES public.courses (course_id) MATCH SIMPLE,
    professor_id integer NOT NULL REFERENCES public.professors (professor_id) MATCH SIMPLE,
    lecture_day integer NOT NULL,
    lecture_time time NOT NULL,
    lecture_duration integer NOT NULL
);

END;
BEGIN;

INSERT INTO courses(course_id, course_duration, course_name, course_ended) VALUES
	(1, 18, 'Топологии и множеста', false),
	(2, 18, 'Химия кристаллов', false),
	(3, 18, 'Строение и динамика молекул', false),
	(4, 18, 'Основы радиотехники', false),
	(5, 18, 'Монетаризм в современном мире', false),
	(6, 18, 'Свойтва и роль нейтрино в физике', false),
	(7, 18, 'Этика общения', false)
	ON CONFLICT DO NOTHING;

END;
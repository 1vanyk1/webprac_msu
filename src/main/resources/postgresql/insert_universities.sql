BEGIN;

INSERT INTO universities VALUES
	(1, 'ГУ имени Иванова', 'ул. Ивановская, д. 1'),
	(2, 'ГУ имени Воробьёва', 'ул. Морозовские горы, д.1'),
	(3, 'Технический Институт имени Дементьева', 'ул. Малая Ложка, д.6'),
	(4, 'Экономический Институт имени Милюкова', 'ул. Благовещенская, д.17'),
	(5, 'Государственный Физико-Математический Институт', 'ул. Первосентябрьская, д.1'),
	(6, 'Гуманитарный Институт имени Волкова', 'ул. Заячьи Холмы, д. 2')
	ON CONFLICT DO NOTHING;

END;
const http = require("http");

const port = Number(process.env.MOCK_1C_PORT || 8091);

const commonSchedule = [
  { day: "Пн", date: 27, lessons: [
    { time: "8:30-10:00", name: "Базы данных", room: "ауд. 304", type: "lecture" },
    { time: "10:15-11:45", name: "Web-разработка", room: "ауд. 208", type: "lab" },
  ] },
  { day: "Вт", date: 28, lessons: [
    { time: "12:00-13:30", name: "Алгоритмы", room: "ауд. 101", type: "practice" },
  ] },
  { day: "Ср", date: 29, lessons: [
    { time: "8:30-10:00", name: "Операционные системы", room: "ауд. 402", type: "lecture" },
    { time: "10:15-11:45", name: "Английский язык", room: "ауд. 215", type: "seminar" },
    { time: "13:00-14:30", name: "Проектирование ПО", room: "ауд. 303", type: "lecture" },
  ] },
  { day: "Чт", date: 30, lessons: [
    { time: "10:15-11:45", name: "Базы данных", room: "ауд. 304", type: "lab" },
    { time: "15:00-16:30", name: "Физическая культура", room: "Спортзал", type: "practice" },
  ] },
  { day: "Пт", date: 31, lessons: [
    { time: "8:30-10:00", name: "Сети и телекоммуникации", room: "ауд. 501", type: "lecture" },
    { time: "10:15-11:45", name: "Математический анализ", room: "ауд. 108", type: "seminar" },
  ] },
  { day: "Сб", date: 1, lessons: [] },
];

const students = {
  "2021-301-047": {
    profile: {
      fullName: "Иванов Алексей Игоревич",
      initials: "АИ",
      groupName: "ИТ-301",
      course: 3,
      email: "a.ivanov@student.kosygin-rgu.ru",
      phone: "+7 (916) 123-45-67",
      birthDate: "14 марта 2002",
      faculty: "Институт мехатроники и информационных технологий",
      direction: "09.03.04 Программная инженерия",
      curator: "Смирнова О.П.",
      yearIn: 2021,
      yearOut: 2025,
      recordBook: "2021-301-047",
      educationForm: "Очная, бюджет",
    },
    grades: [
      grade("grade-1", "Базы данных", "Иванов А.В.", "exam", "Экзамен", "18 янв", "5", "done"),
      grade("grade-2", "Операционные системы", "Петров С.Н.", "exam", "Экзамен", "20 янв", "4", "done"),
      grade("grade-3", "Алгоритмы и структуры данных", "Смирнова О.П.", "exam", "Экзамен", "22 янв", "5", "done"),
      grade("grade-4", "Математический анализ", "Козлов Д.Р.", "exam", "Экзамен", "25 янв", "2", "debt"),
      grade("grade-5", "Сети и телекоммуникации", "Миронов С.А.", "exam", "Экзамен", "30 янв", null, "pending"),
      grade("grade-6", "Проектирование ПО", "Лебедева Н.К.", "exam", "Экзамен", "2 фев", null, "pending"),
      grade("grade-7", "Web-разработка", "Орлов М.Р.", "credit", "Зачет", "15 янв", "зач", "done"),
      grade("grade-8", "Английский язык", "Никитина Е.В.", "credit", "Зачет", "16 янв", "зач", "done"),
    ],
    schedule: commonSchedule,
    debts: [
      debt("debt-1", "Математический анализ", "Козлов Д.Р.", "Экзамен не сдан", "14 февраля 2025, 10:00", "ауд. 108"),
      debt("debt-2", "Учебная практика", "Смирнова О.П.", "Не загружен отчет", "22 февраля 2025, 14:00", "ауд. 106"),
    ],
  },
  "2021-301-048": {
    profile: {
      fullName: "Петрова Мария Сергеевна",
      initials: "МС",
      groupName: "ИТ-301",
      course: 3,
      email: "m.petrova@student.kosygin-rgu.ru",
      phone: "+7 (925) 555-12-44",
      birthDate: "2 июня 2002",
      faculty: "Институт мехатроники и информационных технологий",
      direction: "09.03.04 Программная инженерия",
      curator: "Смирнова О.П.",
      yearIn: 2021,
      yearOut: 2025,
      recordBook: "2021-301-048",
      educationForm: "Очная, бюджет",
    },
    grades: [
      grade("grade-1", "Базы данных", "Иванов А.В.", "exam", "Экзамен", "18 янв", "5", "done"),
      grade("grade-2", "Операционные системы", "Петров С.Н.", "exam", "Экзамен", "20 янв", "5", "done"),
      grade("grade-3", "Алгоритмы и структуры данных", "Смирнова О.П.", "exam", "Экзамен", "22 янв", "4", "done"),
      grade("grade-4", "Математический анализ", "Козлов Д.Р.", "exam", "Экзамен", "25 янв", "4", "done"),
      grade("grade-5", "Сети и телекоммуникации", "Миронов С.А.", "exam", "Экзамен", "30 янв", null, "pending"),
      grade("grade-6", "Проектирование ПО", "Лебедева Н.К.", "exam", "Экзамен", "2 фев", "5", "done"),
      grade("grade-7", "Web-разработка", "Орлов М.Р.", "credit", "Зачет", "15 янв", "зач", "done"),
      grade("grade-8", "Английский язык", "Никитина Е.В.", "credit", "Зачет", "16 янв", "зач", "done"),
    ],
    schedule: commonSchedule,
    debts: [],
  },
  "2022-402-015": {
    profile: {
      fullName: "Сидоров Дмитрий Андреевич",
      initials: "ДА",
      groupName: "ДИ-402",
      course: 2,
      email: "d.sidorov@student.kosygin-rgu.ru",
      phone: "+7 (903) 812-33-19",
      birthDate: "9 ноября 2003",
      faculty: "Институт дизайна",
      direction: "54.03.01 Дизайн",
      curator: "Лебедева Н.К.",
      yearIn: 2022,
      yearOut: 2026,
      recordBook: "2022-402-015",
      educationForm: "Очная, договор",
    },
    grades: [
      grade("grade-1", "История искусств", "Федорова И.А.", "exam", "Экзамен", "19 янв", "4", "done"),
      grade("grade-2", "Композиция", "Лебедева Н.К.", "exam", "Экзамен", "21 янв", "5", "done"),
      grade("grade-3", "Цветоведение", "Орлов М.Р.", "credit", "Зачет", "15 янв", "зач", "done"),
      grade("grade-4", "Проектная графика", "Миронов С.А.", "exam", "Экзамен", "28 янв", null, "pending"),
      grade("grade-5", "Академический рисунок", "Козлов Д.Р.", "exam", "Экзамен", "30 янв", "2", "debt"),
    ],
    schedule: [
      { day: "Пн", date: 27, lessons: [
        { time: "10:15-11:45", name: "Композиция", room: "ауд. 512", type: "practice" },
        { time: "12:00-13:30", name: "Цветоведение", room: "ауд. 407", type: "seminar" },
      ] },
      { day: "Вт", date: 28, lessons: [
        { time: "8:30-10:00", name: "История искусств", room: "ауд. 214", type: "lecture" },
      ] },
      { day: "Ср", date: 29, lessons: [
        { time: "13:00-14:30", name: "Проектная графика", room: "ауд. 508", type: "lab" },
      ] },
      { day: "Чт", date: 30, lessons: [] },
      { day: "Пт", date: 31, lessons: [
        { time: "10:15-11:45", name: "Академический рисунок", room: "мастерская 2", type: "practice" },
      ] },
      { day: "Сб", date: 1, lessons: [] },
    ],
    debts: [
      debt("debt-1", "Академический рисунок", "Козлов Д.Р.", "Не сдан итоговый просмотр", "17 февраля 2025, 12:00", "мастерская 2"),
    ],
  },
  "2020-104-033": {
    profile: {
      fullName: "Кузнецова Анна Павловна",
      initials: "АП",
      groupName: "ТЛ-104",
      course: 4,
      email: "a.kuznetsova@student.kosygin-rgu.ru",
      phone: "+7 (977) 404-88-10",
      birthDate: "21 апреля 2001",
      faculty: "Институт текстиля и легкой промышленности",
      direction: "29.03.01 Технология изделий легкой промышленности",
      curator: "Никитина Е.В.",
      yearIn: 2020,
      yearOut: 2024,
      recordBook: "2020-104-033",
      educationForm: "Заочная, договор",
    },
    grades: [
      grade("grade-1", "Материаловедение", "Никитина Е.В.", "exam", "Экзамен", "17 янв", "4", "done"),
      grade("grade-2", "Конструирование изделий", "Миронов С.А.", "exam", "Экзамен", "23 янв", "5", "done"),
      grade("grade-3", "Технология швейных изделий", "Петров С.Н.", "exam", "Экзамен", "26 янв", "4", "done"),
      grade("grade-4", "Экономика производства", "Иванов А.В.", "credit", "Зачет", "14 янв", "зач", "done"),
      grade("grade-5", "Производственная практика", "Лебедева Н.К.", "credit", "Зачет", "3 фев", null, "pending"),
    ],
    schedule: [
      { day: "Пн", date: 27, lessons: [] },
      { day: "Вт", date: 28, lessons: [
        { time: "18:30-20:00", name: "Материаловедение", room: "ауд. 301", type: "lecture" },
      ] },
      { day: "Ср", date: 29, lessons: [
        { time: "18:30-20:00", name: "Конструирование изделий", room: "ауд. 318", type: "practice" },
      ] },
      { day: "Чт", date: 30, lessons: [] },
      { day: "Пт", date: 31, lessons: [
        { time: "18:30-20:00", name: "Технология швейных изделий", room: "лаборатория 4", type: "lab" },
      ] },
      { day: "Сб", date: 1, lessons: [
        { time: "10:15-11:45", name: "Производственная практика", room: "ауд. 116", type: "seminar" },
      ] },
    ],
    debts: [],
  },
};

function grade(id, subject, teacher, type, typeLabel, date, value, status) {
  return { id, subject, teacher, type, typeLabel, date, grade: value, status };
}

function debt(id, subject, teacher, reason, retakeDate, location) {
  return { id, subject, teacher, reason, retakeDate, location };
}

function send(response, status, payload) {
  const body = JSON.stringify(payload);
  response.writeHead(status, {
    "Content-Type": "application/json; charset=utf-8",
    "Content-Length": Buffer.byteLength(body),
  });
  response.end(body);
}

const server = http.createServer((request, response) => {
  const match = request.url.match(/^\/onec\/students\/([^/]+)\/(profile|grades|schedule|debts)$/);

  if (request.method !== "GET" || !match) {
    send(response, 404, { error: "Not found" });
    return;
  }

  const student = students[match[1]];
  if (!student) {
    send(response, 404, { error: "Student not found" });
    return;
  }

  send(response, 200, student[match[2]]);
});

server.listen(port, "127.0.0.1", () => {
  console.log(`mock-1c is running at http://127.0.0.1:${port}`);
  console.log(`demo students: ${Object.keys(students).join(", ")}`);
});

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

function sendHtml(response, status, html) {
  const body = Buffer.from(html, "utf-8");
  response.writeHead(status, {
    "Content-Type": "text/html; charset=utf-8",
    "Content-Length": body.length,
  });
  response.end(body);
}

function readJsonBody(request) {
  return new Promise((resolve, reject) => {
    let raw = "";
    request.on("data", (chunk) => {
      raw += chunk;
      if (raw.length > 1_000_000) {
        reject(new Error("Payload too large"));
        request.destroy();
      }
    });
    request.on("end", () => {
      if (!raw) {
        resolve({});
        return;
      }
      try {
        resolve(JSON.parse(raw));
      } catch (error) {
        reject(error);
      }
    });
    request.on("error", reject);
  });
}

let nextGradeId = 1000;

function adminPage() {
  const options = Object.entries(students)
    .map(([id, data]) => `<option value="${id}">${id} — ${data.profile.fullName}</option>`)
    .join("");

  return `<!doctype html>
<html lang="ru">
<head>
<meta charset="utf-8" />
<title>mock-1c — управление оценками</title>
<style>
  body { font-family: system-ui, sans-serif; max-width: 920px; margin: 24px auto; padding: 0 16px; color: #1a1a1a; }
  h1 { font-size: 20px; }
  table { width: 100%; border-collapse: collapse; margin: 16px 0; }
  th, td { border: 1px solid #ddd; padding: 6px 8px; font-size: 13px; }
  th { background: #f5f5f5; text-align: left; }
  input, select { width: 100%; box-sizing: border-box; font-size: 13px; padding: 4px; }
  button { padding: 6px 12px; cursor: pointer; }
  .row-actions { display: flex; gap: 6px; }
  .add-form { display: grid; grid-template-columns: repeat(6, 1fr) auto; gap: 6px; align-items: end; margin-top: 8px; }
  .add-form label { font-size: 11px; color: #666; display: block; }
  #status { margin: 10px 0; min-height: 20px; color: #0a7d2e; font-size: 13px; }
  #status.error { color: #c0392b; }
</style>
</head>
<body>
<h1>mock-1c — оценки студента</h1>
<p>Измените оценки здесь, затем нажмите «Синхронизировать с 1С» в личном кабинете студента,
чтобы изменения попали в базу данных бэкенда.</p>

<label for="student">Студент</label>
<select id="student">${options}</select>

<div id="status"></div>

<table>
  <thead>
    <tr>
      <th>Дисциплина</th><th>Преподаватель</th><th>Тип</th><th>Название типа</th>
      <th>Дата</th><th>Оценка</th><th>Статус</th><th></th>
    </tr>
  </thead>
  <tbody id="grades-body"></tbody>
</table>

<h2>Добавить оценку</h2>
<form class="add-form" id="add-form">
  <div><label>Дисциплина</label><input name="subject" required /></div>
  <div><label>Преподаватель</label><input name="teacher" /></div>
  <div><label>Тип</label>
    <select name="type"><option value="exam">Экзамен</option><option value="credit">Зачет</option></select>
  </div>
  <div><label>Название типа</label><input name="typeLabel" value="Экзамен" /></div>
  <div><label>Дата</label><input name="date" placeholder="напр. 5 мар" /></div>
  <div><label>Оценка</label><input name="grade" placeholder="5 / зач / пусто" /></div>
  <div><label>Статус</label>
    <select name="status">
      <option value="done">Сдано</option>
      <option value="pending">Предстоит</option>
      <option value="debt">Долг</option>
    </select>
  </div>
  <div style="grid-column: 1 / -1;"><button type="submit">Добавить оценку</button></div>
</form>

<script>
const studentSelect = document.getElementById("student");
const body = document.getElementById("grades-body");
const statusEl = document.getElementById("status");

function showStatus(message, isError) {
  statusEl.textContent = message;
  statusEl.className = isError ? "error" : "";
}

async function loadGrades() {
  const id = studentSelect.value;
  const response = await fetch(\`/onec/students/\${id}/grades\`);
  const grades = await response.json();
  body.innerHTML = "";
  for (const item of grades) {
    body.appendChild(renderRow(id, item));
  }
}

function renderRow(studentId, item) {
  const tr = document.createElement("tr");
  tr.innerHTML = \`
    <td><input data-field="subject" value="\${item.subject || ""}" /></td>
    <td><input data-field="teacher" value="\${item.teacher || ""}" /></td>
    <td>
      <select data-field="type">
        <option value="exam" \${item.type === "exam" ? "selected" : ""}>Экзамен</option>
        <option value="credit" \${item.type === "credit" ? "selected" : ""}>Зачет</option>
      </select>
    </td>
    <td><input data-field="typeLabel" value="\${item.typeLabel || ""}" /></td>
    <td><input data-field="date" value="\${item.date || ""}" /></td>
    <td><input data-field="grade" value="\${item.grade || ""}" /></td>
    <td>
      <select data-field="status">
        <option value="done" \${item.status === "done" ? "selected" : ""}>Сдано</option>
        <option value="pending" \${item.status === "pending" ? "selected" : ""}>Предстоит</option>
        <option value="debt" \${item.status === "debt" ? "selected" : ""}>Долг</option>
      </select>
    </td>
    <td class="row-actions">
      <button data-action="save">Сохранить</button>
      <button data-action="delete">Удалить</button>
    </td>
  \`;

  tr.querySelector('[data-action="save"]').addEventListener("click", async () => {
    const payload = {};
    tr.querySelectorAll("[data-field]").forEach((el) => {
      payload[el.dataset.field] = el.value;
    });
    const response = await fetch(\`/onec/students/\${studentId}/grades/\${item.id}\`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    if (response.ok) {
      showStatus("Сохранено в mock-1c. Теперь синхронизируйте кабинет.", false);
    } else {
      showStatus("Ошибка сохранения", true);
    }
  });

  tr.querySelector('[data-action="delete"]').addEventListener("click", async () => {
    const response = await fetch(\`/onec/students/\${studentId}/grades/\${item.id}\`, { method: "DELETE" });
    if (response.ok) {
      showStatus("Оценка удалена из mock-1c. Синхронизируйте кабинет.", false);
      loadGrades();
    } else {
      showStatus("Ошибка удаления", true);
    }
  });

  return tr;
}

document.getElementById("add-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const form = event.target;
  const payload = Object.fromEntries(new FormData(form).entries());
  const response = await fetch(\`/onec/students/\${studentSelect.value}/grades\`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  if (response.ok) {
    showStatus("Оценка добавлена в mock-1c. Синхронизируйте кабинет.", false);
    form.reset();
    loadGrades();
  } else {
    showStatus("Ошибка добавления", true);
  }
});

studentSelect.addEventListener("change", loadGrades);
loadGrades();
</script>
</body>
</html>`;
}

const server = http.createServer(async (request, response) => {
  const url = new URL(request.url, `http://${request.headers.host}`);
  const path = url.pathname;

  if (request.method === "GET" && path === "/admin") {
    sendHtml(response, 200, adminPage());
    return;
  }

  if (request.method === "GET" && path === "/onec/students") {
    const list = Object.entries(students).map(([id, data]) => ({ id, fullName: data.profile.fullName }));
    send(response, 200, list);
    return;
  }

  const resourceMatch = path.match(/^\/onec\/students\/([^/]+)\/(profile|grades|schedule|debts)$/);
  const gradeMatch = path.match(/^\/onec\/students\/([^/]+)\/grades\/([^/]+)$/);

  if (request.method === "GET" && resourceMatch) {
    const student = students[resourceMatch[1]];
    if (!student) {
      send(response, 404, { error: "Student not found" });
      return;
    }
    send(response, 200, student[resourceMatch[2]]);
    return;
  }

  if (request.method === "POST" && resourceMatch && resourceMatch[2] === "grades") {
    const student = students[resourceMatch[1]];
    if (!student) {
      send(response, 404, { error: "Student not found" });
      return;
    }
    let payload;
    try {
      payload = await readJsonBody(request);
    } catch {
      send(response, 400, { error: "Invalid JSON" });
      return;
    }
    const newGrade = grade(
      `grade-${nextGradeId++}`,
      payload.subject || "",
      payload.teacher || "",
      payload.type || "exam",
      payload.typeLabel || "",
      payload.date || "",
      payload.grade || null,
      payload.status || "pending",
    );
    student.grades.push(newGrade);
    send(response, 201, newGrade);
    return;
  }

  if ((request.method === "PUT" || request.method === "DELETE") && gradeMatch) {
    const student = students[gradeMatch[1]];
    if (!student) {
      send(response, 404, { error: "Student not found" });
      return;
    }
    const gradeIndex = student.grades.findIndex((item) => item.id === gradeMatch[2]);
    if (gradeIndex === -1) {
      send(response, 404, { error: "Grade not found" });
      return;
    }

    if (request.method === "DELETE") {
      const [removed] = student.grades.splice(gradeIndex, 1);
      send(response, 200, removed);
      return;
    }

    let payload;
    try {
      payload = await readJsonBody(request);
    } catch {
      send(response, 400, { error: "Invalid JSON" });
      return;
    }
    const existing = student.grades[gradeIndex];
    student.grades[gradeIndex] = {
      ...existing,
      subject: payload.subject ?? existing.subject,
      teacher: payload.teacher ?? existing.teacher,
      type: payload.type ?? existing.type,
      typeLabel: payload.typeLabel ?? existing.typeLabel,
      date: payload.date ?? existing.date,
      grade: payload.grade === "" ? null : payload.grade ?? existing.grade,
      status: payload.status ?? existing.status,
    };
    send(response, 200, student.grades[gradeIndex]);
    return;
  }

  send(response, 404, { error: "Not found" });
});

server.listen(port, "0.0.0.0", () => {
  console.log(`mock-1c is running at http://0.0.0.0:${port}`);
  console.log(`admin UI: http://localhost:${port}/admin`);
  console.log(`demo students: ${Object.keys(students).join(", ")}`);
});

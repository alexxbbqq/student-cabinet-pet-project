import { useEffect, useMemo, useState } from "react";

const API_BASE = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const api = {
  async login(login, password) {
    return request("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ login, password }),
    });
  },
  profile: (token) => request("/api/student/profile", {}, token),
  grades: (token) => request("/api/student/grades", {}, token),
  schedule: (token) => request("/api/student/schedule", {}, token),
  debts: (token) => request("/api/student/debts", {}, token),
};

async function request(path, options = {}, token) {
  const headers = {
    ...(options.headers || {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };

  const response = await fetch(`${API_BASE}${path}`, { ...options, headers });
  if (!response.ok) {
    throw new Error(`API error ${response.status}`);
  }
  return response.json();
}

function Logo() {
  return <div className="logo-mark">К</div>;
}

function LoginPage({ onLogin }) {
  const [login, setLogin] = useState("2021-301-047");
  const [password, setPassword] = useState("password");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);
    try {
      const session = await api.login(login, password);
      onLogin(session);
    } catch {
      setError("Не удалось войти. Проверьте логин, пароль и доступность backend на localhost:8080.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-brand">
        <div className="brand-row">
          <Logo />
          <div>
            <strong>РГУ им. А.Н. Косыгина</strong>
            <span>Личный кабинет студента</span>
          </div>
        </div>
        <h1>Личный кабинет студента</h1>
        <p>Оценки, расписание, задолженности и профиль студента.</p>
      </section>

      <section className="login-panel">
        <form className="login-card" onSubmit={submit}>
          <h2>Вход в систему</h2>

          <label htmlFor="login">Студенческий билет или email</label>
          <input id="login" value={login} onChange={(event) => setLogin(event.target.value)} />

          <label htmlFor="password">Пароль</label>
          <div className="password-field">
            <input
              id="password"
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />
            <button
              type="button"
              className={`password-toggle ${showPassword ? "visible" : ""}`}
              aria-label={showPassword ? "Скрыть пароль" : "Показать пароль"}
              title={showPassword ? "Скрыть пароль" : "Показать пароль"}
              onClick={() => setShowPassword((value) => !value)}
            >
              <span className="eye-icon" aria-hidden="true" />
            </button>
          </div>

          {error && <div className="form-error">{error}</div>}
          <button type="submit" disabled={loading}>{loading ? "Входим..." : "Войти"}</button>
        </form>
      </section>
    </main>
  );
}

function AppShell({ session, onLogout }) {
  const [page, setPage] = useState("grades");
  const [data, setData] = useState({ profile: null, grades: [], schedule: [], debts: [] });
  const [status, setStatus] = useState("loading");
  const [refreshing, setRefreshing] = useState(false);

  function loadData() {
    return Promise.all([api.profile(session.token), api.grades(session.token), api.schedule(session.token), api.debts(session.token)])
      .then(([profile, grades, schedule, debts]) => {
        setData({ profile, grades, schedule, debts });
        setStatus("ready");
      })
      .catch(() => setStatus("error"));
  }

  useEffect(() => {
    loadData();
  }, [session.token]);

  async function refresh() {
    setRefreshing(true);
    try {
      await loadData();
    } finally {
      setRefreshing(false);
    }
  }

  const pages = {
    grades: <GradesPage grades={data.grades} />,
    schedule: <SchedulePage schedule={data.schedule} />,
    debts: <DebtsPage debts={data.debts} />,
    profile: <ProfilePage profile={data.profile} grades={data.grades} />,
  };

  const titles = {
    grades: "Оценки",
    schedule: "Расписание",
    debts: "Задолженности",
    profile: "Профиль",
  };

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="sidebar-header">
          <Logo />
          <div>
            <strong>РГУ им. А.Н. Косыгина</strong>
            <span>Студенческий портал</span>
          </div>
        </div>

        <div className="student-mini">
          <div className="avatar">{data.profile?.initials || "С"}</div>
          <div>
            <strong>{data.profile?.fullName?.split(" ").slice(0, 2).join(" ") || "Студент"}</strong>
            <span>{data.profile?.groupName || "Группа"}</span>
          </div>
        </div>

        <nav>
          <button className={page === "grades" ? "active" : ""} onClick={() => setPage("grades")}>Оценки</button>
          <button className={page === "schedule" ? "active" : ""} onClick={() => setPage("schedule")}>Расписание</button>
          <button className={page === "debts" ? "active" : ""} onClick={() => setPage("debts")}>
            Задолженности {data.debts.length > 0 && <span>{data.debts.length}</span>}
          </button>
          <button className={page === "profile" ? "active" : ""} onClick={() => setPage("profile")}>Профиль</button>
        </nav>

        <button className="logout" onClick={onLogout}>Выйти</button>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <h1>{titles[page]}</h1>
          <div className="topbar-actions">
            <button className="sync-button" onClick={refresh} disabled={refreshing}>
              {refreshing ? "Обновление..." : "Обновить"}
            </button>
            <div className="sync-state">{status === "ready" ? "Данные обновлены" : status === "error" ? "Ошибка подключения" : "Подключение"}</div>
          </div>
        </header>

        <main className="content">
          {status === "loading" && <StateCard title="Загружаем данные" text="Получаем профиль, оценки и расписание." />}
          {status === "error" && <StateCard title="Backend недоступен" text="Запустите student-api на порту 8080, затем обновите страницу." />}
          {status === "ready" && pages[page]}
        </main>
      </section>
    </div>
  );
}

function StateCard({ title, text }) {
  return (
    <div className="state-card">
      <h2>{title}</h2>
      <p>{text}</p>
    </div>
  );
}

function GradesPage({ grades }) {
  const [filter, setFilter] = useState("all");
  const [search, setSearch] = useState("");

  const stats = useMemo(() => {
    const numeric = grades.map((item) => Number(item.grade)).filter((grade) => grade > 2);
    const gpa = numeric.length ? (numeric.reduce((sum, grade) => sum + grade, 0) / numeric.length).toFixed(1) : "-";
    return {
      gpa,
      debts: grades.filter((item) => item.status === "debt").length,
      exams: `${grades.filter((item) => item.type === "exam" && item.status === "done").length}/${grades.filter((item) => item.type === "exam").length}`,
      credits: grades.filter((item) => item.type === "credit" && item.status === "done").length,
    };
  }, [grades]);

  const visible = grades.filter((item) => {
    const matchesFilter = filter === "all" || item.type === filter || item.status === filter;
    const matchesSearch = item.subject.toLowerCase().includes(search.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  return (
    <>
      <div className="stats-grid">
        <StatCard label="Средний балл" value={stats.gpa} />
        <StatCard label="Задолженности" value={stats.debts} tone={stats.debts ? "danger" : "good"} />
        <StatCard label="Экзамены" value={stats.exams} />
        <StatCard label="Зачеты" value={stats.credits} />
      </div>

      <div className="toolbar">
        <div className="tabs">
          <button className={filter === "all" ? "active" : ""} onClick={() => setFilter("all")}>Все</button>
          <button className={filter === "exam" ? "active" : ""} onClick={() => setFilter("exam")}>Экзамены</button>
          <button className={filter === "credit" ? "active" : ""} onClick={() => setFilter("credit")}>Зачеты</button>
          <button className={filter === "pending" ? "active" : ""} onClick={() => setFilter("pending")}>Предстоит</button>
        </div>
        <input className="search" placeholder="Поиск дисциплины" value={search} onChange={(event) => setSearch(event.target.value)} />
      </div>

      <div className="table-card">
        <div className="grade-row grade-head">
          <span>Дисциплина</span>
          <span>Тип</span>
          <span>Дата</span>
          <span>Оценка</span>
          <span>Статус</span>
        </div>
        {visible.map((item) => (
          <div className="grade-row" key={item.id}>
            <div>
              <strong>{item.subject}</strong>
              <small>{item.teacher}</small>
            </div>
            <span>{item.typeLabel}</span>
            <span>{item.date}</span>
            <GradeBadge grade={item.grade} />
            <StatusBadge status={item.status} />
          </div>
        ))}
      </div>
    </>
  );
}

function StatCard({ label, value, tone }) {
  return (
    <div className={`stat-card ${tone || ""}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}

function GradeBadge({ grade }) {
  if (!grade) return <span className="grade-badge muted">-</span>;
  return <span className={`grade-badge grade-${grade}`}>{grade}</span>;
}

function StatusBadge({ status }) {
  const labels = { done: "Сдано", pending: "Предстоит", debt: "Долг" };
  return <span className={`status-badge ${status}`}>{labels[status] || status}</span>;
}

function SchedulePage({ schedule }) {
  return (
    <div className="schedule-grid">
      {schedule.map((day) => (
        <section className="day-card" key={day.day}>
          <header>
            <strong>{day.day}</strong>
            <span>{day.date}</span>
          </header>
          {day.lessons.length === 0 && <p className="empty">Пар нет</p>}
          {day.lessons.map((lesson) => (
            <article className={`lesson ${lesson.type}`} key={`${day.day}-${lesson.time}-${lesson.name}`}>
              <time>{lesson.time}</time>
              <strong>{lesson.name}</strong>
              <span>{lesson.room}</span>
            </article>
          ))}
        </section>
      ))}
    </div>
  );
}

function DebtsPage({ debts }) {
  if (debts.length === 0) {
    return <StateCard title="Задолженностей нет" text="Все контрольные точки закрыты." />;
  }
  return (
    <div className="debt-list">
      {debts.map((debt) => (
        <article className="debt-card" key={debt.id}>
          <div>
            <strong>{debt.subject}</strong>
            <span>{debt.teacher}</span>
            <p>{debt.reason}</p>
          </div>
          <div className="retake">
            <span>{debt.retakeDate}</span>
            <small>{debt.location}</small>
          </div>
        </article>
      ))}
    </div>
  );
}

function ProfilePage({ profile, grades }) {
  const average = useMemo(() => {
    const numeric = grades.map((item) => Number(item.grade)).filter((grade) => grade > 2);
    return numeric.length ? (numeric.reduce((sum, grade) => sum + grade, 0) / numeric.length).toFixed(1) : "-";
  }, [grades]);

  if (!profile) return null;

  const personal = [
    ["ФИО", profile.fullName],
    ["Дата рождения", profile.birthDate],
    ["Email", profile.email],
    ["Телефон", profile.phone],
  ];
  const education = [
    ["Факультет", profile.faculty],
    ["Направление", profile.direction],
    ["Группа", profile.groupName],
    ["Куратор", profile.curator],
    ["Год поступления", profile.yearIn],
    ["Год выпуска", profile.yearOut],
  ];

  return (
    <div className="profile-grid">
      <aside className="profile-card">
        <div className="avatar large">{profile.initials}</div>
        <h2>{profile.fullName}</h2>
        <p>{profile.groupName}, {profile.course} курс</p>
        <Metric label="Средний балл" value={average} />
        <Metric label="Зачетная книжка" value={profile.recordBook} />
        <Metric label="Форма обучения" value={profile.educationForm} />
      </aside>
      <div className="info-stack">
        <InfoTable title="Личная информация" rows={personal} />
        <InfoTable title="Учебная информация" rows={education} />
      </div>
    </div>
  );
}

function Metric({ label, value }) {
  return (
    <div className="metric">
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}

function InfoTable({ title, rows }) {
  return (
    <section className="info-table">
      <h2>{title}</h2>
      {rows.map(([label, value]) => (
        <div key={label}>
          <span>{label}</span>
          <strong>{value}</strong>
        </div>
      ))}
    </section>
  );
}

export default function App() {
  const [session, setSession] = useState(() => {
    const saved = localStorage.getItem("student-session");
    return saved ? JSON.parse(saved) : null;
  });

  function login(sessionData) {
    localStorage.setItem("student-session", JSON.stringify(sessionData));
    setSession(sessionData);
  }

  function logout() {
    localStorage.removeItem("student-session");
    setSession(null);
  }

  return session ? <AppShell session={session} onLogout={logout} /> : <LoginPage onLogin={login} />;
}

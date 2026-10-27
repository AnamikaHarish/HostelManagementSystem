-- Enable foreign key constraint support in SQLite
PRAGMA foreign_keys = ON;

-- Student Table
CREATE TABLE IF NOT EXISTS student (
    student_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    semester INTEGER NOT NULL,
    phone VARCHAR(15) NOT NULL
);

-- Room Table
CREATE TABLE IF NOT EXISTS room (
    room_id INTEGER PRIMARY KEY AUTOINCREMENT,
    block VARCHAR(10) NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    capacity INTEGER NOT NULL,
    occupied_beds INTEGER DEFAULT 0
);

-- Allocation Table
CREATE TABLE IF NOT EXISTS allocation (
    allocation_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id VARCHAR(50) NOT NULL,
    room_id INTEGER NOT NULL,
    allocation_date TEXT NOT NULL,
    checkout_date TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (room_id) REFERENCES room(room_id)
);
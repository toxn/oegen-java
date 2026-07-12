CREATE TABLE IF NOT EXISTS persons (
    id TEXT PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    gender TEXT,
    father_id TEXT REFERENCES persons (id) ON DELETE SET NULL,
    mother_id TEXT REFERENCES persons (id) ON DELETE SET NULL,
    extra_json TEXT
);

CREATE INDEX IF NOT EXISTS idx_persons_father ON persons (father_id);
CREATE INDEX IF NOT EXISTS idx_persons_mother ON persons (mother_id);

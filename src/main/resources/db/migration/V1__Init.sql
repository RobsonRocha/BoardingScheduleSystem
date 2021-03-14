CREATE TABLE IF NOT EXISTS enterprise (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(150)
);

CREATE INDEX IF NOT EXISTS idx_enterprise ON enterprise (name);

CREATE TABLE IF NOT EXISTS employee (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(150),
  role VARCHAR(150),
  enterprise_id int,
  CONSTRAINT fk_enterprise
      FOREIGN KEY(enterprise_id)
  	  REFERENCES enterprise(id)
);

CREATE INDEX IF NOT EXISTS idx_employee ON employee (name);


CREATE TABLE IF NOT EXISTS board_schedule (
  id   SERIAL PRIMARY KEY,
  employee_id int,
  init_date timestamp,
  end_date timestamp,
  CONSTRAINT fk_employee
      FOREIGN KEY(employee_id)
  	  REFERENCES employee(id)
);





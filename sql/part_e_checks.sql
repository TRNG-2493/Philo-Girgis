-- ============================================================================
-- Part E - The data-quality validation suite (time-box: 35 min)  /  Project 1 milestone
-- File format (the Python code in Part F reads exactly this format):
--   -- @CHECK <id> | <category> | <what must be true or what is wrong> | returns <table>.<pk column>
--   one SQL statement that returns the VIOLATING rows: first column = primary key of the offending row.
--   Zero rows returned = the check PASSES.  One or more rows = the check FAILS (those rows are the evidence).
-- Run:  python lab_runner.py --checks sql/part_e_checks.sql [--only E-05]
-- Three checks (E-14, E-16, E-20) are already written as worked examples. Write the other 17.
-- Placeholders look like:  SELECT NULL AS violating_id WHERE 1 = 0;   (they return 0 rows, so they "pass" silently:
-- a check you have not written is NOT a passing check. lab_runner.py reports them as TODO.)
-- Rules of the game:
--   * Return one row per OFFENDING ROW of the table named in "returns" (no duplicates, no extra columns needed).
--   * Return the primary key ONLY once per offending row (use DISTINCT or an OR condition, not a join that repeats rows).
--   * Every check must be a SELECT and must be safe to run any time.
-- ============================================================================

-- ---- Category: row counts and reconciliation -------------------------------

-- @CHECK E-01 | reconciliation | Build summary: reported_total equals the real number of runs | returns builds.build_id
-- (technique: Part D5)
-- TODO: write the violation query below and delete the placeholder
SELECT b.build_id
from builds b left join test_runs r on b.build_id = r.build_id
group by b.build_id having reported_total <> COUNT(run_id);

-- @CHECK E-02 | reconciliation | Build summary: reported_passed and reported_failed equal the real counts | returns builds.build_id
-- (technique: D5 + conditional aggregation from D1. Count results exactly 'pass' / 'fail'. A build with no runs has 0 of each.)
-- TODO: write the violation query below and delete the placeholder
SELECT b.build_id
from builds b left join test_runs r on b.build_id = r.build_id
group by b.build_id
having SUM(CASE WHEN result = 'pass' THEN 1 ELSE 0 END) <> reported_passed or
SUM(CASE WHEN result = 'fail' THEN 1 ELSE 0 END) <> reported_failed;

-- @CHECK E-03 | missing rows | Every build has at least one test run | returns builds.build_id
-- (technique: B3)
-- TODO: write the violation query below and delete the placeholder
select b.build_id from builds b
left join test_runs r on b.build_id = r.build_id
group by b.build_id having count(run_id) < 1;

-- @CHECK E-04 | missing rows | Every ACTIVE test case was executed at least once | returns test_cases.case_id
-- (technique: C3, plus a status filter. Retired cases are allowed to have no runs.)
-- TODO: write the violation query below and delete the placeholder
SELECT c.case_id from test_cases c
left join test_runs r on r.case_id = c.case_id
where status like "ACTIVE"
group by c.case_id having count(r.case_id) < 1;

-- ---- Category: uniqueness ---------------------------------------------------

-- @CHECK E-05 | uniqueness | One run per (build, case): return the SURPLUS rows, keep the lowest run_id | returns test_runs.run_id
-- (technique: D2 as a subquery. Rows that are NOT the MIN(run_id) of their (build_id, case_id) group are surplus.)
-- TODO: write the violation query below and delete the placeholder
select run_id from test_runs
where build_id in (select build_id from test_runs group by build_id, case_id having count(*) > 1)
and case_id in (select case_id from test_runs group by build_id, case_id having count(*) > 1)
and run_id not in (select min(run_id) from test_runs group by build_id, case_id having count(*) > 1);

-- @CHECK E-06 | uniqueness | A test case title must be unique within its module: return surplus cases, keep the lowest case_id | returns test_cases.case_id
-- TODO: write the violation query below and delete the placeholder
select case_id from test_cases
where title in (select title from test_cases group by module, title having count(*) > 1)
and module in (select module from test_cases group by module, title having count(*) > 1)
and case_id not in (select min(case_id) from test_cases group by module, title having count(*) > 1);

-- ---- Category: completeness -------------------------------------------------

-- @CHECK E-07 | completeness | Test case priority is present and one of P1, P2, P3 | returns test_cases.case_id
-- TODO: write the violation query below and delete the placeholder
SELECT case_id from test_cases
where priority is null or not priority in ("P1", "P2", "P3");

-- @CHECK E-08 | completeness | Every run has case_id, a non-blank tester, a result and a duration_sec | returns test_runs.run_id
-- (watch out: "missing" can be NULL or blank text. Four columns, one row per offending run.)
-- TODO: write the violation query below and delete the placeholder
SELECT run_id from test_runs
where case_id is null or
tester is null or trim(tester) like "" or
result is null or trim(result) like "" or
duration_sec is null;

-- @CHECK E-09 | completeness + validity | Defect severity is present and one of low, medium, high, critical | returns defects.defect_id
-- TODO: write the violation query below and delete the placeholder
SELECT defect_id from defects
where severity is null or trim(severity) like ""
or not severity in ("low", "medium", "high", "critical");

-- ---- Category: referential integrity (what PRAGMA foreign_key_check would find, but readable) -------

-- @CHECK E-10 | referential | test_runs.case_id (when given) exists in test_cases | returns test_runs.run_id
-- (a NULL case_id is a COMPLETENESS problem (E-08), not an orphan. Do not report it here.)
-- TODO: write the violation query below and delete the placeholder
SELECT r.run_id
FROM test_runs AS r
LEFT JOIN test_cases AS c ON c.case_id = r.case_id
WHERE r.case_id IS NOT NULL AND c.case_id IS NULL;

-- @CHECK E-11 | referential | test_runs.build_id exists in builds | returns test_runs.run_id
-- TODO: write the violation query below and delete the placeholder
SELECT run_id from test_runs r
left join builds b on b.build_id = r.build_id
where b.build_id is null;

-- @CHECK E-12 | referential | defects.run_id (when given) exists in test_runs | returns defects.defect_id
-- TODO: write the violation query below and delete the placeholder
SELECT defect_id from defects d
left join test_runs r on r.run_id = d.run_id
where not d.run_id is null and r.run_id is null;

-- @CHECK E-13 | referential | defects.case_id (when given) exists in test_cases | returns defects.defect_id
-- TODO: write the violation query below and delete the placeholder
SELECT defect_id from defects d
left join test_cases c on c.case_id = d.case_id
where d.case_id is not null and c.case_id is null;

-- ---- Category: validity (domains and ranges) --------------------------------

-- @CHECK E-14 | validity | Run result is one of pass, fail, skip (NULL is reported by E-08, not here) | returns test_runs.run_id
-- (WORKED EXAMPLE: already complete)
SELECT run_id
FROM test_runs
WHERE result IS NOT NULL AND result NOT IN ('pass', 'fail', 'skip');

-- @CHECK E-15 | validity | Run duration_sec is between 1 and 3600 (NULL is reported by E-08, not here) | returns test_runs.run_id
-- TODO: write the violation query below and delete the placeholder
SELECT run_id from test_runs
where not duration_sec between 1 and 3600;

-- ---- Category: cross-table consistency and business rules -------------------

-- @CHECK E-16 | business rule | A run cannot happen before its build was created (run_date >= built_date) | returns test_runs.run_id
-- (WORKED EXAMPLE: already complete. ISO dates compare correctly as text.)
SELECT r.run_id
FROM test_runs AS r
INNER JOIN builds AS b ON b.build_id = r.build_id
WHERE r.run_date < b.built_date;

-- @CHECK E-17 | business rule | Every FAILED run has at least one defect | returns test_runs.run_id
-- TODO: write the violation query below and delete the placeholder
SELECT r.run_id from test_runs r
left join defects d on d.run_id = r.run_id
where result = "fail" and d.run_id is null;

-- @CHECK E-18 | business rule | A defect that references a run must reference a FAILED run | returns defects.defect_id
-- (technique: C5. Defects whose run does not exist are E-12, not this check.)
-- TODO: write the violation query below and delete the placeholder
SELECT defect_id from defects d
inner join test_runs r on r.run_id = d.run_id
where not result = "fail";

-- @CHECK E-19 | cross-table consistency | defects.case_id equals the case_id of the run it references | returns defects.defect_id
-- (only for defects whose run exists and where both case ids are present)
-- TODO: write the violation query below and delete the placeholder
select defect_id from defects d
left join test_runs r on r.run_id = d.run_id
where d.case_id <> r.case_id;


-- @CHECK E-20 | business rule | Defect dates and status agree: closed_date >= opened_date; status fixed/closed needs a closed_date; status open must have none | returns defects.defect_id
-- (WORKED EXAMPLE: already complete)
SELECT defect_id
FROM defects
WHERE (closed_date IS NOT NULL AND closed_date < opened_date)
   OR (status IN ('fixed', 'closed') AND closed_date IS NULL)
   OR (status = 'open' AND closed_date IS NOT NULL);

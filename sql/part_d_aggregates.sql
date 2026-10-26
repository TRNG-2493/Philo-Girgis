-- ============================================================================
-- Part D - GROUP BY, HAVING and aggregate functions (time-box: 25 min)
-- Run:  python lab_runner.py sql/part_d_aggregates.sql [--only D3]
-- Return EXACTLY the columns named in RETURNS, in that order. Round percentages with ROUND(x, 1).
-- ============================================================================

-- @Q D1 Conditional aggregation: results per build
-- TASK: one row per build_id found in test_runs with the number of runs by result.
-- RETURNS: build_id, total, passed, failed, skipped     (counts of the exact values 'pass', 'fail', 'skip')
-- HINT-1: SUM(CASE WHEN result = 'pass' THEN 1 ELSE 0 END) AS passed ...
-- HINT-2: you will see a build_id that is not in the builds table. That is an orphan, not your mistake.
-- TODO: write your statement below and delete the placeholder
SELECT build_id, count(*) as total,
SUM(CASE WHEN result = 'pass' THEN 1 ELSE 0 END) AS passed,
SUM(CASE WHEN result = 'fail' THEN 1 ELSE 0 END) AS failed,
SUM(CASE WHEN result = 'skip' THEN 1 ELSE 0 END) AS skipped
from test_runs group by build_id;

-- @Q D2 GROUP BY + HAVING: duplicate executions
-- TASK: (build_id, case_id) pairs that have more than one run.
-- RETURNS: build_id, case_id, n
-- TODO: write your statement below and delete the placeholder
select build_id, case_id, count(*) as n
from test_runs group by build_id, case_id having n > 1;

-- @Q D3 Join + GROUP BY: failure rate per module
-- TASK: for every module, the number of runs, the number of failed runs and the failure percentage
--       rounded to 1 decimal. Only runs whose case exists count (join test_runs to test_cases).
-- RETURNS: module, runs, failed, fail_pct
-- HINT-1: 100.0 * failed / runs  (the 100.0 avoids integer division; see the aggregate-functions lesson)
-- TODO: write your statement below and delete the placeholder
SELECT module, count(*) as runs, 
SUM(CASE WHEN result = 'fail' THEN 1 ELSE 0 END) AS failed,
Round(100.0 * SUM(CASE WHEN result = 'fail' THEN 1 ELSE 0 END) / count(*), 1) as fail_pct
from test_cases c inner join test_runs r on c.case_id = r.case_id
group by module;


-- @Q D4 Profile a column: defects by severity
-- TASK: one row per severity value (NULL gets its own row) with the number of defects and how many are 'open'.
-- RETURNS: severity, defects, open_defects
-- HINT-1: look for the rows that are NOT low/medium/high/critical: they are validity defects.
-- TODO: write your statement below and delete the placeholder
SELECT severity, count(*) as defects,
SUM(CASE WHEN status = 'open' THEN 1 ELSE 0 END) AS open_defects
from defects group by severity;

-- @Q D5 Reconciliation: reported totals vs reality (LEFT JOIN + GROUP BY + HAVING)
-- TASK: builds whose reported_total differs from the real number of rows in test_runs.
--       A build with NO runs must be reported too (real total 0).
-- RETURNS: build_id, reported_total, actual_total
-- HINT-1: LEFT JOIN runs, then COUNT(r.run_id) (not COUNT(*): COUNT(*) would count the lonely build row as 1).
-- HINT-2: HAVING reported_total <> COUNT(r.run_id)   (you may use the column since it is in GROUP BY)
-- TODO: write your statement below and delete the placeholder
SELECT b.build_id, reported_total, COUNT(run_id) as actual_total
from builds b left join test_runs r on b.build_id = r.build_id
group by b.build_id having reported_total <> actual_total;

-- @Q D6 WHERE vs HAVING: average duration per tester
-- TASK: only runs with a VALID duration (1 to 3600 s) count. Per tester value (NULL and blank included as they are),
--       return the number of runs and the average duration rounded to 1 decimal, but only for testers with at least 5 such runs.
-- RETURNS: tester, runs, avg_duration
-- HINT-1: row filter -> WHERE (before grouping); group filter -> HAVING (after grouping).
-- TODO: write your statement below and delete the placeholder
SELECT tester, count(*) as runs, round(avg(duration_sec),1) as avg_duration
from test_runs where duration_sec between 1 and 3600
group by tester having runs > 5;

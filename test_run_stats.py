"""
test_run_stats.py -- Part C: your unit tests for run_stats.py.

Run from the starter_code folder:
    python -m unittest -v test_run_stats

Requirements (see README, Part C):
  * at least 12 test methods
  * one regression test for each of the 7 bugs (a test that FAILS on the buggy file and PASSES once fixed)
  * at least 3 tests of bad or unusual input (empty list, n == 0, missing key)
  * at least 1 test that uses assertRaises
  * at least 1 use of setUp (so tests stay independent) and at least 1 use of assertLogs (Part B)
Name every test for the behaviour it protects, e.g. test_even_count_averages_two_middle_values.
"""
import unittest

import logging

import run_stats


class TestMedian(unittest.TestCase):
    # Worked example: Arrange, Act, Assert.
    def test_odd_count_returns_middle(self):
        values = [5, 1, 3]                       # Arrange
        result = run_stats.median(values)        # Act
        self.assertEqual(result, 3)              # Assert

    def test_even_count(self):
        values = [5,1,3,4]
        result = run_stats.median(values) 
        self.assertEqual(result, 3.5)

    def test_empty_list(self):
        values = []
        with self.assertRaises(ValueError):
             run_stats.median(values)

    def test_input_list_not_changed(self):
        values = [5,1,3,4]
        run_stats.median(values) 
        self.assertEqual(values, [5,1,3,4])

    # TODO: even count (BUG-1)    TODO: empty list (BUG-7)    TODO: input list not changed


class TestSlowest(unittest.TestCase):
    def test_n_items_biggest_first(self):
        values = [5,1,3,4]
        n = 2
        result = run_stats.slowest(values, n)
        self.assertEqual(result, [5, 4])

    def test_n_is_zero(self):
        values = [5,1,3,4]
        n = 0
        result = run_stats.slowest(values, n)
        self.assertEqual(result, [])

    def test_caller_list_not_reordered(self):
        values = [5,1,3,4]
        result = run_stats.slowest(values, 3)
        self.assertEqual(values, [5,1,3,4])

    # TODO: n items, biggest first (BUG-2)    TODO: n == 0    TODO: caller's list not reordered (BUG-6)


class TestGroupBySuite(unittest.TestCase):
    def test_counts_per_suite_and_status(self):
        values = [{"test": "t1", "suite": "login", "status": "pass"}, {"test": "t2", "suite": "login", "status": "fail"},
               {"test": "t3", "suite": "login", "status": "pass"}, {"test": "t4", "suite": "search", "status": "pass"}]
        result = run_stats.group_by_suite(values)
        self.assertEqual(result, {"login": {"pass": 2, "fail": 1}, "search": {"pass": 1}})

    def test_missing_suite_is_unknown(self):
        values = [{"test": "t1", "status": "pass"}]
        result = run_stats.group_by_suite(values)
        self.assertEqual(result, {"unknown": {"pass": 1}})

    def test_assertLogs_warning(self):
        values = [{"test": "t1", "status": "pass"}]
        run_stats.group_by_suite(values)
        self.assertLogs(run_stats.logger, logging.WARNING)

    # TODO: counts per suite and status (BUG-3)    TODO: missing "suite" -> "unknown"    TODO: assertLogs WARNING


class TestNoteFlaky(unittest.TestCase):
    def test_default_list_different_state(self):
        run_stats.note_flaky("first")
        result = run_stats.note_flaky("second")
        self.assertNotEqual(len(result), 2)

    def test_caller_list_extended(self):
        result = run_stats.note_flaky("second", ["first"])
        self.assertEqual(result, ["first", "second"])

        
    # TODO: two calls without a list do not share state (BUG-4)    TODO: caller's own list is extended


class TestRunIdAndSummary(unittest.TestCase):
    def setUp(self):
        run_stats.run_counter = 0                # reset shared state so each test starts clean
        self.results = [{"test": "t1", "suite": "login", "status": "pass"}]

    def test_ids_increase(self):
        r1 = run_stats.next_run_id()
        r2 = run_stats.next_run_id()
        self.assertEqual(r1, "RUN-0001")
        self.assertEqual(r2, "RUN-0002")

    def test_summary_with_durations(self):
        res = [{"test": "t1", "suite": "login", "status": "pass"}]
        data = [120, 80, 340, 95, 210, 150]
        result = run_stats.summarise_run(res, data)
        self.assertEqual(result.get("latest_ms"), 150)
        self.assertEqual(result.get("median_ms"), 135.0)
        self.assertEqual(result.get("slowest_ms"), [340, 210, 150])

    def test_summary_with_empty_durations(self):
        res = [{"test": "t1", "suite": "login", "status": "pass"}]
        result = run_stats.summarise_run(res, [])
        self.assertEqual(result.get("latest_ms"), None)
        self.assertEqual(result.get("median_ms"), None)
        self.assertEqual(result.get("slowest_ms"), [])
        
    # TODO: ids increase (BUG-5)    TODO: summary with durations (latest_ms, BUG-6)    TODO: summary with [] (BUG-7)


if __name__ == "__main__":
    unittest.main()

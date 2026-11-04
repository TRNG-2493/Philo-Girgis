# SELECTION_LOG.md (Task 7)

Fill in the "Selected" column by running the command with `--collect-only -q` and counting
test lines, or by reading the summary line. Then write one sentence on when you would use it.

| Command | Selected | When would a team run this? |
|---|---|---|
| `python -m pytest -m smoke` |  |  |
| `python -m pytest -m "regression and not slow"` |  |  |
| `python -m pytest -m boundary` |  |  |
| `python -m pytest -k username` |  |  |
| `python -m pytest -k "grade and not raises"` |  |  |
| `python -m pytest tests/test_cart.py::TestCartTotal::test_empty_cart_total_is_zero` |  |  |

## Questions
1. Temporarily change one `@pytest.mark.smoke` to `@pytest.mark.smok` (typo) and run pytest. What happens and what exit code do you get? Why is that useful? (Undo the typo afterwards!)
2. What exit code did a run that selected zero tests give? (Try `-k zzzz`.) 
3. Which test is your xfail, and what would you see if the bug were fixed (strict=True)?

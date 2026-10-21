"""qe_foundations/ingestion/validation.py  -  TASK 5 (about 25 min)

validate_record() decides whether ONE raw record is acceptable.

IMPORTANT: this is VALIDATION OF EXTERNAL DATA. Raise exceptions here.
Do NOT use `assert` in this file (assertions can be switched off with `python -O`).
See README Task 5 for the exact rules.
"""

from .exceptions import InvalidValueError, MissingFieldError

REQUIRED_FIELDS = ("id", "title", "status", "duration")
VALID_STATUSES = ("pass", "fail", "blocked")


def validate_record(row, raw):
    """Turn one raw record into a clean dict, or raise a RecordValidationError.

    row : 1-based record number in the file (used in error messages)
    raw : whatever the reader produced for that record (usually a dict)

    Returns {"id": str, "title": str, "status": str, "duration": float}.
    """
    if not isinstance(raw, dict):
        raise InvalidValueError(row, "record", raw, dict)

    for key in REQUIRED_FIELDS:
        if raw.get(key) == None or str(raw.get(key)).strip() == "":
            raise MissingFieldError(row, key)
    if not str(raw["status"]).strip().lower() in VALID_STATUSES:
        raise InvalidValueError(row, "status", raw, VALID_STATUSES)
    temp = {}
    temp["id"] = str(raw["id"]).strip()
    temp["title"] = str(raw["title"]).strip()
    temp["status"] = str(raw["status"]).strip().lower()
    try:
        temp["duration"] = float(str(raw["duration"]))
        check = temp.get("duration")
        if check < 0 or check != temp.get("duration") or check == float("inf"):
             raise InvalidValueError(row, "duration", raw, float)
    except ValueError as e:
            raise InvalidValueError(row, "duration", raw, float) from e

    return temp

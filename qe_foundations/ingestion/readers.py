"""qe_foundations/ingestion/readers.py  -  TASK 4 (about 35 min)

One reader per format. Each returns a LIST OF RAW RECORDS and only deals with
FORMAT problems (is this valid CSV / JSON / XML? are the columns there?).
Business rules (is the status allowed? is the duration negative?) belong to
validation.py, NOT here.

Always open files through open_source() from contexts.py.
See README Task 4 for the exact contract of each reader.
"""

import csv
import json
import xml.etree.ElementTree as ET

from .contexts import open_source
from .exceptions import MalformedSourceError, MissingColumnsError
from .validation import REQUIRED_FIELDS


def read_csv_records(path):
    """CSV -> list of raw dicts, one per data row (use csv.DictReader)."""
    raw = []
    with open_source(path) as file:
        reader = csv.DictReader(file)
        raw = list(reader)
        missing = ["id","title","status","duration"]
        if not reader.fieldnames:
            raise MissingColumnsError(path, missing)
        for k in reader.fieldnames:
            if k in missing:
                missing.remove(k)
        if not missing == []:
            raise MissingColumnsError(path, missing)
    return raw



def read_json_records(path):
    """JSON -> list of raw records. Expected shape: {"suite": ..., "tests": [...]}."""
    raw = []
    try:
        with open_source(path) as file:
            data = json.load(file)
            if not isinstance(data, dict):
                raise MalformedSourceError(path, "wrong shape")
            raw = data.get("tests")
            if not isinstance(raw, list):
                raise MalformedSourceError(path, "no tests")
    except json.JSONDecodeError as e:
        raise MalformedSourceError(path, e) from e
    return raw
        


def read_xml_records(path):
    """XML -> list of raw dicts, one per <testcase> element at ANY depth."""
    raw = []
    try:
        with open_source(path) as file:
            root = ET.fromstring(file.read())
            for case in root.iter("testcase"):
                temp = {"id": case.get("id", None),
                        "status": case.get("status", None),
                        "title": case.findtext("title", None),
                        "duration": case.findtext("duration", None)}
                raw.append(temp)
    except ET.ParseError as e:
        raise MalformedSourceError(path, e) from e

    return raw

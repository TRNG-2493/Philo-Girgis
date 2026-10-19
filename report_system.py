"""Test-report system: TestResult, Suite, Reporter (+ subclasses).

Fill in every TODO. Run `python3 check_exercise.py N` after each part.
Until a method is finished it raises NotImplementedError; that is normal.
"""


class TestResult:
    """One test outcome. Validated, with two alternative constructors."""

    # Class variables (shared by every TestResult). Given to you.
    VALID_STATUSES = ("PASS", "FAIL", "SKIPPED", "ERROR")
    total_created = 0

    def __init__(self, test_id, status, duration=0.0, message=""):
        # TODO 1.1: validate test_id with TestResult.is_valid_id (ValueError if bad)
        #           and store it in self._test_id
        if TestResult.is_valid_id(test_id):
            self._test_id = test_id
        else:
            raise ValueError("bad test id")
        # TODO 1.2: set self.status and self.duration THROUGH the properties below,
        #           so validation runs. Store message as a plain attribute.
        self.status = status
        self.duration = duration
        self.message = message
        # TODO 1.3: increase TestResult.total_created ONLY after everything above
        #           succeeded (an object that fails validation must not be counted)
        TestResult.total_created +=1


    # ---- static helper ----------------------------------------------------
    @staticmethod
    def is_valid_id(test_id):
        """True for text like 'TC-001': 'TC-' followed by one or more digits."""
        # TODO 1.4: return True/False. Never raise, even for None or numbers.
        check = str(test_id)
        return check.startswith("TC-") and check[3:].isdigit()

    # ---- encapsulated attributes -------------------------------------------
    @property
    def test_id(self):
        # TODO 1.5: return the stored id. Read-only: do NOT write a setter.
        return self._test_id

    @property
    def status(self):
        # TODO 1.6: return the stored status
        return self._status

    @status.setter
    def status(self, value):
        # TODO 1.6: must be text; strip and upper-case it; it must be in
        #           VALID_STATUSES, otherwise raise ValueError (include the bad
        #           value in the message). Only then store it in self._status.
        check = str(value).strip().upper()
        if check in TestResult.VALID_STATUSES:
            self._status = check
        else:
            raise ValueError(f"bad status: {str(value)}")


    @property
    def duration(self):
        # TODO 1.7: return the stored duration
        return self._duration

    @duration.setter
    def duration(self, value):
        # TODO 1.7: not int/float (or a bool) -> TypeError; negative -> ValueError;
        #           otherwise store float(value) in self._duration
        if isinstance(value, bool) or not isinstance(value, (int, float)):
            raise TypeError("wrong type")
        elif value < 0:
            raise ValueError("cant be negative")
        else:
            self._duration = float(value)

    @property
    def is_passed(self):
        # TODO 1.8: True only when the status is "PASS". Read-only.
        return self.status == "PASS"

    # ---- class methods ------------------------------------------------------
    @classmethod
    def how_many(cls):
        # TODO 1.9: return the class-level counter
        return cls.total_created

    @classmethod
    def from_dict(cls, data):
        # TODO 1.10: keys "test_id" and "status" are required (ValueError naming the
        #            missing key); "duration" (default 0.0) and "message"
        #            (default "") are optional. Build the object with cls(...),
        #            NOT TestResult(...).
        id = data.get("test_id")
        stat = data.get("status")
        dur = data.get("duration")
        mes = data.get("message")
        if not id:
            raise ValueError("missing test_id")
        elif not stat:
            raise ValueError("missing status")
        elif not dur and not mes:
            return cls(id, stat)
        elif dur and not mes:
            return cls(id, stat, duration = dur)
        elif not dur and mes:
            return cls(id, stat, message = mes)
        else:
            return cls(id, stat, dur, mes)

    @classmethod
    def from_csv_line(cls, line):
        # TODO 1.11: "TC-001,PASS,1.2,Timeout after 30s" -> TestResult.
        #            Only the first two fields are required. The message may
        #            itself contain commas (hint: split with a limit).
        #            Strip spaces around each field. Too few fields -> ValueError.
        check = str(line)
        temp = check.split(",", 3)
        for i in range(len(temp)):
            temp[i] = temp[i].strip()
        if len(temp) < 2:
            raise ValueError("too few fields")
        elif len(temp) == 2:
            return cls(temp[0], temp[1])
        elif len(temp) == 3:
            if "".join(temp[2].split(".")).isdigit():
                return cls(temp[0], temp[1], duration = float(temp[2]))
            else:
                return cls(temp[0], temp[1], message = temp[2])
        else:
            return cls(temp[0], temp[1], float(temp[2]), temp[3])



    # ---- text forms ----------------------------------------------------------
    def __str__(self):
        # TODO 1.12: "TC-001 PASS 1.2s"  (add " - <message>" only if there is one)
        if self.message:
            return f"{self.test_id} {self.status} {self.duration}s - {self.message}"
        else:
            return f"{self.test_id} {self.status} {self.duration}s"

    def __repr__(self):
        # TODO 1.12: "TestResult('TC-001', 'PASS', 1.2)"; use the REAL class name
        #            via type(self).__name__ so subclasses print correctly
        return f"{type(self).__name__}('{self.test_id}', '{self.status}', {str(self.duration)})"


class Suite:
    """A named collection of TestResult objects (has-a, not is-a list)."""

    def __init__(self, name):
        # TODO 2.1: name must be non-empty text after strip() (else ValueError);
        #           store the stripped name in self.name
        # TODO 2.2: create the per-object list self._results here (NOT in the class body)
        if not isinstance(name, str) or name.strip() == "":
            raise ValueError("name must be non-empty text")
        self.name = name.strip()
        self._results = []

    @classmethod
    def from_rows(cls, name, rows):
        # TODO 2.3: build a suite and add TestResult.from_dict(row) for every row
        newSuite = cls(name)
        for row in rows:
            newSuite.add(TestResult.from_dict(row))
        return newSuite


    def add(self, result):
        # TODO 2.4: not a TestResult -> TypeError; a test_id already in the suite
        #           -> ValueError (mention the id); otherwise append
        if not isinstance(result, TestResult):
            raise TypeError("not a TestResult")
        if result.test_id in list(map(lambda case: case.test_id, self.results)):
            raise ValueError(f"{result.test_id} is in the suite")
        self._results.append(result)


    @property
    def results(self):
        # TODO 2.5: return a COPY of the internal list
        return list(self._results)

    def count(self):
        # TODO 2.5: number of results
        return len(self._results)

    @property
    def pass_rate(self):
        # TODO 2.6: percent (0-100) of results whose status is PASS, out of ALL
        #           results (SKIPPED counts in the total). Empty suite -> 0.0
        passCount = 0
        total = len(self.results)
        if total == 0:
            return 0.0
        for case in self.results:
            if case.status == "PASS":
                passCount +=1
                
        return 100*passCount/total

    @property
    def total_duration(self):
        # TODO 2.7: sum of all durations
        total = 0
        for case in self.results:
            total += case.duration
        return total


    def counts_by_status(self):
        # TODO 2.7: dict with EVERY valid status as a key, e.g.
        #           {"PASS": 2, "FAIL": 1, "SKIPPED": 1, "ERROR": 0}
        statuses = {"PASS": 0, "FAIL": 0, "SKIPPED": 0, "ERROR": 0}
        for case in self.results:
            statuses[case.status] += 1
        return statuses

    def failures(self):
        # TODO 2.8: list of results whose status is FAIL or ERROR (suite order)
        failure = []
        for c in self.results:
            if c.status == "FAIL" or c.status == "ERROR":
                failure.append(c)
        return failure

    def slowest(self, n=1):
        # TODO 2.8: the n slowest results, slowest first (sorted with a key)
        return sorted(self.results, key=lambda x: x.duration, reverse=True)[:n]


class Reporter:
    """Base class: presentation only. Subclasses override render()."""

    def __init__(self, suite):
        # TODO 3.1: suite must be a Suite (else TypeError); store it as self.suite
        if not isinstance(suite, Suite):
            raise TypeError("not a suite")
        self.suite = suite

    @staticmethod
    def pad(text, width):
        # TODO 3.2: str(text) left-justified in `width` characters. Never cut text.
        return str(text).ljust(width)

    @staticmethod
    def format_percent(value):
        # TODO 3.2: 66.666 -> "66.7%"
        return str(round(value, 1))+"%"

    def render(self):
        # TODO 3.3: the base class has no format: raise NotImplementedError with a
        #           helpful message naming the class (type(self).__name__)
        raise NotImplementedError(type(self).__name__)

    def line_count(self):
        # TODO 3.4: number of lines in self.render(). Write it ONCE here;
        #           subclasses inherit it.
        return len(self.render().split("\n"))


class TextReporter(Reporter):
    def render(self):
        # TODO 3.5: see README for the exact layout (header, one row per result,
        #           "Pass rate: 50.0%"). Use self.pad and self.format_percent.
        lines = ["=== Login ==="]
        for c in self.suite.results:
            lines.append(Reporter.pad(c.test_id, 8)+Reporter.pad(c.status, 11)+
                         str(round(c.duration, 1))+"s")
        lines.append("Pass rate: "+Reporter.format_percent(self.suite.pass_rate))
        return "\n".join(lines)


class CsvReporter(Reporter):
    HEADER = "test_id,status,duration,message"

    @staticmethod
    def clean_cell(text):
        # TODO 3.6: commas -> ";" and newlines -> " " so one cell stays one cell
        return text.replace(",", ";").replace("\n", " ")

    def render(self):
        # TODO 3.6: see README for the exact layout
        lines = [CsvReporter.HEADER]
        for c in self.suite.results:
            lines.append(c.test_id+","+c.status+","+str(round(c.duration, 1))+","+
                         CsvReporter.clean_cell(c.message))
        return "\n".join(lines)


# STRETCH 1: add class MarkdownReporter(Reporter) here (see README).

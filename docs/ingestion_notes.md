# Ingestion module - design notes (Week 2, Day 2)

Answer in your own words, 2-4 sentences each.

## 1. Exceptions or assertions?
In `validate_record` I raise exceptions for a negative duration, but in
`IngestionReport.check_invariants` I use `assert`. Why is each the right tool there?

validate_record is meant to reject negative duration while check_invariants is meant to prove that we rejected the right stuff

## 2. Stop or collect?
Why is a malformed JSON file a `SourceError` that stops loading, while a record with
a bad status is a `RecordValidationError` that is collected? Who benefits from this split?

we stop loading at sourceerror since our code expects not malformation, but we raise recordvalidaitonerror for bad statuses in order to keep records of them. we ultimately benefit from the split since we can determine exactly what the error is and fix it

## 3. What did the context managers buy you?
Name two things that `open_source` / `LoadSession` do for the caller that you would otherwise
have to remember to do at every call site.

open_source handles file errors while loadsession sets up the path

## 4. Your hardest bad-input case
Which sample file or record took you longest to handle, and what was the cause?

xml took the longest solely because I was unfamiliar with it

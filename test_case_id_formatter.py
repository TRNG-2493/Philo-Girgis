print("=== Test-Case ID Formatter ===")

module_prefix = input("Module prefix (e.g. login): ").strip().upper()
test_number = int(input("Test number: "))
test_title = input("Test title: ").strip()
total_tests = int(input("Tests executed: "))
passed_tests = int(input("Tests passed: "))

test_id = module_prefix+"-"+str(test_number).zfill(4)
print(f"Generated ID: {test_id}")
print(f"Title: {test_title}")

failed_tests = total_tests - passed_tests
pass_rate = round(passed_tests / total_tests * 100,1) 
release_ready = pass_rate >= 90

print(f"Failed: {failed_tests}")
print(f"Pass rate: {pass_rate}%")
print(f"Release ready: {release_ready}")

# THIS SOFTWARE IS DISTRBUTED AS IS

import argparse
import glob
import json
import re
import sys
import time
from subprocess import PIPE, Popen, run

success_count, fail_count = 0, 0

parser = argparse.ArgumentParser(description='This is the compiler test running script!')
parser.add_argument('--ignore-warnings', default=False, dest='ignore_Warnings')
parser.add_argument('-n', '--test-name', default='all', action="store")
parser.add_argument('--silent', default=False, dest='none-verbose')
parser.add_argument('--clean', default=False, dest='clean-first')
parser.add_argument('-f', '--failed',
                    help="Run failed tests from previous invocation",
                    action="store_true")
parser.add_argument('--write-pprint', default=False,
                    help="Write PPrint output from each test to a corresponding"
                         " file in tests/pprint_output/\{\}+pprintout.ul",
                    action="store_true")
parser.add_argument('--check-pprint', default=False,
                    help="Insure all outputed pretty print files are valid",
                    action="store_true")
parser.add_argument('--check-ast', default=False,
                    help="Don't compare the existing, stored PPrint output to the latest one generated",
                    action="store_true")
parser.add_argument('--custom-test-dir', default=None,
                    help="Specify a different directory of tests than ./tests to run on") 
parser.add_argument('--store-ir', default=False, action="store_true") 
parser.add_argument('--fail-fast', default=False, action="store_true") 


class FailedToCompileError(Exception):
    pass


def collect_files(testdir):
    accepts, rejects = [], []
    for f in glob.glob('{}/*.ul'.format(testdir)):
        if 'accept' in f:
            accepts.append(f)
        else:
            rejects.append(f)

    return accepts, rejects


def run_tests(test_list, write_pprint=False, ignore_ast=False):
    failed_tests = []
    for test in test_list:
        failed = run_on_test_file(test, 'reject' in test or 'invalid' in test, write_pprint, ignore_ast)
        if failed is not None:
            failed_tests.append(failed)
    if failed_tests:
        with open('.tests-cache', 'w') as f:
            f.write(json.dumps({'failed_tests': failed_tests}))
        print("=" * 70)
        print("Wrote failed tests to Cache. Rerun with --failed")


def run_on_test_file(test, reject, write_pprint, check_pprint):
    global success_count, fail_count
    prog = run(['java', 'Compiler', test], stdout=PIPE, stderr=PIPE)
    err = prog.stderr.decode('utf-8')
    out = prog.stdout.decode('utf-8')
    if ((err or 'Error' in out) and not reject):
        print("FAILED: {} - FAILURE TO ACCEPT. Output:".format(test))
        print(out if 'Error' in out else err) 
        fail_count += 1
        return test
    elif reject and not err and 'Error' not in out:
        print("FAILED: {} - FAILURE TO REJECT".format(test))
        fail_count += 1
    else:
        print("Correctly {} {}".format('rejected' if reject else 'accepted', test.split('/')[-1]))
        success_count += 1
    test_name = re.search(r'/(.*)\.ul', test).group(1)
    if not reject and write_pprint: 
        with open('tests/pprint_output/pprintout-{}.ul'.format(test_name), 'w') as f:
            f.write(out)
            print("---> Successfully wrote", f.name)
    elif check_pprint and not reject:
        with open('tests/pprint_output/pprintout-{}.ul'.format(test_name), 'r') as f:
            val = f.read()
            assert val == out, "PPrint output was different from existing PPrint.\
                Insure you have initialized the tests/pprint_output/ dir by running with --write-pprint"

def compile_proj():
    global success_count, fail_count
    make = run(['make'], stdout=PIPE, stderr=PIPE)
    err = make.stderr.lower()
    if b'err' in err:
        raise FailedToCompileError(
            "Antlr wasn't able to compile your grammar\n {}".format(err))
    if b'warn' in err:
        raise FailedToCompileError(
            "Antlr Compiled with warnings: {}\n. Rerun with -f to override this\n".format(err))


def collect_failed_files(a, r):
    tests = []
    try:
        with open('.tests-cache', 'r') as f:
            tests = json.loads(f.read())
    except FileNotFoundError:
        print("=" * 70)
        print("No Test Cache found. Run without --failed first")
    tests = tests['failed_tests']
    return [t for t in tests if t in a], [t for t in tests if t in r]


def main(args):
    global success_count, fail_count
    start = time.time()
    try:
        compile_proj()
    except FailedToCompileError as e:
        print("FailedToCompileErr: {}".format(e))
        return
    if args.test_name != 'all': # run a specific test
        run_on_test_file(
            'tests/{}{}'.format(args.test_name, '.ul' if '.ul' not in args.test_name else ''),
            reject='reject' in args.test_name)
        return

    print("Javac Compiled with no Errors or warnings.\n")
    accept, reject = collect_files('tests/pprint_output' if args.check_pprint else 'tests')
    if args.failed:
        accept, reject = collect_failed_files(accept, reject)

    accept.extend(reject)
    run_tests(accept, args.write_pprint, args.check_ast)
    end = time.time()
    num_tests = str(len(accept))
    print("=" * 70)
    print("All Tests completed. Ran {} tests with {} failures in {} seconds.".format(num_tests, str(fail_count), end - start))


if __name__ == "__main__":
    args = parser.parse_args()
    main(args)

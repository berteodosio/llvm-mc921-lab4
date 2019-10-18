#!/bin/bash

./script.sh tests/test0.sm > result0.txt
diff tests/test0.res result0.txt

./script.sh tests/test1.sm > result1.txt
diff tests/test1.res result1.txt

./script.sh tests/test2.sm > result2.txt
diff tests/test2.res result2.txt

./script.sh tests/test3.sm > result3.txt
diff tests/test3.res result3.txt

./script.sh tests/test4.sm > result4.txt
diff tests/test4.res result4.txt

./script.sh tests/test5.sm > result5.txt
diff tests/test5.res result5.txt

./script.sh tests/test6.sm > result6.txt
diff tests/test6.res result6.txt

./script.sh tests/test7.sm > result7.txt
diff tests/test7.res result7.txt

./script.sh tests/test8.sm > result8.txt
diff tests/test8.res result8.txt

./script.sh tests/test9.sm > result9.txt
diff tests/test9.res result9.txt

echo "[RUN_SCRIPT] Diff completed"
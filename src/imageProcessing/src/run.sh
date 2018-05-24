!/bin/sh

python3 gva.py ../tmp/* ../result/result.txt

python3 afterProcess.py ../result/result.txt

cd ../tmp

rm -rf *
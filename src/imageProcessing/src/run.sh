!/bin/sh

python3 gva.py ../tmp/*.png ../result/result.txt

python3 afterProcess.py ../result/result.txt
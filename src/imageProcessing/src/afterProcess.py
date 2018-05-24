import os
import sys
ROOT = "../result/"



def process(input_path):
        n1 = 0
        lines = []
        with open(input_path, 'r+') as f:
            while True:
                line = f.readline()
                if not line: break
                for i in range(len(line)-1):
                    if line[i] == "{":
                        n1 += 1
                    if line[i] == ':':
                        line = line[:i] + '=' + line[i+1:]
                lines.append(line)
            f.seek(0)
            f.writelines(lines)
            f.close()

        with open(input_path, 'a') as f:
            while(n1 > 0):
                f.write("\n}")
                n1 -= 1

        with open(input_path, 'r') as f:
            while True:
                line = f.readline()
                if not line: break
                print(line, end='')



if __name__ == "__main__":
    input_path = sys.argv[1]
    process(input_path)
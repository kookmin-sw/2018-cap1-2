from pytesseract import *
from PIL import Image
import sys

def OCR(img, path='result',lang='eng'):
    im = Image.open(img)
    text = image_to_string(im, lang = lang)
    f = open('../' + path + '/result.txt','w')
    f.write(text)


if __name__ == '__main__':
    PATH = sys.argv[1]
    OCR('../images/text.png',PATH)
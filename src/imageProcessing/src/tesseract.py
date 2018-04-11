from pytesseract import *
from PIL import Image
import sys

def OCR(img_src, txt_dst, lang='eng'):
    img = Image.open(img_src)
    text = image_to_string(img, lang = lang)
    f = open(txt_dst, 'w')
    f.write(text)


if __name__ == '__main__':
    OCR(sys.argv[1], sys.argv[2])


from pytesseract import *
import cv2
from PIL import Image

def OCR(img, lang='eng'):
    im = Image.open(img)
    text = image_to_string(im, lang = lang)
    f = open('../result/result.txt','w')
    f.write(text)


if __name__ == '__main__':
    OCR('../images/text.png')
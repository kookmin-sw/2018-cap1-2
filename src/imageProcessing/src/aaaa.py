from sys import argv
from os import makedirs
import os
from os.path import join, basename
import json
import requests
from base64 import b64encode
import cv2

from google.cloud import vision
from google.cloud.vision import types

bufferX = 558
bufferY = 630

img = cv2.imread('../images/back.jpg')

ret , thresh1 = cv2.threshold(img,127,0,cv2.THRESH_BINARY)

cv2.imwrite('../images/black.jpg',thresh1)

target = cv2.imread('../images/resized.jpg')
infos = target.shape

thresh1[ 200 : infos[0] + 200, 200 : infos[1] + 200] = target
buffered = thresh1

cv2.imwrite('../images/buffered.jpg',buffered)
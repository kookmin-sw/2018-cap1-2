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

print(vision.text_detection("10.jpg"))
import cv2
import numpy as np
import matplotlib.pyplot as plt
from PIL import Image
import pytesseract as pyt


"""
파일 불러오기 및 영상 처리
"""

def process():
	"""
	파일 불러오기 및 히스토그램 균일화
	"""
	img = cv2.imread('../images/pseudo.jpg', cv2.IMREAD_GRAYSCALE)
	clahe = cv2.createCLAHE(clipLimit = 2.0, tileGridSize(8,8))
	img2 = clahe.apply(img)


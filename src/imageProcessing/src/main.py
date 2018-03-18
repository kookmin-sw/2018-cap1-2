import cv2
import numpy as np
import matplotlib.pyplot as plt
from PIL import Image
import pytesseract as pyt
import argparse
import cv2
import os


"""
파일 불러오기 및 영상 처리
"""
def process():

	#파일 불러오기 및 히스토그램 균일화

	path = "../images/*.jpg"
	img = cv2.imread('../images/clear.jpg', cv2.IMREAD_GRAYSCALE)
	clahe = cv2.createCLAHE(clipLimit = 2.0, tileGridSize = (8,8))
	img2 = clahe.apply(img)


	#퓨리에 변환 실시

	f = np.fft.fft2(img2)
	fshift = np.fft.fftshift(f)
	rows , cols = img.shape
	crow, ccol = int(rows/2), int(cols/2)

	ranges = 5
	
	fshift[crow-ranges:crow+ranges, ccol-ranges:ccol+ranges] = 0
	
	f_ishift = np.fft.ifftshift(fshift)
	img_back = np.fft.ifft2(f_ishift)
	img_back = np.abs(img_back)

	#임계값 설정

	thresholder = 55
	whiteValue = 255


	#블러 이미지 생성 및 임계값 적용, 최종 이미지 저장

	blur = cv2.GaussianBlur(img_back,(5,5),0)

	ret1,finalImg = cv2.threshold(blur,thresholder,whiteValue,cv2.THRESH_BINARY)

	cv2.imwrite('../images/ss.jpg',finalImg)


    #테저렉트 실행(인식 불량)

process()



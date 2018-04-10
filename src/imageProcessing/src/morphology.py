import cv2
import numpy as np

def process():
    img = cv2.imread("../images/ss.jpg")
    kernel = np.ones((5,5), np.uint8)
    img_erosion = cv2.erode(img,kernel,iterations=2)
    #img_dilation = cv2.dilate(img,kernel,iterations=1)

    cv2.imwrite("../images/erode.jpg", img_erosion)

process()
import cv2
import numpy as np

def process():
    img = cv2.imread("../images/ss.jpg")
    kernel = np.ones((5,5), np.uint8)
    img_erosion = cv2.erode(img,kernel,iterations=2)
    img_dilation = cv2.dilate(img_erosion,kernel,iterations=1)

    cv2.imwrite("../images/erode.jpg", img_dilation)

process()

#for i in range(len(k)):
 #   for j in k[i]:
  #      x = j.getX()
   #     y = j.getY()
    #    w = j.getW()
     #   h = j.getH()
      #  chars = img[y: y + h, x: x + w]
       # chars = cv2.resize(chars, None, fx=0.045, fy=0.045, interpolation=cv2.INTER_AREA)
        #constant = cv2.copyMakeBorder(chars, 67, 67, 120, 120, cv2.BORDER_CONSTANT)
        #cv2.imwrite("../chars/" + str(number) + ".jpg", constant)
        #number = number + 1
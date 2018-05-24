import cv2
import numpy as np
import cntClass
import os
ROOT = "/home/shinjong93/바탕화면/pycharm/capstone/src/imageProcessing/new_images"

def extract():
    imageList = [[]]
    result = []

    #imageList , img = contour.convex()

    number = 1
    blank = np.zeros((384, 512, 3), np.uint8)
    color = tuple(reversed((0, 0, 0)))
    blank[:] = color

    imgPath = ROOT  # + str(folder[0])
    imgs = os.listdir(imgPath)
    imgs.sort()

    for imgname in imgs:
        tmp = cv2.imread("../converted/"+imgname)
        x = 1600 / tmp.shape[1]
        y = 900 / tmp.shape[0]
        print(x)
        print(y)
        tmp = cv2.resize(tmp, None, fx=(x), fy=(y), interpolation=cv2.INTER_AREA)
        tmp = cv2.bitwise_not(tmp)
        result.append(tmp)
        cv2.imwrite("../tmp/" + imgname, tmp)


# for i in range(1,11):

        #x = i / imageList[0][0].getH()
        #y = i / imageList[0][0].getH()

    # blank[ int(256-0.5*x) : int(256+0.5*x),int(192-0.5*y): int(192+0.5*y)] = tmp
    # tmp = cv2.copyMakeBorder(tmp, int(225-0.5*y), int(225-0.5*y), int(400-0.5*x), int(400-0.5*x), cv2.BORDER_CONSTANT)

    # constant = cv2.copyMakeBorder(tmp, 384-int(0.5*y), 384-int(0.5*y), 512-int(0.5*x), 512-int(0.5*x), cv2.BORDER_CONSTANT)
    # cv2.imwrite("../images/" + str(number+10000) + ".jpg", constant)





extract()
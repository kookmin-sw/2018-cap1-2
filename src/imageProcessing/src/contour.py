import cv2
import sys
import numpy as np
import cntClass


#   For understanding the structure below code.
#   EX>
#   k is list for line list. k = [[line One] , [line Two] , [line Three] , ...]
#
#   Each line list(list line line One mentioned above) have cntClass object detected before,
#   line One = [cntClass1 , cntClass2, cntClass3, ...]
#   Total cntClass numbers are same with detected chars in img.
#
#   ntClass object have 4 values of x,y,w,h and value of line position
#   Class can return every value it has, also has a method that return center coordinate value.
#   Check cntClass.py for how method defined.
#
#   Finally, structure of complete list would be
#   k = [[cntClass1, cntClass2, cntClass3, ...] , [cntClass1, cntClass2, ...] , ...]
#
#   Accessing to each chars contour would be k[line number][index of chars].method()
#


def sortContour(cnts):
    boundingBoxes = [cv2.boundingRect(c) for c in cnts]
    (cnts, boundingBoxes) = zip(*sorted(zip(cnts,boundingBoxes), key=lambda b: b[1][1]))
    #sort by x*y corrd for make
    return (cnts,boundingBoxes)



def convex():

        img = cv2.imread("../images/erode.jpg")
        imgray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        ret, thr = cv2.threshold(imgray, 55, 255, 0)
        _, contours, _ = cv2.findContours(thr, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        bufferX = 558
        bufferY = 630

        cnts, boxes = sortContour(contours)
        k = [[]]
        corY = boxes[0][1]
        corH = boxes[0][3]
        line = 1

        for i in range(len(boxes)):
            x, y, w, h = boxes[i][0], boxes[i][1], boxes[i][2], boxes[i][3]
            tmp = cntClass.contours()
            tmp.setData(x, y, w, h)
            if y >= corY - corH and y <= corY +corH:
                tmp.setLine(line)
            else:
                corY = boxes[i][1]
                line += 1
                tmp.setLine(line)
                k.append([])
            k[tmp.getLine() - 1].append(tmp)
            #cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 3)

        for i in range(len(k)):
            for j in range(len(k[i])):
                k[i].sort(key=lambda x :x.getX())
               # cv2.putText(img, "#{}".format(j + 1), (k[i][j].getX() + int(0.5 * k[i][j].getW()) - 20, k[i][j].getY() + int(0.5 * k[i][j].getH())),
                #            cv2.FONT_HERSHEY_SIMPLEX,
                #            3.0, (0, 255, 0), 2)

        number = 1
        for i in range(len(k)): #i is number of total lines
            for j in k[i]: #j is single char of each line
                heightCheck = j.getCenterY()
                index = 0
                for p in k[i]:
                    if j.getX() == p.getX():
                        continue
                    else:
                        if p.getX() <= j.getCenterX() and j.getCenterX() <= p.getX() + p.getW() and p.getY() <= heightCheck:
                            tmpW = p.getW()
                            tmpH = j.getH() + j.getY() - p.getY()
                            p.setData(p.getX(),p.getY(),tmpW,tmpH)
                            j.setData(p.getX(),p.getY(),tmpW,tmpH)
                            del k[i][index]
                    index += 1

            number = 1
            blank = np.zeros((384,512,3),np.uint8)
            color = tuple(reversed((0,0,0)))
            blank[:] = color
        tmp = img
        y = 9/k[0][1].getH()
        x = y
        tmp = cv2.resize(tmp,None, fx=(x), fy=(y), interpolation=cv2.INTER_AREA)
           # blank[ int(256-0.5*x) : int(256+0.5*x),int(192-0.5*y): int(192+0.5*y)] = tmp
            #tmp = cv2.copyMakeBorder(tmp, int(225-0.5*y), int(225-0.5*y), int(400-0.5*x), int(400-0.5*x), cv2.BORDER_CONSTANT)
        tmp = cv2.bitwise_not(tmp)
        cv2.imwrite("../images/" + str(number + 20000) + ".jpg", tmp)
        print(x * k[0][0].getW())
        print(y * k[0][1].getH())
            #constant = cv2.copyMakeBorder(tmp, 384-int(0.5*y), 384-int(0.5*y), 512-int(0.5*x), 512-int(0.5*x), cv2.BORDER_CONSTANT)
            #cv2.imwrite("../images/" + str(number+10000) + ".jpg", constant)

      #  for i in range(len(k)):
        #    for j in k[i]:
                #number = number + 1
               # chars= img[k[i][0].getY(): k[i][len(k[i]) - 1].getY() + k[i][len(k[i]) - 1].getH()
               #     , k[i][0].getX(): k[i][len(k[i]) - 1].getX() + k[i][len(k[i]) - 1].getW()]
               # chars = cv2.resize(chars, None, fx=y, fy=y, interpolation=cv2.INTER_AREA)
                #blank[int(256 - 0.5 * x): int(256 + 0.5 * x), int(192 - 0.5 * y): int(192 + 0.5 * y)] = chars
               # constant = blank
               # constant = cv2.copyMakeBorder(chars, 10, 10, 10, 10, cv2.BORDER_CONSTANT)
               # constant = cv2.bitwise_not(constant)
               # cv2.imwrite("../images/"+str(number)+".jpg",constant)


              #  cv2.rectangle(img, (j.getX(), j.getY()), (j.getX() + j.getW(), j.getY() + j.getH()), (0, 0, 255), 3)
          #  cv2.imwrite("../images/" + str(number) + ".jpg", img[k[i][0].getY(): k[i][len(k[i]) - 1].getY() + k[i][len(k[i])-1].getH()
                      #  , k[i][0].getX(): k[i][len(k[i])-1].getX() + k[i][len(k[i])-1].getW()])





        cv2.imwrite("../images/contours.jpg", img)




convex()


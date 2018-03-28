import cv2
import sys
import cntClass


#   p = cntClass.contours()
#   p.setData(x,y,w,h)
#
#
#
#
#
#
#
#
def draw_contour(image, c, i):
    # compute the center of the contour area and draw a circle
    # representing the center
    M = cv2.moments(c)
    cX = int(M["m10"] / M["m00"])
    cY = int(M["m01"] / M["m00"])

    # draw the countour number on the image
    cv2.putText(image, "#{}".format(i + 1), (cX - 20, cY), cv2.FONT_HERSHEY_SIMPLEX,
                3.0, (0, 255, 0), 2)

    # return the image with the contour number drawn on it
    return image


def sortContour(cnts):
    boundingBoxes = [cv2.boundingRect(c) for c in cnts]
    (cnts, boundingBoxes) = zip(*sorted(zip(cnts,boundingBoxes), key=lambda b: b[1][1]))
    #sort by x*y corrd for make
    return (cnts,boundingBoxes)



def convex():

        img = cv2.imread("../images/ss.jpg")
        imgray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        ret, thr = cv2.threshold(imgray, 55, 255, 0)
        _, contours, _ = cv2.findContours(thr, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

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
            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 3)

        for i in range(len(k)):
            for j in range(len(k[i])):
                k[i].sort(key=lambda x :x.getX())
                cv2.putText(img, "#{}".format(j + 1), (k[i][j].getX() + int(0.5 * k[i][j].getW()) - 20, k[i][j].getY() + int(0.5 * k[i][j].getH())),
                            cv2.FONT_HERSHEY_SIMPLEX,
                            3.0, (0, 255, 0), 2)
        cv2.imwrite("../images/contours.jpg", img)


convex()


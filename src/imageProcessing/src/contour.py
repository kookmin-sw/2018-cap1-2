import cv2


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
        print(boxes)
        k = 0
        height = 0
        for i in range(len(boxes)):
            x, y, w, h = boxes[i][0], boxes[i][1], boxes[i][2], boxes[i][3]
            k = k + boxes[i][1]
            height = boxes[i][3]
            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 3)

            img = draw_contour(img, cnts[i], i)

        avg = (k / len(boxes))
        avgH = (h / len(boxes))
        lineOne = []
        lineTwo = []
        lineThree = []

        for i in range(len(boxes)):
            y = boxes[i][1]
            if y >= 0 and y <= avg - avgH:
                lineOne.append(boxes[i])
            elif y >= avg and y <= avg + avgH:
                lineTwo.append(boxes[i])
            elif y > avg + avgH:
                lineThree.append(boxes[i])

        print(lineOne)
        cv2.imwrite("../images/contours.jpg", img)


convex()


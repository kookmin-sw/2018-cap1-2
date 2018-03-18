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


def isSame(bx, by, bw, bh, x, y, w, h):
    result = False
    if (by - bh) > (y - h) > 0:
        result = True
    return result


def sort_contours(cnts, method="left-to-right"):
    reverse = False
    i = 0

    #reverse
    if method == "right-to-left" or method == "bottom-to-top" :
        reverse = True

    # sorting by y-coordinate
    if method == "top-to-bottom" or method == "bottom-to-top" :
        i = 1

    # construct the list of boxes and sort
    boundingBoxes = [cv2.boundingRect(c) for c in cnts]
    (cnts, boundingBoxes) = zip(*sorted(zip(cnts,boundingBoxes), key=lambda b: b[1][i], reverse=reverse))

    #print(boundingBoxes)

    return (cnts,boundingBoxes)


def convex():

        img = cv2.imread("../images/ss.jpg")
        imgray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        ret, thr = cv2.threshold(imgray, 55, 255, 0)
        _, contours, _ = cv2.findContours(thr, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        cnts, boxes = sort_contours(contours, "top-to-bottom")
        bx, by, bw, bh = 0, 0, 0, 0
        for i in range(len(boxes)):
            x, y, w, h = boxes[i][0] , boxes[i][1], boxes[i][2], boxes[i][3]
            if i > 0:
                if (by - y) >= 0:
                    if(bw - w) >= 0:
                        cv2.rectangle(img, (bx, by), (bx + bw, by + bh), (0, 0, 255), 3)
                    else:
                        cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 3)
                else:
                    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 3)
            else:
                cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 3)
            bx, by, bw, bh = x, y, w, h
            img = draw_contour(img, contours[i], i)


        cv2.imwrite("../images/contours.jpg", img)


convex()

#need to do
#to make chars like i,= in one contour,
#consider the height between contours.(maybe)
#if that is close enough, make them as same contour
#
#two chars become one contour if they are too close
#need to find a way to seperate them and make contours each.
#
#final goal is make contours for every chars, and contours should be
#seperated from each other.

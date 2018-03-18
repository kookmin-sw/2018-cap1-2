import numpy as np
import argparse
import cv2

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
    (cnts, boundingBoxes) = zip(*sorted(zip(cnts,boundingBoxes), key=lambda  b:b[1][i], reverse=reverse))

    return (cnts,boundingBoxes)